package entités;

public class Categorie {
    private int codeCategorie;
    private String nomCategorie;
    private String famille;

    public Categorie(int codeCategorie, String nomCategorie, String famille) {
        this.codeCategorie = codeCategorie;
        this.nomCategorie = nomCategorie;
        this.famille = famille;
    }

    public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public int getCodeCategorie() {
        return codeCategorie;
    }

    public void setCodeCategorie(int codeCategorie) {
        this.codeCategorie = codeCategorie;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "codeCategorie=" + codeCategorie +
                ", nomCategorie='" + nomCategorie + '\'' +
                ", famille='" + famille + '\'' +
                '}';
    }
}