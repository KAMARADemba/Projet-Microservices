package usecases;

import domain.Plat;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import repositories.PlatRepositoryInterface;
import java.util.ArrayList;

/**
 * Classe implémentant les cas d'utilisation liés aux plats
 */
public class PlatService {

    /**
     * Dépôt des plats
     */
    protected PlatRepositoryInterface platRepo;

    /**
     * Constructeur permettant d'injecter l'accès aux données
     * @param platRepo objet implémentant l'interface d'accès aux données des plats
     */
    public PlatService(PlatRepositoryInterface platRepo) {
        this.platRepo = platRepo;
    }

    /**
     * Retourne tous les plats au format JSON
     * @return chaîne JSON avec la liste des plats
     */
    public String getAllPlatsJSON() {
        ArrayList<Plat> allPlats = platRepo.getAllPlats();

        String result = null;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            result = jsonb.toJson(allPlats);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    /**
     * Retourne un plat au format JSON
     * @param id identifiant du plat recherché
     * @return chaîne JSON avec les informations du plat
     */
    public String getPlatJSON(int id) {
        String result = null;
        Plat myPlat = platRepo.getPlat(id);

        if (myPlat != null) {
            try (Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(myPlat);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * Crée un nouveau plat
     * @param plat objet Plat à créer
     * @return true si la création a réussi, false sinon
     */
    public boolean createPlat(Plat plat) {
        return platRepo.createPlat(plat.getNom(), plat.getDescription(), plat.getPrix());
    }

    /**
     * Met à jour un plat existant
     * @param id identifiant du plat à modifier
     * @param plat objet Plat avec les nouvelles informations
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updatePlat(int id, Plat plat) {
        return platRepo.updatePlat(id, plat.getNom(), plat.getDescription(), plat.getPrix());
    }

    /**
     * Supprime un plat
     * @param id identifiant du plat à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deletePlat(int id) {
        return platRepo.deletePlat(id);
    }
}
