package entités;

public class Produit {

    private int refProduit;
    private String designation;
    private String description;
    private double prix;
    private String image;
    private int qteStocke;

    public Produit() {
    }

    public Produit(int refProduit, String designation, String description, double prix, String image, int qteStocke) {
        this.refProduit = refProduit;
        this.designation = designation;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.qteStocke = qteStocke;
    }

    public int getQteStocke() {
        return qteStocke;
    }

    public void setQteStocke(int qteStocke) {
        this.qteStocke = qteStocke;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getRefProduit() {
        return refProduit;
    }

    public void setRefProduit(int refProduit) {
        this.refProduit = refProduit;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "refProduit=" + refProduit +
                ", designation='" + designation + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", image='" + image + '\'' +
                ", qteStocke=" + qteStocke +
                '}';
    }
}
