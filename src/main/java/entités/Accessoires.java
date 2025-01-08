package entités;

public class Accessoires extends Produit {
    private String couleur;
    private String matiere;

    // Constructor for Accessoires class
    public Accessoires(String reference, String nom, String description, double prix, int stock,
                       String categorie, String sousCategorie, String couleur, String matiere) {
        // Call to the parent constructor (Produit)
        super(reference, nom, description, prix, stock, categorie, sousCategorie);
        this.couleur = couleur;
        this.matiere = matiere;
    }

    // Getters and setters for Accessoires fields
    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    @Override
    public String toString() {
        return "Accessoires{" +
                "reference='" + getReference() + '\'' +
                ", nom='" + getNom() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", prix=" + getPrix() +
                ", stock=" + getStock() +
                ", categorie='" + getCategorie() + '\'' +
                ", sousCategorie='" + getSousCategorie() + '\'' +
                ", couleur='" + couleur + '\'' +
                ", matiere='" + matiere + '\'' +
                '}';
    }
}
