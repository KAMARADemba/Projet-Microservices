package usecases;

import domain.Utilisateur;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import repositories.UtilisateurRepositoryInterface;
import java.util.ArrayList;

/**
 * Classe implémentant les cas d'utilisation liés aux utilisateurs
 */
public class UtilisateurService {

    protected UtilisateurRepositoryInterface utilisateurRepo;

    public UtilisateurService(UtilisateurRepositoryInterface utilisateurRepo) {
        this.utilisateurRepo = utilisateurRepo;
    }

    /**
     * Retourne tous les utilisateurs au format JSON
     * @return chaîne JSON avec la liste des utilisateurs
     */
    public String getAllUtilisateursJSON() {
        ArrayList<Utilisateur> allUtilisateurs = utilisateurRepo.getAllUtilisateurs();

        String result = null;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            result = jsonb.toJson(allUtilisateurs);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    /**
     * Retourne un utilisateur au format JSON
     * @param id identifiant de l'utilisateur
     * @return chaîne JSON avec les informations de l'utilisateur
     */
    public String getUtilisateurJSON(int id) {
        String result = null;
        Utilisateur myUtilisateur = utilisateurRepo.getUtilisateur(id);

        if (myUtilisateur != null) {
            try (Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(myUtilisateur);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * Crée un nouvel utilisateur
     * @param utilisateur objet Utilisateur à créer
     * @return true si la création a réussi
     */
    public boolean createUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepo.createUtilisateur(
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getAdresse()
        );
    }

    /**
     * Met à jour un utilisateur
     * @param id identifiant de l'utilisateur
     * @param utilisateur objet avec les nouvelles informations
     * @return true si la mise à jour a réussi
     */
    public boolean updateUtilisateur(int id, Utilisateur utilisateur) {
        return utilisateurRepo.updateUtilisateur(
                id,
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getAdresse()
        );
    }

    /**
     * Supprime un utilisateur
     * @param id identifiant de l'utilisateur
     * @return true si la suppression a réussi
     */
    public boolean deleteUtilisateur(int id) {
        return utilisateurRepo.deleteUtilisateur(id);
    }
}
