package infrastructure;

import domain.LigneCommande;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Client HTTP pour consommer l'API Menus (port 3004)
 * Récupère le nom et le prix d'un menu pour construire un snapshot LigneCommande
 */

public class MenuServiceClient {

    private static final String MENUS_API_URL = "http://localhost:3004/menus/";

    /**
     * Appelle l'API Menus et enrichit la LigneCommande avec le nom et le prix
     * @param menuId identifiant du menu
     * @param quantite quantité commandée
     * @return LigneCommande complète, ou null si le menu est introuvable
     */
    public LigneCommande fetchLigneCommande(int menuId, int quantite) {
        try {
            URL url = new URL(MENUS_API_URL + menuId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return null; // menu introuvable
            }

            try (InputStream is = conn.getInputStream();
                 JsonReader reader = Json.createReader(is)) {

                JsonObject json = reader.readObject();
                String nom = json.getString("nom");
                double prix = json.getJsonNumber("prix").doubleValue();

                return new LigneCommande(menuId, nom, quantite, prix);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}