package Service;

import Controllers.GestionCommandeController;
import entités.GestionCommande;
import entités.Produit;
import ConnectionDB.DataBaseConnection;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.Map;

public class ServiceGestionCommande {
    private final Connection connection;

    public ServiceGestionCommande() {
        this.connection = ConnectionDB.DataBaseConnection.getInstance().getConnection();
    }

    public int getLastIdCommande() {
        String query = "SELECT MAX(id_commande) FROM commande";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1); // Get the result from the first column
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du dernier id_commande : " + e.getMessage());
        }
        return 0; // Return 0 if the table is empty or an error occurs
    }

    // Ajouter un produit à une commande depuis la base de données
    public boolean ajouterProduitDepuisBDD(GestionCommande commande, String referenceProduit, int quantite) {
        if (quantite <= 0) {
            System.err.println("Quantité invalide.");
            GestionCommandeController.afficherAlerte("Erreur", "Impossible d'ajouter le produit : Quantité invalide", Alert.AlertType.ERROR);
            return false;
        }

        String query = "SELECT reference, nom, prix FROM produits WHERE reference = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, referenceProduit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String reference = resultSet.getString("reference");
                    String nom = resultSet.getString("nom");
                    double prix = resultSet.getDouble("prix");

                    Produit produit = new Produit(reference, nom, "", prix, 1, "", "");
                    boolean ajouté = commande.ajouterProduit(produit, quantite);
                    if (ajouté) {
                        System.out.println("Produit ajouté : " + nom + " (Quantité : " + quantite + ")");
                        GestionCommandeController.afficherAlerte("CONFIRMATION", "Produit ajouté : " + nom + " (Quantité : " + quantite + ")", Alert.AlertType.CONFIRMATION);
                        return true;
                    }
                } else {
                    System.err.println("Produit introuvable avec la référence : " + referenceProduit);
                    GestionCommandeController.afficherAlerte("Erreur","Produit introuvable avec la référence : " + referenceProduit, Alert.AlertType.ERROR);

                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du produit : " + e.getMessage());
        }
        return false;
    }

    // Sauvegarder une commande dans la base de données
    public void sauvegarderCommandeDansBDD(GestionCommande commande) {
        String insertCommandeSQL = "INSERT INTO `commande` (id_commande, id_user, montant_total, etat_commande, date_commande) VALUES (?, ?, ?, ?, ?)";
        String insertProduitSQL = "INSERT INTO  `commande_produit` (id_commande, reference_produit, quantite) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertCommandeSQL)) {
                preparedStatement.setInt(1, commande.getIdCommande());
                preparedStatement.setInt(2, commande.getIdUser());
                preparedStatement.setDouble(3, commande.getMontantTotal());
                preparedStatement.setString(4, commande.getEtatCommande());
                preparedStatement.setDate(5, java.sql.Date.valueOf(commande.getDateCommande()));
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertProduitSQL)) {
                for (Map.Entry<Produit, Integer> entry : commande.getProduits().entrySet()) {
                    preparedStatement.setInt(1, commande.getIdCommande());
                    preparedStatement.setString(2, entry.getKey().getReference());
                    preparedStatement.setInt(3, entry.getValue());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }

            connection.commit();
            System.out.println("Commande sauvegardée avec succès dans la base de données.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde de la commande : " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la restauration du mode AutoCommit : " + e.getMessage());
            }
        }
    }

    public boolean supprimerProduitDepuisBDD(String referenceProduit) {
        String query = "DELETE FROM produit WHERE reference = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, referenceProduit);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
        return false;
    }


}
