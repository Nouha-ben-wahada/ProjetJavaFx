package Service;

import entités.Produit;
import entités.Vetements;
import entités.Accessoires;
import ConnectionDB.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit implements IService<Produit> {
    private Connection connection = DataBaseConnection.getInstance().getConnection();
    private Statement ste;

    public ServiceProduit() {
        try {
            ste = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void ajouter(Produit produit) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM vetements WHERE reference = ? UNION SELECT COUNT(*) FROM accessoires WHERE reference = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setString(1, produit.getReference());
        checkStatement.setString(2, produit.getReference());
        ResultSet rs = checkStatement.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("Produit avec cette référence existe déjà !");
            return;
        }

        if (produit instanceof Vetements) {
            String query = "INSERT INTO vetements (reference, nom, description, prix, stock, categorie, sousCategorie, taille, couleur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, produit.getReference());
            statement.setString(2, produit.getNom());
            statement.setString(3, produit.getDescription());
            statement.setDouble(4, produit.getPrix());
            statement.setInt(5, produit.getStock());
            statement.setString(6, produit.getCategorie());
            statement.setString(7, produit.getSousCategorie());
            Vetements vetements = (Vetements) produit;
            statement.setString(8, vetements.getTaille());
            statement.setString(9, vetements.getCouleur());
            statement.executeUpdate();
            System.out.println("Produit de type Vetements ajouté avec succès !");
        } else if (produit instanceof Accessoires) {
            String query = "INSERT INTO accessoires (reference, nom, description, prix, stock, categorie, sousCategorie, couleur, matiere) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, produit.getReference());
            statement.setString(2, produit.getNom());
            statement.setString(3, produit.getDescription());
            statement.setDouble(4, produit.getPrix());
            statement.setInt(5, produit.getStock());
            statement.setString(6, produit.getCategorie());
            statement.setString(7, produit.getSousCategorie());
            Accessoires accessoires = (Accessoires) produit;
            statement.setString(8, accessoires.getCouleur());
            statement.setString(9, accessoires.getMatiere());
            statement.executeUpdate();
            System.out.println("Produit de type Accessoires ajouté avec succès !");
        }
    }

    @Override
    public void supprimer(Produit produit) throws SQLException {
        supprimer(produit.getReference());
    }

    @Override
    public void supprimer(int idService) throws SQLException {
        throw new UnsupportedOperationException("Suppression par ID non supportée pour les produits.");
    }

    public void supprimer(String reference) throws SQLException {
        String queryVetements = "DELETE FROM vetements WHERE reference = ?";
        String queryAccessoires = "DELETE FROM accessoires WHERE reference = ?";
        PreparedStatement statementVetements = connection.prepareStatement(queryVetements);
        statementVetements.setString(1, reference);
        int rowsAffected = statementVetements.executeUpdate();

        if (rowsAffected == 0) {
            PreparedStatement statementAccessoires = connection.prepareStatement(queryAccessoires);
            statementAccessoires.setString(1, reference);
            int accessoriesAffected = statementAccessoires.executeUpdate();
            if (accessoriesAffected == 0) {
                System.out.println("Aucun produit trouvé avec cette référence.");
            } else {
                System.out.println("Produit supprimé avec succès dans la table accessoires !");
            }
        } else {
            System.out.println("Produit supprimé avec succès dans la table vetements !");
        }
    }

    @Override
    public void update(Produit produit) throws SQLException {
        String query;
        PreparedStatement statement;

        if (produit instanceof Vetements) {
            query = "UPDATE vetements SET nom = ?, description = ?, prix = ?, stock = ?, categorie = ?, sousCategorie = ?, taille = ?, couleur = ? WHERE reference = ?";
            statement = connection.prepareStatement(query);
            statement.setString(7, ((Vetements) produit).getTaille());
            statement.setString(8, ((Vetements) produit).getCouleur());
            statement.setString(9, produit.getReference());
        } else if (produit instanceof Accessoires) {
            query = "UPDATE accessoires SET nom = ?, description = ?, prix = ?, stock = ?, categorie = ?, sousCategorie = ?, couleur = ?, matiere = ? WHERE reference = ?";
            statement = connection.prepareStatement(query);
            statement.setString(7, ((Accessoires) produit).getCouleur());
            statement.setString(8, ((Accessoires) produit).getMatiere());
            statement.setString(9, produit.getReference());
        } else {
            throw new IllegalArgumentException("Type de produit non supporté.");
        }

        statement.setString(1, produit.getNom());
        statement.setString(2, produit.getDescription());
        statement.setDouble(3, produit.getPrix());
        statement.setInt(4, produit.getStock());
        statement.setString(5, produit.getCategorie());
        statement.setString(6, produit.getSousCategorie());
        statement.executeUpdate();
        System.out.println("Produit mis à jour avec succès !");
    }

    @Override
    public Produit getById(int id) throws SQLException {
        throw new UnsupportedOperationException("Recherche par ID non supportée pour les produits.");
    }

    public Produit getById(String reference) throws SQLException {
        String queryVetements = "SELECT * FROM vetements WHERE reference = ?";
        String queryAccessoires = "SELECT * FROM accessoires WHERE reference = ?";
        PreparedStatement statementVetements = connection.prepareStatement(queryVetements);
        statementVetements.setString(1, reference);
        ResultSet rsVetements = statementVetements.executeQuery();
        if (rsVetements.next()) {
            return new Vetements(
                    rsVetements.getString("reference"),
                    rsVetements.getString("nom"),
                    rsVetements.getString("description"),
                    rsVetements.getDouble("prix"),
                    rsVetements.getInt("stock"),
                    rsVetements.getString("categorie"),
                    rsVetements.getString("sousCategorie"),
                    rsVetements.getString("taille"),
                    rsVetements.getString("couleur")
            );
        }

        PreparedStatement statementAccessoires = connection.prepareStatement(queryAccessoires);
        statementAccessoires.setString(1, reference);
        ResultSet rsAccessoires = statementAccessoires.executeQuery();
        if (rsAccessoires.next()) {
            return new Accessoires(
                    rsAccessoires.getString("reference"),
                    rsAccessoires.getString("nom"),
                    rsAccessoires.getString("description"),
                    rsAccessoires.getDouble("prix"),
                    rsAccessoires.getInt("stock"),
                    rsAccessoires.getString("categorie"),
                    rsAccessoires.getString("sousCategorie"),
                    rsAccessoires.getString("couleur"),
                    rsAccessoires.getString("matiere")
            );
        }

        return null;
    }

    @Override
    public List<Produit> getAll() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String queryVetements = "SELECT * FROM vetements";
        String queryAccessoires = "SELECT * FROM accessoires";
        Statement statementVetements = connection.createStatement();
        ResultSet rsVetements = statementVetements.executeQuery(queryVetements);
        while (rsVetements.next()) {
            produits.add(new Vetements(
                    rsVetements.getString("reference"),
                    rsVetements.getString("nom"),
                    rsVetements.getString("description"),
                    rsVetements.getDouble("prix"),
                    rsVetements.getInt("stock"),
                    rsVetements.getString("categorie"),
                    rsVetements.getString("sousCategorie"),
                    rsVetements.getString("taille"),
                    rsVetements.getString("couleur")
            ));
        }

        Statement statementAccessoires = connection.createStatement();
        ResultSet rsAccessoires = statementAccessoires.executeQuery(queryAccessoires);
        while (rsAccessoires.next()) {
            produits.add(new Accessoires(
                    rsAccessoires.getString("reference"),
                    rsAccessoires.getString("nom"),
                    rsAccessoires.getString("description"),
                    rsAccessoires.getDouble("prix"),
                    rsAccessoires.getInt("stock"),
                    rsAccessoires.getString("categorie"),
                    rsAccessoires.getString("sousCategorie"),
                    rsAccessoires.getString("couleur"),
                    rsAccessoires.getString("matiere")
            ));
        }

        return produits;
    }
}
