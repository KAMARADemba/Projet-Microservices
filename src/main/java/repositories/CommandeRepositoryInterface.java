package repositories;

import domain.Commande;
import java.io.Closeable;
import java.util.List;

/**
 * Interface définissant les opérations CRUD sur les commandes
 */
public interface CommandeRepositoryInterface extends Closeable {

    /**
     * Retourne toutes les commandes, ou filtrées par abonné
     * @param abonneId -1 pour toutes les commandes, sinon l'id de l'abonné
     */
    List<Commande> getAllCommandes(int abonneId);

    /**
     * Retourne une commande par son id
     */
    Commande getCommandeById(int id);

    /**
     * Crée une commande et retourne son id généré
     */
    int createCommande(Commande commande);

    /**
     * Met à jour l'adresse et la date de livraison d'une commande
     * @return true si la mise à jour a réussi
     */
    boolean updateCommande(int id, String adresseLivraison, String dateLivraison);

    /**
     * Supprime une commande par son id
     * @return true si la suppression a réussi
     */
    boolean deleteCommande(int id);
}