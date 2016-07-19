package model;

/**
 * POJO representing an entity in the gene table under the new schema.
 * Created by allengong on 7/15/16.
 */
public class Gene {

    private String ensembleId;
    private String gene_symbol;
    private String chromosome = "";
    private int species;

    public Gene(String ensembleId, String gene_symbol, String chromosome, int species) {
        this.ensembleId = ensembleId;
        this.gene_symbol = gene_symbol;
        this.chromosome = chromosome;
        this.species = species;
    }

    public String getGene_symbol() {
        return gene_symbol;
    }

    public String getEnsembleId() {
        return ensembleId;
    }

    public void setEnsembleId(String ensembleId) {
        this.ensembleId = ensembleId;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public void setGene_symbol(String gene_symbol) {
        this.gene_symbol = gene_symbol;
    }

    public int getSpecies() {
        return species;
    }

    public void setSpecies(int species) {
        this.species = species;
    }

    @Override
    public String toString() {
        String s = ensembleId + " " + gene_symbol + " " + chromosome + " " + species;
        return s;
    }

}
