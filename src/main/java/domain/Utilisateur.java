package domain;

/**
 * Entité métier représentant un abonné de l'application
 */
public class Utilisateur {

    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String adresse;

    public Utilisateur() {}

    public Utilisateur(int id, String nom, String prenom, String email, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public String toString() {
        return "Utilisateur{id=" + id + ", nom='" + nom + "', prenom='" + prenom + "', email='" + email + "'}";
    }
}
