package Service;

import entités.Reservation22;
import entités.User;

import java.sql.SQLException;
import java.util.List;
public interface IService <T>{

    void ajouter(T t) throws SQLException;
    void supprimer(T t) throws SQLException;

    void upadte(Reservation22 reservation22);

    void update(T t) throws SQLException;

    void supprimer(int idService) throws SQLException;

    T getById(int id) throws SQLException;
    List<T> getAll() throws SQLException;
}