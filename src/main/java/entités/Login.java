package entités;

public class Login {
    private String username;
    private String password;

    // Constructeur par défaut
    public Login() {
    }

    // Constructeur avec paramètres
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters et Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Méthode toString (pour le débogage)
    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='******'" + // On masque le mot de passe pour plus de sécurité
                '}';
    }
}
