package domain;

/**
 * Entité métier représentant un plat proposé par l'entreprise
 */
public class Plat {

    protected int id;
    protected String nom;
    protected String description;
    protected double prix;

    public Plat() {}

    public Plat(int id, String nom, String description, double prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public double getPrix() { return prix; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setPrix(double prix) { this.prix = prix; }

    @Override
    public String toString() {
        return "Plat{id=" + id + ", nom='" + nom + "', description='" + description + "', prix=" + prix + '}';
    }
}