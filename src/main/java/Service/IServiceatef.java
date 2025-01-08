package Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface générique pour la gestion des services CRUD.
 *
 * @param <T> Type d'entité gérée par le service.
 */
public interface IService<T> {
    /**
     * Ajoute une nouvelle entité dans la base de données.
     *
     * @param entity L'entité à ajouter.
     * @throws SQLException En cas d'erreur lors de l'opération.
     */
    void ajouter(T entity) throws SQLException;

    /**
     * Supprime une entité de la base de données.
     *
     * @param entity L'entité à supprimer.
     * @throws SQLException En cas d'erreur lors de l'opération.
     */
    void supprimer(String reference) throws SQLException;

    /**
     * Met à jour une entité existante dans la base de données.
     *
     * @param entity L'entité à mettre à jour.
     * @throws SQLException En cas d'erreur lors de l'opération.
     */
    void update(T entity) throws SQLException;

    /**
     * Récupère une entité par sa référence unique.
     *
     * @param reference La référence de l'entité à récupérer.
     * @return L'entité correspondante ou null si elle n'existe pas.
     * @throws SQLException En cas d'erreur lors de l'opération.
     */
    T getById(String reference) throws SQLException;

    /**
     * Récupère toutes les entités de la base de données.
     *
     * @return Une liste contenant toutes les entités.
     * @throws SQLException En cas d'erreur lors de l'opération.
     */
    List<T> getAll() throws SQLException;
}
