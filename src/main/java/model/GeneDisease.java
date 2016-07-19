package model;

/**
 * POJO representing an entity in the disease_gene table under the new schema.
 * Created by allengong on 7/15/16.
 */
public class GeneDisease {

    private String DOID;
    private String ensembleId;
    private String pertubation = null;
    private String relationship;
    private String weblink;
    private String dataSource;

    public GeneDisease(String DOID, String ensembleId, String pertubation, String relationship, String weblink, String dataSource) {
        this.DOID = DOID;
        this.ensembleId = ensembleId;
        this.pertubation = pertubation;
        this.relationship = relationship;
        this.weblink = weblink;
        this.dataSource = dataSource;
    }

    public String getDOID() {
        return DOID;
    }

    public void setDOID(String DOID) {
        this.DOID = DOID;
    }

    public String getEnsembleId() {
        return ensembleId;
    }

    public void setEnsembleId(String ensembleId) {
        this.ensembleId = ensembleId;
    }

    public String getPertubation() {
        return pertubation;
    }

    public void setPertubation(String pertubation) {
        this.pertubation = pertubation;
    }


    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return "GeneDisease{" +
                "DOID='" + DOID + '\'' +
                ", ensembleId='" + ensembleId + '\'' +
                ", pertubation='" + pertubation + '\'' +
                ", relationship='" + relationship + '\'' +
                ", weblink='" + weblink + '\'' +
                ", dataSource='" + dataSource + '\'' +
                '}';
    }
}
