package Service;

import entités.Reservation22;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservationList implements IService<Reservation22> {

    // Liste en mémoire pour stocker les réservations
    private List<Reservation22> reservations = new ArrayList<>();
    private int nextId = 1; // ID incrémental pour les réservations

    public ServiceReservationList() {
        // Initialisation si nécessaire
    }

    @Override
    public void ajouter(Reservation22 reservation22) {
        // Générer le prochain ID de réservation
        reservation22.setIdReservation(nextId++);
        reservations.add(reservation22);
        System.out.println("Réservation ajoutée avec succès : " + reservation22);
    }

    @Override
    public void supprimer(Reservation22 reservation22) {
        reservations.remove(reservation22);
        System.out.println("Réservation supprimée : " + reservation22);
    }

    @Override
    public void supprimer(int idReservation) {
        reservations.removeIf(reservation -> reservation.getIdReservation() == idReservation);
        System.out.println("Réservation avec ID " + idReservation + " supprimée.");
    }

    @Override
    public void upadte(Reservation22 reservation22) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getIdReservation() == reservation22.getIdReservation()) {
                reservations.set(i, reservation22);
                System.out.println("Réservation mise à jour : " + reservation22);
                return;
            }
        }
        System.out.println("Réservation non trouvée pour la mise à jour : " + reservation22.getIdReservation());
    }

    /*public void update(int idReservation, LocalDate newDate, LocalTime newTime, Reservation22.Status newStatus) {
        for (Reservation22 reservation : reservations) {
            if (reservation.getIdReservation().equals(idReservation)) {
                reservation.setReservationDate(newDate);
                reservation.setReservationTime(newTime);
                reservation.setStatus(newStatus);
                System.out.println("Réservation mise à jour : " + reservation);
                return;
            }
        }
        System.out.println("Aucune réservation trouvée avec l'ID " + idReservation);
    }*/

    @Override
    public void update(Reservation22 reservation) {
        upadte(reservation); // Réutilise la méthode `upadte`
    }

    @Override
    public List<Reservation22> getAll() {
        return new ArrayList<>(reservations); // Retourne une copie de la liste
    }

    /*@Override
    public void supprimer(String reference) throws SQLException {

    }*/


    @Override
    public Reservation22 getById(int id) {
        for (Reservation22 reservation : reservations) {
            if (reservation.getIdReservation() == id) {
                return reservation;
            }
        }
        return null;
    }

   /* @Override
    public Reservation22 getById(String reference) {
        // Implémentation inutile ici (ou personnalisée selon vos besoins)
        return null;
    }*/

    // Méthode pour afficher toutes les réservations
    public void afficherReservations() {
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation enregistrée.");
        } else {
            for (Reservation22 reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    /*public Reservation22 getById(int idReservation) {
        for (Reservation22 reservation : reservations) {
            if (reservation.getIdReservation().equals(idReservation)) {
                return reservation;
            }
        }
        System.out.println("Aucune réservation trouvée avec l'ID " + idReservation);
        return null;
    }*/
}
