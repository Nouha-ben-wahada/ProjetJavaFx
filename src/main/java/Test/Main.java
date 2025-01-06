package Test;

import Controllers.GestionCommandeController;
import Service.ServiceGestionCommande;
import entités.Produit;
import entités.GestionCommande;

public class Main {
        public static void main(String[] args) {

                GestionCommande commande = new GestionCommande(2); // Utilisateur avec ID 1

                // Ajouter des produits directement depuis la base de données
                ServiceGestionCommande service = new ServiceGestionCommande();
                service.ajouterProduitDepuisBDD(commande, "REF001", 2);
                service.sauvegarderCommandeDansBDD(commande);

                // Afficher la commande
                commande.afficherCommande();
                System.out.println("Montant total : " + commande.getMontantTotal());

                commande = new GestionCommande(2); // Utilisateur avec ID 1
                GestionCommandeController CommandeController = new GestionCommandeController();
                service.ajouterProduitDepuisBDD(commande, "REF123", 2);
                CommandeController.initialize();
                CommandeController.supprimerProduitDepuisCommande("REF123");
                service.sauvegarderCommandeDansBDD(commande);

                // Afficher la commande
                commande.afficherCommande();
                System.out.println("Montant total : " + commande.getMontantTotal());
        }
}