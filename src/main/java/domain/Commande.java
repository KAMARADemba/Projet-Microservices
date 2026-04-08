package domain;

import java.util.List;

/**
 * Entité métier représentant une commande passée par un abonné
 */
public class Commande {

    protected int id;
    protected int abonneId;
    protected String dateCommande;   // ISO-8601 : "2025-03-20T14:32:00"
    protected String adresseLivraison;
    protected String dateLivraison;  // ISO-8601 : "2025-04-01"
    protected List<LigneCommande> lignes;
    protected double prixTotal;

    public Commande() {}

    public Commande(int id, int abonneId, String dateCommande,
                    String adresseLivraison, String dateLivraison,
                    List<LigneCommande> lignes, double prixTotal) {
        this.id = id;
        this.abonneId = abonneId;
        this.dateCommande = dateCommande;
        this.adresseLivraison = adresseLivraison;
        this.dateLivraison = dateLivraison;
        this.lignes = lignes;
        this.prixTotal = prixTotal;
    }

    public int getId() { return id; }
    public int getAbonneId() { return abonneId; }
    public String getDateCommande() { return dateCommande; }
    public String getAdresseLivraison() { return adresseLivraison; }
    public String getDateLivraison() { return dateLivraison; }
    public List<LigneCommande> getLignes() { return lignes; }
    public double getPrixTotal() { return prixTotal; }

    public void setId(int id) { this.id = id; }
    public void setAbonneId(int abonneId) { this.abonneId = abonneId; }
    public void setDateCommande(String dateCommande) { this.dateCommande = dateCommande; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }
    public void setDateLivraison(String dateLivraison) { this.dateLivraison = dateLivraison; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    @Override
    public String toString() {
        return "Commande{id=" + id + ", abonneId=" + abonneId +
                ", dateCommande='" + dateCommande + "', adresseLivraison='" + adresseLivraison +
                "', dateLivraison='" + dateLivraison + "', prixTotal=" + prixTotal + '}';
    }
}