package repositories;

import domain.Utilisateur;
import java.util.ArrayList;

/**
 * Interface définissant les opérations d'accès aux données des utilisateurs
 */
public interface UtilisateurRepositoryInterface {

    void close();

    /**
     * Récupère un utilisateur par son identifiant
     * @param id identifiant de l'utilisateur
     * @return un objet Utilisateur ou null si non trouvé
     */
    Utilisateur getUtilisateur(int id);

    /**
     * Récupère tous les utilisateurs
     * @return liste de tous les utilisateurs
     */
    ArrayList<Utilisateur> getAllUtilisateurs();

    /**
     * Crée un nouvel utilisateur
     * @param nom nom de l'utilisateur
     * @param prenom prénom de l'utilisateur
     * @param email adresse email
     * @param adresse adresse postale
     * @return true si la création a réussi
     */
    boolean createUtilisateur(String nom, String prenom, String email, String adresse);

    /**
     * Met à jour un utilisateur
     * @param id identifiant de l'utilisateur
     * @param nom nouveau nom
     * @param prenom nouveau prénom
     * @param email nouvel email
     * @param adresse nouvelle adresse
     * @return true si la mise à jour a réussi
     */
    boolean updateUtilisateur(int id, String nom, String prenom, String email, String adresse);

    /**
     * Supprime un utilisateur
     * @param id identifiant de l'utilisateur
     * @return true si la suppression a réussi
     */
    boolean deleteUtilisateur(int id);
}
