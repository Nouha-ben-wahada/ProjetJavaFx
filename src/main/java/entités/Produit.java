package entités;

import java.util.Objects;

public class Produit {
    private String reference;
    private String nom;
    private String description;
    private double prix;
    private int stock;
    private String categorie;
    private String sousCategorie;

    // Constructor for Produit class
    public Produit(String reference, String nom, String description, double prix, int stock,
                   String categorie, String sousCategorie) {
        this.reference = reference;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
        this.sousCategorie = sousCategorie;
    }

    // Getters and setters for Produit class fields
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getSousCategorie() {
        return sousCategorie;
    }

    public void setSousCategorie(String sousCategorie) {
        this.sousCategorie = sousCategorie;
    }




    ///////////////////////////////////////////////////////
    // Equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return Objects.equals(reference, produit.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }
    /////////////////////////////////////////////////////////
}
