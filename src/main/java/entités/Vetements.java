package entités;

public class Vetements extends Produit {
    private String taille;
    private String couleur;

    // Constructor matching the parameters
    public Vetements(String reference, String nom, String description, double prix, int stock,
                     String categorie, String sousCategorie, String taille, String couleur) {
        super(reference, nom, description, prix, stock, categorie, sousCategorie); // Call to the parent constructor
        this.taille = taille;
        this.couleur = couleur;
    }

    // Getters and setters for taille and couleur
    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    @Override
    public String toString() {
        return "Vetements{" +
                "reference='" + super.getReference() + '\'' +
                ", nom='" + super.getNom() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", prix=" + super.getPrix() +
                ", stock=" + super.getStock() +
                ", categorie='" + super.getCategorie() + '\'' +
                ", sousCategorie='" + super.getSousCategorie() + '\'' +
                ", taille='" + taille + '\'' +
                ", couleur='" + couleur + '\'' +
                '}';
    }
}
