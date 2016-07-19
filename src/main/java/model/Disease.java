package model;

/**
 * POJO representing an entity in the disease table under the new schema.
 * Created by allengong on 7/15/16.
 */
public class Disease {

    private String terms; // DOID, HP, or MP
    private String name;
    private String description;

    private final int HEART_DISEASE = 1;

    private String notes;
    private String diseaseUrl;

    public Disease(String terms, String name, String description, String notes, String diseaseUrl) {
        this.terms = terms;
        this.name = name;
        this.description = description;
        this.notes = notes;
        this.diseaseUrl = diseaseUrl;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHEART_DISEASE() {
        return HEART_DISEASE;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDiseaseUrl() {
        return diseaseUrl;
    }

    public void setDiseaseUrl(String diseaseUrl) {
        this.diseaseUrl = diseaseUrl;
    }

    @Override
    public String toString() {
        return "Terms: " + terms + " Name: " + name + " " + diseaseUrl + " " + notes;
    }
}
