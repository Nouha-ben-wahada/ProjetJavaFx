package org.example;

import ConnectionDB.DataBaseConnection;
import entités.Produit;
import entités.Vetements;
import entités.Accessoires;
import Service.ServiceProduit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Connexion à la base de données
        Connection con = DataBaseConnection.getInstance().getConnection();
        System.out.println("Database connection: " + con);

        // Créer une instance de ServiceProduit
        ServiceProduit serviceProduit = new ServiceProduit();

        // Test pour l'entité Vetements
        Vetements vetement = new Vetements(
                "REF009",                    // Référence
                "T-shirt",                  // Nom
                "T-shirt en coton bio",     // Description
                25.99,                      // Prix
                50,                         // Stock
                "Vêtements",                // Catégorie
                "Homme",                    // Sous-catégorie
                "L",                        // Taille
                "Bleu"                      // Couleur
        );

        // Test pour l'entité Accessoires
        Accessoires accessoire = new Accessoires(
                "REF007",                    // Référence
                "Montre",                   // Nom
                "Montre en acier inoxydable", // Description
                199.99,                     // Prix
                20,                         // Stock
                "Accessoires",              // Catégorie
                "Montres",                  // Sous-catégorie
                "Noir",                     // Couleur
                "Métal"                     // Matière
        );

        // Ajouter le Vetement
        try {
            serviceProduit.ajouter(vetement);
            System.out.println("Vetement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du vetement : " + e.getMessage());
        }

        // Ajouter l'Accessoire
        try {
            serviceProduit.ajouter(accessoire);
            System.out.println("Accessoire ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'accessoire : " + e.getMessage());
        }

        // Récupérer tous les produits
        try {
            List<Produit> produits = serviceProduit.getAll();
            System.out.println("Liste des produits : " + produits);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits : " + e.getMessage());
        }

        // Mettre à jour le Vetement
        vetement.setPrix(29.99); // Modifier le prix
        vetement.setStock(45);  // Mettre à jour le stock
        try {
            serviceProduit.update(vetement);
            System.out.println("Vetement mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du vetement : " + e.getMessage());
        }

        // Récupérer un produit spécifique par référence
        try {
            Produit fetchedProduit = serviceProduit.getById("REF002"); // Utilisez REF002 ici pour tester
            System.out.println("Produit récupéré : " + fetchedProduit);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du produit : " + e.getMessage());
        }

//        // Optionnel : Supprimer l'Accessoire
//        // Vous pouvez décommenter cette partie si vous souhaitez tester la suppression.
//        try {
//            serviceProduit.supprimer(accessoire);
//            System.out.println("Accessoire supprimé avec succès !");
//        } catch (SQLException e) {
//            System.out.println("Erreur lors de la suppression de l'accessoire : " + e.getMessage());
//        }
    }
}
