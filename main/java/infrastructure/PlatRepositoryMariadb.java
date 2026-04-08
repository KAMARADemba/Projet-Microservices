package infrastructure;

import domain.Plat;
import repositories.PlatRepositoryInterface;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;

/**
 * Classe permettant d'accéder aux plats stockés dans une base de données MariaDB
 */
public class PlatRepositoryMariadb implements PlatRepositoryInterface, Closeable {

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
    public PlatRepositoryMariadb(String infoConnection, String user, String pwd)
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

    @Override
    public Plat getPlat(int id) {
        Plat selectedPlat = null;
        String query = "SELECT * FROM Plat WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                String nom = result.getString("nom");
                String description = result.getString("description");
                double prix = result.getDouble("prix");

                selectedPlat = new Plat(id, nom, description, prix);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedPlat;
    }

    @Override
    public ArrayList<Plat> getAllPlats() {
        ArrayList<Plat> listPlats;
        String query = "SELECT * FROM Plat";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();
            listPlats = new ArrayList<>();

            while (result.next()) {
                int id = result.getInt("id");
                String nom = result.getString("nom");
                String description = result.getString("description");
                double prix = result.getDouble("prix");

                listPlats.add(new Plat(id, nom, description, prix));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPlats;
    }

    @Override
    public boolean createPlat(String nom, String description, double prix) {
        String query = "INSERT INTO Plat (nom, description, prix) VALUES (?, ?, ?)";
        int nbRowModified = 0;

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, nom);
            ps.setString(2, description);
            ps.setDouble(3, prix);

            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nbRowModified != 0;
    }

    @Override
    public boolean updatePlat(int id, String nom, String description, double prix) {
        String query = "UPDATE Plat SET nom=?, description=?, prix=? WHERE id=?";
        int nbRowModified = 0;

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, nom);
            ps.setString(2, description);
            ps.setDouble(3, prix);
            ps.setInt(4, id);

            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nbRowModified != 0;
    }

    @Override
    public boolean deletePlat(int id) {
        String query = "DELETE FROM Plat WHERE id=?";
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
