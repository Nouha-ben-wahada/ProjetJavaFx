package entités;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import ConnectionDB.DataBaseConnection;
import Service.ServiceGestionCommande;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionCommande {
    private static int compteurCommandes = 0; // Compteur pour générer des IDs uniques
    private int idCommande; // Identifiant unique de la commande
    private int idUser; // Référence à l'utilisateur
    private Map<Produit, Integer> produits; // Map des produits et quantités
    private double montantTotal; // Montant total de la commande
    private String etatCommande; // État de la commande (en cours, livrée, annulée)
    private LocalDate dateCommande; // Date de création

    // Constructeur
    public GestionCommande(int idUser) {
        if (idUser <= 0) throw new IllegalArgumentException("ID utilisateur invalide.");
        this.idUser = idUser;
        this.produits = new HashMap<>();
        this.montantTotal = 0;
        this.etatCommande = "En cours";
        this.dateCommande = LocalDate.now();
        synchronized (GestionCommande.class) {
            ServiceGestionCommande service = new ServiceGestionCommande();
            this.idCommande = service.getLastIdCommande() + 1;
        }
    }

//    // Ajouter un produit à la commande
    public boolean ajouterProduit(Produit produit, int quantite) {
        if (quantite <= 0) {
            return false; // Quantité invalide
        }
        produits.put(produit, produits.getOrDefault(produit, 0) + quantite);
        recalculerMontantTotal();
        return true;
    }

    // Supprimer un produit de la commande par référence
//    public void supprimerProduit(String reference) {
//        Produit produitASupprimer = getProduitParReference(reference);
//        if (produitASupprimer != null) {
//            produits.remove(produitASupprimer);
//            recalculerMontantTotal();
//            System.out.println("Produit " + reference + " supprimé.");
//        } else {
//            System.out.println("Produit " + reference + " introuvable.");
//        }
//    }

    // Recalculer le montant total de la commande
    public double recalculerMontantTotal() {
        montantTotal = produits.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrix() * entry.getValue())
                .sum();
        return montantTotal;
    }

    // Mettre à jour l'état de la commande
    public void mettreAJourEtat(String nouvelEtat) {
        this.etatCommande = nouvelEtat;
    }


    @Override
    public String toString() {
        return "GestionCommande{" +
                "idCommande=" + idCommande +
                ", idUser=" + idUser +
                ", produits=" + produits +
                ", montantTotal=" + montantTotal +
                ", etatCommande='" + etatCommande + '\'' +
                ", dateCommande=" + dateCommande +
                '}';
    }

    // Getters
    public int getIdCommande() {
        return idCommande;
    }

    public int getIdUser() {
        return idUser;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public String getEtatCommande() {
        return etatCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public Map<Produit, Integer> getProduits() {
        return produits;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public void afficherCommande() {
        System.out.println("=== Contenu de la commande ===");
        produits.forEach((produit, quantite) ->
                System.out.println(produit.getReference() + " - " + produit.getNom() + " - " + produit.getPrix() + " € - Quantité: " + quantite)
        );
    }

    //public String getModeLivraison() {
    //}

//    public void modifierQuantite(String reference, int nouvelleQuantite) {
//        if (nouvelleQuantite <= 0) {
//            supprimerProduit(reference);
//            return;
//        }
//
//        Produit produit = getProduitParReference(reference);
//        if (produit != null) {
//            produits.put(produit, nouvelleQuantite);
//            System.out.println("Quantité de " + reference + " mise à jour: " + nouvelleQuantite);
//            recalculerMontantTotal(); // Recalculer le montant total après modification
//        } else {
//            System.out.println("Produit " + reference + " introuvable.");
//        }
//    }
//
//    public Produit getProduitParReference(String reference) {
//        return produits.keySet().stream()
//                .filter(produit -> produit.getReference().equals(reference))
//                .findFirst()
//                .orElse(null); // Produit non trouvé
//    }
}

