package entités;

public class Service {

    // Attributs de la classe
    private int idService;            // Identifiant unique
    private String nomService;        // Nom du service
    private Categorie categorie;      // Catégorie du service
    private int duration;             // Durée du service (en minutes)
    private int prix;                 // Prix du service
    private String description;       // Description détaillée du service


    // Enum pour les catégories avec les valeurs spécifiées
    public enum Categorie {
        COIFFURE("COIFFURE"),
        MAQUILLAGE("MAQUILLAGE"),
        SOINS_DE_VISAGE("SOINS_DE_VISAGE"),
        MASSAGES("MASSAGES"),
        POSE_DE_VERNIS("POSE_DE_VERNIS");

        private final String label;

        Categorie(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // Constructeur par défaut
    public Service() {
    }

    // Constructeur avec tous les champs
    public Service(int idService, String nomService, Categorie categorie, int duration, int prix, String description) {
        this.idService = idService;
        this.nomService = nomService;
        this.categorie = categorie;
        this.duration = duration;
        this.prix = prix;
        this.description = description;
    }


    // Getters et Setters
    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Méthode toString() pour afficher les informations du service
    @Override
    public String toString() {
        return "Service{" +
                "idService=" + idService +
                ", nomService='" + nomService + '\'' +
                ", categorie=" + categorie +
                ", duration=" + duration +
                " prix=" + prix +
                ", description='" + description + '\'' +
                '}';
    }

}
