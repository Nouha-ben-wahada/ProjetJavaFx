package entités;

public class Client {
    private int idClient;
    private String nomClient;
    private String prenomClient;
    private String emailClient;
    private String adresse;
    private String tel;
    private String userName;
    private String password;
    private String dateCreationCompte;

    public Client() {
    }

    public Client(int idClient, String nomClient, String prenomClient, String emailClient, String adresse, String tel, String userName, String password, String dateCreationCompte) {
        this.idClient = idClient;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.emailClient = emailClient;
        this.adresse = adresse;
        this.tel = tel;
        this.userName = userName;
        this.password = password;
        this.dateCreationCompte = dateCreationCompte;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getDateCreationCompte() {
        return dateCreationCompte;
    }

    public void setDateCreationCompte(String dateCreationCompte) {
        this.dateCreationCompte = dateCreationCompte;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + idClient +
                ", nomClient='" + nomClient + '\'' +
                ", prenomClient='" + prenomClient + '\'' +
                ", emailClient='" + emailClient + '\'' +
                ", adresse='" + adresse + '\'' +
                ", tel='" + tel + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", dateCreationCompte='" + dateCreationCompte + '\'' +
                '}';
    }
}
