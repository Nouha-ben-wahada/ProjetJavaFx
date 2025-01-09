package Service;
import entités.Reservation22;
import entités.User;
import ConnectionDB.DataBaseConnection;
import entités.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    private Connection connection = DataBaseConnection.getInstance().getConnection();
    private Statement ste;

    public ServiceUser() {
        try {
            ste = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String query = "INSERT INTO `user` (id, nom, prenom, tel, email, adresse, user_name, password, date_creation_compte, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, user.getId());
        statement.setString(2, user.getNom());
        statement.setString(3, user.getPrenom());
        statement.setString(4, user.getTel());
        statement.setString(5, user.getEmail());
        statement.setString(6, user.getAdresse());
        statement.setString(7, user.getUserName());
        statement.setString(8, user.getPassword());
        statement.setString(9, user.getDateCreationCompte().toString());
        statement.setString(10, user.getRole().name());
        statement.executeUpdate();
    }

    @Override
    public void supprimer(User user) throws SQLException {
        String query = "DELETE FROM `user` WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, user.getId());
        statement.executeUpdate();
    }

    @Override
    public void upadte(Reservation22 reservation22) {

    }

    @Override
    public void update(User t) throws SQLException {
        String query = "UPDATE `user` SET nom = ?, prenom = ?, email = ?, adresse = ?, tel = ?, user_name = ?, password = ?, role = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, t.getNom());
        statement.setString(2, t.getPrenom());
        statement.setString(3, t.getEmail());
        statement.setString(4, t.getAdresse());
        statement.setString(5, t.getTel());
        statement.setString(6, t.getUserName());
        statement.setString(7, t.getPassword());
        statement.setString(8, t.getRole().name());
        statement.setInt(9, t.getId());
        statement.executeUpdate();
    }

    @Override
    public void supprimer(int idService) throws SQLException {

    }

    @Override
    public User getById(int id) throws SQLException {
        String query = "SELECT * FROM `user` WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new User(
                    resultSet.getString("nom"),
                    resultSet.getString("prenom"),
                    resultSet.getString("tel"),
                    resultSet.getString("email"),
                    resultSet.getString("adresse"),
                    resultSet.getString("user_name"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role")) // Assuming Role is inside User
            );
        }
        return null;
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM `user`";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            users.add(new User(
                    resultSet.getString("nom"),
                    resultSet.getString("prenom"),
                    resultSet.getString("tel"),
                    resultSet.getString("email"),
                    resultSet.getString("adresse"),
                    resultSet.getString("user_name"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role")) // Assuming Role is inside User
            ));
        }
        return users;
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
