package service;

import model.Disease;
import model.Gene;
import model.GeneDisease;
import model.GeneDiseasePublication;

import java.sql.*;

/**
 * Handle all DB access, singleton class.
 * Created by allengong on 7/15/16.
 */
public class DBAccess {

    private static DBAccess singleton;
    private static Connection conn;

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/COPADB_LOCAL?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DBAccess() {};

    public static DBAccess getInstance() {
        if (singleton == null) {
            singleton = new DBAccess();
        }

        return singleton;
    }

    // get a connection object
    public Connection getConnection() throws Exception {
        if (conn == null) {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }

        return conn;
    }

    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    /* begin insert logic */
    public void insertIntoGeneTable(Gene gene) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            getConnection(); // ensure that conn is initialized

            System.out.println("\nInserting gene into table...");
            System.out.println(gene);

            //check to see if an existing row for this ensemble id exists
            String checkString = "select * from gene where ensembl_id=?";
            ps = conn.prepareStatement(checkString);
            ps.setString(1, gene.getEnsembleId());
            rs = ps.executeQuery();

            // if there is a result, then there is a row in the DB with this ensembl id, need to skip
            if (rs.next() == true) {
                System.out.println("Row exists, skipping!");
                return;
            }
            //System.out.println("Performing insert...");
            // else we are good to insert
            String insertString = "insert into gene (ensembl_id, gene_symbol, chromosome, species_id) " +
                    "values (?, ?, ?, ?)";
            ps = conn.prepareStatement(insertString);
            ps.setString(1, gene.getEnsembleId());
            ps.setString(2, gene.getGene_symbol());
            ps.setString(3, gene.getChromosome());
            ps.setInt(4, gene.getSpecies());

            int status = ps.executeUpdate();
            System.out.println("Insert with result: " + status + "!");

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertIntoDiseaseTable(Disease disease) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            getConnection(); // ensure that conn is initialized

            System.out.println("\nInserting disease into table...");
            System.out.println(disease);

            //check to see if an existing row for this ensemble id exists
            String checkString = "select * from disease where disease_id=?";
            ps = conn.prepareStatement(checkString);
            ps.setString(1, disease.getTerms());
            rs = ps.executeQuery();

            // if there is a result, then there is a row in the DB with this ensembl id, need to skip
            if (rs.next() == true) {
                System.out.println("Row exists, skipping!");
                return;
            }

            //System.out.println("Performing insert...");

            // else we are good to insert
            String insertString = "insert into disease (disease_id, name, description, heart_disease, notes, disease_url) " +
                    "values (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertString);
            ps.setString(1, disease.getTerms());
            ps.setString(2, disease.getName());
            ps.setString(3, disease.getDescription());
            ps.setInt(4, disease.getHEART_DISEASE());
            ps.setString(5, disease.getNotes());
            ps.setString(6, disease.getDiseaseUrl());

            int status = ps.executeUpdate();
            System.out.println("Insert with result: " + status + "!");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertIntoDiseaseGeneTabe(GeneDisease gd) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            getConnection(); // ensure that conn is initialized

            System.out.println("\nInserting disease-gene into table...");
            System.out.println(gd);

            //check to see if an existing row for this ensemble id exists
            String checkString = "select * from disease_gene where disease_id=? and ensembl_id=?";
            ps = conn.prepareStatement(checkString);
            ps.setString(1, gd.getDOID());
            ps.setString(2, gd.getEnsembleId());
            rs = ps.executeQuery();

            // if there is a result, then there is a row in the DB with this ensembl+disease id pair, need to skip
            if (rs.next() == true) {
                System.out.println("Row exists, skipping!");
                return;
            }

            //System.out.println("Performing insert...");

            // else we are good to insert
            String insertString = "insert into disease_gene (disease_id, ensembl_id, perturbation, " +
                    "relationship, weblink, data_source) " +
                    "values (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertString);
            ps.setString(1, gd.getDOID());
            ps.setString(2, gd.getEnsembleId());
            ps.setString(3, gd.getPertubation());
            ps.setString(4, gd.getRelationship());
            ps.setString(5, gd.getWeblink());
            ps.setString(6, gd.getDataSource());

            int status = ps.executeUpdate();

            System.out.println("Insert with result: " + status + "!");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertIntoDiseaseGenePublicationTabe(GeneDiseasePublication gdp) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            getConnection(); // ensure that conn is initialized

            System.out.println("\nInserting disease-gene-publication into table...");
            System.out.println(gdp);

            //check to see if an existing row for this ensemble id exists
            String checkString = "select * from disease_gene_publication where disease_id=? and ensembl_id=? and " +
                    "pubmed_id=?";
            ps = conn.prepareStatement(checkString);
            ps.setString(1, gdp.getDisease_url());
            ps.setString(2, gdp.getEnsembl_id());
            ps.setString(3, gdp.getPubmed_id());

            rs = ps.executeQuery();

            // if there is a result, then there is a row in the DB with this ensembl+disease id pair, need to skip
            if (rs.next() == true) {
                System.out.println("Row exists, skipping!");
                return;
            }

            //System.out.println("Performing insert...");

            // else we are good to insert
            String insertString = "insert into disease_gene_publication (disease_id, ensembl_id, pubmed_id, " +
                    "pubmed_title, pubmed_author) " +
                    "values (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertString);
            ps.setString(1, gdp.getDisease_url());
            ps.setString(2, gdp.getEnsembl_id());
            ps.setString(3, gdp.getPubmed_id());
            ps.setString(4, gdp.getPubmed_title());
            ps.setString(5, gdp.getPubmed_author());

            int status = ps.executeUpdate();

            System.out.println("Insert with result: " + status + "!");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // new, to add the missing notes field to disease_gene
    public void updateDiseaseGeneWithNotes(GeneDisease gd, String notes) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            getConnection(); // ensure that conn is initialized

            System.out.println("\nUpdating gene_disease with note...");
            System.out.println(gd + " " + notes);

            //check to see if an existing row for this ensemble id exists
            String checkString = "select * from disease_gene where disease_id=? and ensembl_id=? and " +
                    "notes <> ''";
            ps = conn.prepareStatement(checkString);
            ps.setString(1, gd.getDOID());
            ps.setString(2, gd.getEnsembleId());

            rs = ps.executeQuery();

            // if there is a result, then there is a row in the DB with this ensembl+disease id pair, need to skip
            if (rs.next() == true) {
                System.out.println("Row exists, skipping!");
                return;
            }

            System.out.println("Performing Update...");

            // else we are good to insert
            String updateString = "update disease_gene set notes='" + notes + "' " +
                    "where disease_id='" + gd.getDOID() + "' " + "AND ensembl_id='" + gd.getEnsembleId() + "';";
            ps = conn.prepareStatement(updateString);

            int status = ps.executeUpdate();

            System.out.println("Update with result: " + status + "!");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

} // end class
