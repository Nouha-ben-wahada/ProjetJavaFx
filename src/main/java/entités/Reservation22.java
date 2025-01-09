package entités;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;


public class Reservation22 {

    private Integer idReservation;   // Sera généré automatiquement
    private Integer id;
    private String usernameclient;
    //private String prenomclient;
    private Integer idService;
    private String categorie;
    private LocalDate reservation_date;      // Date choisie par le client
    private LocalTime reservation_time;      // Heure choisie par le client
    private Integer montantTotal;                   // Prix issu de la table service
    private Status status;                  // Statut de la réservation

    public Reservation22(int idReservation, String nomClient, String prenomClient, String typeService, LocalDate reservationDate, LocalTime reservationTime, Integer montantTotal, Status status) {
    }

    public Reservation22(ResultSet resultSet) {
    }

    public Reservation22(int idReservation, int id, String nomClient, String prenomClient, int idService, String typeService, LocalDate reservationDate, LocalTime reservationTime, Integer montantTotal, Status status, String tel, String email, String adresse) {
    }

    public Reservation22(int nextIdReservation, int id, String nom, String prenom, int idService, Service.Categorie categorie, LocalDate reservationDate, LocalTime reservationTime, int prix, Status status) {
    }

    public Reservation22(Integer integer, Integer id, String nomClient, String prenomClient, Integer idService, String categorie, LocalDate reservationDate, LocalTime reservationTime, Integer prix, String status) {
    }

    public Reservation22(int idReservation, int id, String usernameclient,
                         int idService, String categorie, LocalDate reservationDate,
                         LocalTime reservationTime, int montantTotal, String status) {
        this.idReservation = idReservation;
        this.id = id;
        this.usernameclient = usernameclient;
        //this.prenomclient = prenomclient;
        this.idService = idService;
        this.categorie = categorie;
        this.reservation_date = reservationDate;
        this.reservation_time = reservationTime;
        this.montantTotal = montantTotal;
        this.status = Status.valueOf(status);
    }

    public Reservation22(Integer idReservation, Integer id, Integer idService, String categorie, LocalDate reservationDate, LocalTime reservationTime, Integer prix, String status) {
    }

    public Reservation22(int i, int i1, String clientName, String clientPrename, Integer idService, String categorie, LocalDate reservationDate, LocalTime reservationTime, Integer prix, String confirmee) {
    }

    public Reservation22(int i, int i1, String clientName, String clientPrename, Service idService, Service categorie, LocalDate reservationDate, LocalTime reservationTime, Service prix, Status confirmee) {
    }

    public Reservation22(Integer idReservation, int i, String nomClient, String prenomClient, Integer idService, String categorie, LocalDate reservationDate, LocalTime reservationTime, Integer prix, String confirmee) {
    }

    public Reservation22(Integer idReservation, int i, String nomClient, String prenomClient, int i1, String maquillage, LocalDate reservationDate, LocalTime reservationTime, int i2, String confirmee) {
    }


    // Enum pour les Status avec les valeurs spécifiées
    public enum Status {
        EN_ATTENTE("EN_ATTENTE"),
        CONFIRMEE("CONFIRMEE"),
        ANNULEE("ANNULEE");

        private final String label;

        Status(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    // Constructeur par défaut
    public Reservation22() {}

    // Constructeur avec tous les attributs
    public Reservation22(Integer idReservation, Integer id, String usernameclient, Integer idService, String categorie, LocalDate reservation_date, LocalTime reservation_time, Integer montantTotal, Status status) {
        this.idReservation = idReservation;
        this.id = id;
        this.usernameclient = usernameclient;
        //this.prenomclient = prenomclient;
        this.idService = idService;
        this.categorie = categorie;
        this.reservation_date = reservation_date;
        this.reservation_time = reservation_time;
        this.montantTotal = montantTotal;
        this.status = status;
    }

    // Méthode pour récupérer le prix depuis la table service (à implémenter)
    public int setPrixFromService(Service service) {
        this.montantTotal = service.getPrix();
        return montantTotal;
    }

    // Méthode pour récupérer le prix depuis la table service (à implémenter)
    public int setIdServiceFromService(Service service) {
        this.idService = service.getIdService();
        return idService;
    }

    // Méthode pour récupérer le prix depuis la table service (à implémenter)
    public String setcategorieFromService(Service service) {
        this.categorie = String.valueOf(service.getCategorie());
        return categorie;
    }

    public Integer getPrix() {return montantTotal;}
    public void setPrix(Integer prix) {this.montantTotal = prix;}

    public Integer getIdReservation() {return idReservation;}
    public void setIdReservation(Integer idReservation) {this.idReservation = idReservation;}

    public String getUsernameclient() {return usernameclient;}
    public void setUsernameclient(String usernameclient) {this.usernameclient = usernameclient;}

    /*public String getPrenomclient() {
        return prenomclient;
    }*/

    /*public void setPrenomclient(String prenomclient) {
        this.prenomclient = prenomclient;
    }*/

    public Integer getidService() {return idService;}
    public void setidService(Integer idService) {this.idService = idService;}

    public LocalDate getReservationDate() {return reservation_date;}
    public void setReservationDate(LocalDate reservationDate) {this.reservation_date = reservationDate;}

    public LocalTime getReservationTime() {return reservation_time;}
    public void setReservationTime(LocalTime reservationTime) {this.reservation_time = reservationTime;}

    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Integer montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Reservation22{" +
                "idReservation=" + idReservation +
                ", id=" + id +
                ", usernameclient='" + usernameclient + '\'' +
                ", idService=" + idService +
                ", categorie='" + categorie + '\'' +
                ", reservation_date=" + reservation_date +
                ", reservation_time=" + reservation_time +
                ", montantTotal=" + montantTotal +
                ", status=" + status +
                '}';
    }
}
