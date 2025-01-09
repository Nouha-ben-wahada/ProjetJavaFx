package Service;

import ConnectionDB.DataBaseConnection;
import entités.Produit;
import entités.Vetements;
import entités.Accessoires;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceProduitUser {
    private Connection connection;

    public ServiceProduitUser() {
        this.connection = DataBaseConnection.getInstance().getConnection();
    }

    // Recherche générale dans la table `produit`
    public ObservableList<Produit> getProduitsFiltrés(String categorie, String sousCategorie, String nomProduit) {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM produit WHERE 1=1");

        if (categorie != null && !categorie.isEmpty()) queryBuilder.append(" AND LOWER(categorie) = LOWER(?)");
        if (sousCategorie != null && !sousCategorie.isEmpty()) queryBuilder.append(" AND LOWER(sousCategorie) = LOWER(?)");
        if (nomProduit != null && !nomProduit.isEmpty()) queryBuilder.append(" AND LOWER(nom) LIKE LOWER(?)");

        try (PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString())) {
            int index = 1;
            if (categorie != null && !categorie.isEmpty()) pstmt.setString(index++, categorie.toLowerCase());
            if (sousCategorie != null && !sousCategorie.isEmpty()) pstmt.setString(index++, sousCategorie.toLowerCase());
            if (nomProduit != null && !nomProduit.isEmpty()) pstmt.setString(index, "%" + nomProduit.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) produits.add(createProduitFromResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche: " + e.getMessage());
        }

        return produits;
    }

    // Recherche spécifique dans la table `vetements`
    public ObservableList<Produit> getProduitsFromVetements(String sousCategorie, String nomProduit) {
        ObservableList<Produit> produits = FXCollections.observableArrayList();

        String query = "SELECT * FROM `vetements` WHERE sousCategorie = ? AND nom LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sousCategorie);
            preparedStatement.setString(2, "%" + nomProduit + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Produit vetements = new Produit();
                //vetements.setReference(resultSet.getString("reference"));
                vetements.setNom(resultSet.getString("nom"));
                //vetements.setDescription(resultSet.getString("description"));
                vetements.setPrix(resultSet.getDouble("prix"));
               // vetements.setStock(resultSet.getInt("stock"));
                vetements.setCategorie(resultSet.getString("categorie"));
                vetements.setSousCategorie(resultSet.getString("sousCategorie"));
               // vetements.setTaille(resultSet.getString("taille"));
              //  vetements.setCouleur(resultSet.getString("couleur"));

                produits.add(vetements);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }


    // Recherche spécifique dans la table `accessoires`
    public ObservableList<Produit> getProduitsFromAccessoires(String categorie, String nomProduit) {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM accessoires WHERE 1=1");

        if (categorie != null && !categorie.isEmpty()) queryBuilder.append(" AND LOWER(categorie) = LOWER(?)");
        if (nomProduit != null && !nomProduit.isEmpty()) queryBuilder.append(" AND LOWER(nom) LIKE LOWER(?)");

        try (PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString())) {
            int index = 1;
            if (categorie != null && !categorie.isEmpty()) pstmt.setString(index++, categorie.toLowerCase());
            if (nomProduit != null && !nomProduit.isEmpty()) pstmt.setString(index, "%" + nomProduit.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                produits.add(new Accessoires(
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("stock"),
                        rs.getString("categorie"),
                        "Accessoire",
                        rs.getString("couleur"),
                        rs.getString("matiere")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche dans accessoires: " + e.getMessage());
        }

        return produits;
    }

    // Méthode pour créer un objet Produit depuis un ResultSet
    private Produit createProduitFromResultSet(ResultSet rs) throws SQLException {
        String sousCategorie = rs.getString("sousCategorie");
        if ("vêtement".equalsIgnoreCase(sousCategorie)) {
            return new Vetements(
                    rs.getString("reference"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getDouble("prix"),
                    rs.getInt("stock"),
                    rs.getString("categorie"),
                    sousCategorie,
                    rs.getString("taille"),
                    rs.getString("couleur")
            );
        } else {
            return new Accessoires(
                    rs.getString("reference"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getDouble("prix"),
                    rs.getInt("stock"),
                    rs.getString("categorie"),
                    sousCategorie,
                    rs.getString("couleur"),
                    rs.getString("matiere")
            );
        }
    }
    public int getIdByUserName(String userName) throws SQLException {
        String query = "SELECT id FROM `user` WHERE user_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, userName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1; // Retourne -1 si aucun utilisateur n'a été trouvé
    }
}