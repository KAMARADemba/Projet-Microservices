package domain;

/**
 * Représente une ligne dans une commande (un menu + quantité + prix snapshot)
 */
public class LigneCommande {

    protected int menuId;
    protected String menuNom;
    protected int quantite;
    protected double prixUnitaire;
    protected double prixLigne;

    public LigneCommande() {}

    public LigneCommande(int menuId, String menuNom, int quantite, double prixUnitaire) {
        this.menuId = menuId;
        this.menuNom = menuNom;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.prixLigne = prixUnitaire * quantite;
    }

    public int getMenuId() { return menuId; }
    public String getMenuNom() { return menuNom; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public double getPrixLigne() { return prixLigne; }

    public void setMenuId(int menuId) { this.menuId = menuId; }
    public void setMenuNom(String menuNom) { this.menuNom = menuNom; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setPrixLigne(double prixLigne) { this.prixLigne = prixLigne; }

    @Override
    public String toString() {
        return "LigneCommande{menuId=" + menuId + ", menuNom='" + menuNom +
                "', quantite=" + quantite + ", prixUnitaire=" + prixUnitaire +
                ", prixLigne=" + prixLigne + '}';
    }
}