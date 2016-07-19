package model;

/**
 * Created by allengong on 7/19/16.
 */
public class GeneDiseasePublication {

    private String disease_url;
    private String ensembl_id;
    private String pubmed_id;
    private String pubmed_title;
    private String pubmed_author;

    public GeneDiseasePublication(String disease_url, String ensembl_id, String pubmed_id, String pubmed_title, String pubmed_author) {
        this.disease_url = disease_url;
        this.ensembl_id = ensembl_id;
        this.pubmed_id = pubmed_id;
        this.pubmed_title = pubmed_title;
        this.pubmed_author = pubmed_author;
    }

    public String getDisease_url() {
        return disease_url;
    }

    public void setDisease_url(String disease_url) {
        this.disease_url = disease_url;
    }

    public String getEnsembl_id() {
        return ensembl_id;
    }

    public void setEnsembl_id(String ensembl_id) {
        this.ensembl_id = ensembl_id;
    }

    public String getPubmed_id() {
        return pubmed_id;
    }

    public void setPubmed_id(String pubmed_id) {
        this.pubmed_id = pubmed_id;
    }

    public String getPubmed_title() {
        return pubmed_title;
    }

    public void setPubmed_title(String pubmed_title) {
        this.pubmed_title = pubmed_title;
    }

    public String getPubmed_author() {
        return pubmed_author;
    }

    public void setPubmed_author(String pubmed_author) {
        this.pubmed_author = pubmed_author;
    }

    @Override
    public String toString() {
        return "GeneDiseasePublication{" +
                "disease_url='" + disease_url + '\'' +
                ", ensembl_id='" + ensembl_id + '\'' +
                ", pubmed_id='" + pubmed_id + '\'' +
                ", pubmed_title='" + pubmed_title + '\'' +
                ", pubmed_author='" + pubmed_author + '\'' +
                '}';
    }
}
