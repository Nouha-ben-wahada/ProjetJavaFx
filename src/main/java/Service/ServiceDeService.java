package Service;

import ConnectionDB.DataBaseConnection;
import entités.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDeService implements IService<Service> {

    private Connection connection = DataBaseConnection.getInstance().getConnection();

    public ServiceDeService() {
        try {
            connection.createStatement(); // Initialisation inutile ici, sauf si une déclaration est nécessaire
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'établissement de la connexion : " + e.getMessage());
        }
    }

    // Constructeur pour initialiser la connexion à la base de données
    public ServiceDeService(Connection connection) {

        this.connection = connection;
    }

    @Override
    public void ajouter(Service service) throws SQLException {
        String query = "INSERT INTO service (idService, nomService, categorie, duration, prix, description) " +
                "VALUES (:1, :2, :3, :4, :5, :6)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, service.getIdService());
            statement.setString(2, service.getNomService());
            statement.setString(3, service.getCategorie().getLabel().toUpperCase());
            statement.setInt(4, service.getDuration());
            statement.setInt(5, service.getPrix());
            statement.setString(6, service.getDescription());
            statement.executeUpdate();
        }
    }

    @Override
    public void supprimer(Service service) throws SQLException {

    }

    @Override
    public void supprimer(int idService) throws SQLException {
        String query = "DELETE FROM service WHERE idService = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idService);
            statement.executeUpdate();
        }
    }


    @Override
    public Service getById(int idService) throws SQLException {
        String query = "SELECT * FROM service WHERE idService = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idService);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToService(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<Service> getAll() throws SQLException {
        List<Service> servicess = new ArrayList<>();
        String query = "SELECT * FROM service";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                servicess.add(mapResultSetToService(resultSet));
            }
        }
        return servicess;
    }


    @Override
    public void update(Service service) throws SQLException {
        String query = "UPDATE service SET nomService = ?, categorie = ?, " +
                "duration = ?, prix = ?, description = ? WHERE idService = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, service.getNomService());
            statement.setString(2, service.getCategorie().getLabel());
            statement.setInt(3, service.getDuration());
            statement.setInt(4, service.getPrix());
            statement.setString(5, service.getDescription());
            statement.setInt(6, service.getIdService());
            statement.executeUpdate();
        }
    }


    private Service mapResultSetToService(ResultSet resultSet) throws SQLException {
        return new Service(
                resultSet.getInt("idService"),
                resultSet.getString("nomService"),
                Service.Categorie.valueOf(resultSet.getString("categorie").toUpperCase().replace(" ", "_")), // Enum catégorie
                resultSet.getInt("duration"),
                resultSet.getInt("prix"),
                resultSet.getString("description")
        );
    }


    public boolean existe(int idService) throws SQLException {
        String query = "SELECT COUNT(*) FROM service WHERE idService = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idService);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    public void afficherServices() throws SQLException {
        List<Service> servicess = getAll();
        for (Service service : servicess) {
            System.out.println(service);
        }
    }


}
