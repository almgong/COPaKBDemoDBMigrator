import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import model.Disease;
import model.Gene;
import model.GeneDisease;
import model.GeneDiseasePublication;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DBAccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Java program to read in a CSV representation of the demo_disease_database,
 * and insert into the new schema.
 *
 * 1. insert Rat and Zebrafish to DB, and update the getSpeciesId() method to return correct values
 * 2. alter tables to have the new, expected fields
 * 3. Need to specifically change the DOID field to allow for HP, MP, etc., may also need to udpate all current DOID
 * values to have the DOID_* prefix -- I opted to just remove DOID_ prefixes and keep HP_.. etc.
 *
 * *** note that 1-3 is in the sql_script, but make sure to double check the values for zebrafish and rat.
 *
 * 4. Run the main class here
 *
 * Created by allengong on 7/15/16.
 */
public class Migrator {

    public static Map<String, String> speciesMap = new HashMap<String, String>();

    public static List<String> getEnsembleId(String species, String geneSymbol) throws Exception {
        String url = "http://rest.ensembl.org/xrefs/symbol/%s/%s?content-type=application/json";
        HttpResponse<JsonNode> jsonRes = Unirest.get(String.format(url, species, geneSymbol)).asJson();

        JSONArray jsonArr = jsonRes.getBody().getArray();
        List<String> ensembleIds = new ArrayList<String>();

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject obj = (JSONObject) jsonArr.get(i);
            String id = (String) obj.get("id");

            // only add valid ids
            if (id.startsWith("EN")) {
                ensembleIds.add(id);
            }
        }

        return ensembleIds;
    }

    public static Map<String, String> getPublicationMetaData(String pmid) throws Exception {
        String url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed" +
                "&retmode=json&rettype=abstract&id=" + pmid;
        HttpResponse<JsonNode> jsonRes = Unirest.get(url).asJson();

        JSONObject obj = jsonRes.getBody().getObject().getJSONObject("result").getJSONObject(pmid);
        System.out.println(obj.get("title"));
        System.out.println(obj.get("lastauthor"));


        return null;
    }

    // returns the species_id, specific to the current COPaDB!!
    public static int getSpeciesId(String species) throws Exception {
        if (species.equals("fly")) {
            return 4;   // Fruit fly
        } else if (species.equals("human")) {
            return 1;
        } else if (species.equals("mouse")) {
            return 2;
        } else if (species.equals("rat")) {
            return 12;
        } else if (species.equals("zebrafish")) {
            return 13;
        } else {
            throw new Exception("Something went terribly wrong in getSpeciesId()");
        }
    }


    public static void main(String [] args) {

        //System.setProperty("jsse.enableSNIExtension", "false"); // deals with SSL issue with unirest

        // counter of number of rows skipped, for sanity check purposes
        int numSkipped = 0;

        // some hardcoded translations between species name found in demo db and expected value by ensembl rest
        // API
        speciesMap.put("human", "human");
        speciesMap.put("rat", "rat");
        speciesMap.put("mouse", "mouse");
        speciesMap.put("zebrafish", "zebrafish");
        speciesMap.put("fly", "fruitfly");

        // file
        BufferedReader br = null;

        try {
            DBAccess dao = DBAccess.getInstance();
            dao.getConnection();                    // causes connection initialization

            // read in CSV file, make necessary inserts line by line
            br = new BufferedReader(new FileReader("src/main/java/demo_disease_table_07182016.csv"));
            String line;
            String[] lineAsArr;

            int count = 0;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                if(count++ == 20) break;

                // preprocess
                line = line.replace("\"", "");  // remove quotes

                // each line is in the form:
                // idx dataSource geneSymbol species disease relationship diseaseURL pubmeds weblink notes terms
                lineAsArr = line.split("\t");

                // skip rows generated from COPaKB, as they are redundant here
                if (lineAsArr[1].equals("COPaKB")) {
                    continue;
                }

                /*
                 *  Build Gene and insert into gene table
                 */

                // use Ensemble rest API to get a list of the ensemble ids

                /* TEMP */
//                List<String> ensembleIds = new ArrayList<String>();
//                ensembleIds.add("dummy");

                List<String> ensembleIds = getEnsembleId(speciesMap.get(lineAsArr[3]),
                        lineAsArr[2]);

                if (ensembleIds.size() == 0) {
                    numSkipped++;
                    continue;   // if no ensemble id found, can't insert into the DB correctly.
                }

                String ensembleId = ensembleIds.get(0);     // first id will be the one used for this row
                Gene gene = new Gene(ensembleId, lineAsArr[2], null, getSpeciesId(lineAsArr[3]));

                dao.insertIntoGeneTable(gene);


                /*
                 * Build Disease and insert into the disease table
                 */
                String terms = lineAsArr[10].replace(";", "");   // get terms, and remove any trailing ;
                terms = terms.replace("DOID_", "");              // if a DOID, remove the prefix to conform to curr db

                Disease disease = new Disease(terms, lineAsArr[4], "", lineAsArr[9], lineAsArr[6]);
                dao.insertIntoDiseaseTable(disease);


                /*
                 * Build gene-disease relationship and insert into the disease_gene table
                 */
                //getPublicationMetaData(lineAsArr[7]); // very slow to do it one by one, do it en masse later
                GeneDisease geneDisease = new GeneDisease(terms, ensembleId, null, lineAsArr[5], lineAsArr[8], lineAsArr[1]);

                dao.insertIntoDiseaseGeneTabe(geneDisease);

                /*
                 * Build gene-disease-publication relationship and insert into disease_gene_publication table
                 */
                GeneDiseasePublication gdp = new GeneDiseasePublication(terms, ensembleId, lineAsArr[7],
                        "", "");

                dao.insertIntoDiseaseGenePublicationTabe(gdp);

                // be polite, sleep for 750 more ms before next REST call to ensembl API
                Thread.sleep(750);
            }

            br.close();
            dao.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // finally

        }

        System.out.println("Num skipped due to missing info, etc. : " + numSkipped);

    }
}
