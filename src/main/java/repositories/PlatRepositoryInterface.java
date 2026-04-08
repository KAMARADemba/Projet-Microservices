package repositories;

import domain.Plat;
import java.util.ArrayList;

/**
 * Interface définissant les opérations d'accès aux données des plats
 */
public interface PlatRepositoryInterface {

    /**
     * Ferme la connexion au dépôt
     */
    void close();

    /**
     * Récupère un plat par son identifiant
     * @param id identifiant du plat
     * @return un objet Plat ou null si non trouvé
     */
    Plat getPlat(int id);

    /**
     * Récupère tous les plats
     * @return liste de tous les plats
     */
    ArrayList<Plat> getAllPlats();

    /**
     * Crée un nouveau plat
     * @param nom nom du plat
     * @param description description du plat
     * @param prix prix du plat
     * @return true si la création a réussi, false sinon
     */
    boolean createPlat(String nom, String description, double prix);

    /**
     * Met à jour un plat existant
     * @param id identifiant du plat à modifier
     * @param nom nouveau nom
     * @param description nouvelle description
     * @param prix nouveau prix
     * @return true si la mise à jour a réussi, false sinon
     */
    boolean updatePlat(int id, String nom, String description, double prix);

    /**
     * Supprime un plat
     * @param id identifiant du plat à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean deletePlat(int id);
}
