package infrastructure;

import domain.Commande;
import domain.LigneCommande;
import repositories.CommandeRepositoryInterface;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant d'accéder aux commandes stockées dans une base de données MariaDB
 */
public class CommandeRepositoryMariadb implements CommandeRepositoryInterface, Closeable {

    /**
     * Connexion à la base de données
     */
    protected Connection dbConnection;

    /**
     * Constructeur initialisant la connexion à la base de données
     * @param infoConnection URL de connexion JDBC
     * @param user identifiant de connexion
     * @param pwd mot de passe
     */
    public CommandeRepositoryMariadb(String infoConnection, String user, String pwd)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    @Override
    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Construit un objet Commande complet depuis un ResultSet (sans les lignes)
     */
    private Commande buildCommande(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int abonneId = rs.getInt("abonne_id");
        String dateCommande = rs.getString("date_commande");
        String adresseLivraison = rs.getString("adresse_livraison");
        String dateLivraison = rs.getString("date_livraison");
        double prixTotal = rs.getDouble("prix_total");
        return new Commande(id, abonneId, dateCommande, adresseLivraison, dateLivraison, new ArrayList<>(), prixTotal);
    }

    /**
     * Charge les lignes d'une commande depuis la table LigneCommande
     */
    private List<LigneCommande> getLignesForCommande(int commandeId) throws SQLException {
        List<LigneCommande> lignes = new ArrayList<>();
        String query = "SELECT * FROM LigneCommande WHERE commande_id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, commandeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int menuId = rs.getInt("menu_id");
                String menuNom = rs.getString("menu_nom");
                int quantite = rs.getInt("quantite");
                double prixUnitaire = rs.getDouble("prix_unitaire");
                double prixLigne = rs.getDouble("prix_ligne");

                LigneCommande ligne = new LigneCommande(menuId, menuNom, quantite, prixUnitaire);
                ligne.setPrixLigne(prixLigne);
                lignes.add(ligne);
            }
        }
        return lignes;
    }

    @Override
    public List<Commande> getAllCommandes(int abonneId) {
        List<Commande> list = new ArrayList<>();
        String query = abonneId == -1
                ? "SELECT * FROM Commande"
                : "SELECT * FROM Commande WHERE abonne_id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            if (abonneId != -1) ps.setInt(1, abonneId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Commande c = buildCommande(rs);
                c.setLignes(getLignesForCommande(c.getId()));
                list.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Commande getCommandeById(int id) {
        String query = "SELECT * FROM Commande WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Commande c = buildCommande(rs);
                c.setLignes(getLignesForCommande(id));
                return c;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int createCommande(Commande commande) {
        String queryCommande = "INSERT INTO Commande (abonne_id, date_commande, adresse_livraison, date_livraison, prix_total) VALUES (?, ?, ?, ?, ?)";
        String queryLigne = "INSERT INTO LigneCommande (commande_id, menu_id, menu_nom, quantite, prix_unitaire, prix_ligne) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            // Désactiver l'autocommit pour faire les 2 inserts en une transaction
            dbConnection.setAutoCommit(false);

            int newId;

            try (PreparedStatement ps = dbConnection.prepareStatement(queryCommande, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, commande.getAbonneId());
                ps.setString(2, commande.getDateCommande());
                ps.setString(3, commande.getAdresseLivraison());
                ps.setString(4, commande.getDateLivraison());
                ps.setDouble(5, commande.getPrixTotal());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) {
                    dbConnection.rollback();
                    return -1;
                }
                newId = keys.getInt(1);
            }

            // Insérer chaque ligne
            try (PreparedStatement ps = dbConnection.prepareStatement(queryLigne)) {
                for (LigneCommande ligne : commande.getLignes()) {
                    ps.setInt(1, newId);
                    ps.setInt(2, ligne.getMenuId());
                    ps.setString(3, ligne.getMenuNom());
                    ps.setInt(4, ligne.getQuantite());
                    ps.setDouble(5, ligne.getPrixUnitaire());
                    ps.setDouble(6, ligne.getPrixLigne());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            dbConnection.commit();
            return newId;

        } catch (SQLException e) {
            try { dbConnection.rollback(); } catch (SQLException ex) { System.err.println(ex.getMessage()); }
            throw new RuntimeException(e);
        } finally {
            try { dbConnection.setAutoCommit(true); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    @Override
    public boolean updateCommande(int id, String adresseLivraison, String dateLivraison) {
        String query = "UPDATE Commande SET adresse_livraison=?, date_livraison=? WHERE id=?";
        int nbRowModified = 0;

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, adresseLivraison);
            ps.setString(2, dateLivraison);
            ps.setInt(3, id);
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nbRowModified != 0;
    }

    @Override
    public boolean deleteCommande(int id) {
        // Les LigneCommande sont supprimées en cascade (ON DELETE CASCADE)
        String query = "DELETE FROM Commande WHERE id=?";
        int nbRowModified = 0;

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nbRowModified != 0;
    }
}