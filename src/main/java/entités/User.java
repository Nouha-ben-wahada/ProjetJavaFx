package entités;

import java.time.LocalDateTime;

public class User {
    public static Object Role;
    private int id;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String adresse;
    private String userName;
    private String password;
    private LocalDateTime dateCreationCompte = LocalDateTime.now();
    private Role role;

    public User(String text, String txtprenomText, String txttelText, String txtemailText, String txtadresseText, String txtuserText, String txtpasswordText, String value) {
        // Constructeur vide
    }
    public User (){
        dateCreationCompte = LocalDateTime.now();
    }
    public User(String nom, String prenom, String tel, String email, String adresse, String userName, String password, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.adresse = adresse;
        this.userName = userName;
        this.password = password;
        this.dateCreationCompte = LocalDateTime.now();
        this.role = role;
    }

    // Getters et setters pour chaque champ
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getDateCreationCompte() {
        return dateCreationCompte;
    }

    public void setDateCreationCompte(LocalDateTime dateCreationCompte) {
        this.dateCreationCompte = dateCreationCompte;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", dateCreationCompte=" + dateCreationCompte +
                ", role='" + role + '\'' +
                '}';
    }
}
