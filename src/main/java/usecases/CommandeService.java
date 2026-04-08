package usecases;

import domain.Commande;
import domain.LigneCommande;
import infrastructure.MenuServiceClient;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import repositories.CommandeRepositoryInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service gérant la logique métier des commandes
 */
public class CommandeService {

    private final CommandeRepositoryInterface repo;
    private final MenuServiceClient menuClient;

    public CommandeService(CommandeRepositoryInterface repo, MenuServiceClient menuClient) {
        this.repo = repo;
        this.menuClient = menuClient;
    }

    /**
     * Retourne toutes les commandes (filtrées ou non) au format JSON
     */
    public String getAllCommandesJSON(int abonneId) {
        List<Commande> commandes = repo.getAllCommandes(abonneId);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(commandes);
        } catch (Exception e) {
            return "[]";
        }
    }

    /**
     * Retourne une commande par son id au format JSON, null si introuvable
     */
    public String getCommandeJSON(int id) {
        Commande c = repo.getCommandeById(id);
        if (c == null) return null;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(c);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Crée une nouvelle commande :
     * - appelle l'API Menus pour chaque ligne (snapshot nom + prix)
     * - calcule prixLigne et prixTotal
     * - enregistre la dateCommande côté serveur
     * @return la commande créée au format JSON, null si un menu est introuvable
     */
    public String createCommandeJSON(Commande input) {
        List<LigneCommande> lignesEnrichies = new ArrayList<>();
        double prixTotal = 0;

        for (LigneCommande ligne : input.getLignes()) {
            LigneCommande enrichie = menuClient.fetchLigneCommande(ligne.getMenuId(), ligne.getQuantite());
            if (enrichie == null) return null; // menu introuvable → 404
            prixTotal += enrichie.getPrixLigne();
            lignesEnrichies.add(enrichie);
        }

        // Horodatage côté serveur
        String dateCommande = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        input.setDateCommande(dateCommande);
        input.setLignes(lignesEnrichies);
        input.setPrixTotal(prixTotal);

        int newId = repo.createCommande(input);
        if (newId < 0) return null;

        input.setId(newId);

        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(input);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Met à jour l'adresse et la date de livraison d'une commande
     */
    public boolean updateCommande(int id, String adresse, String dateLivraison) {
        return repo.updateCommande(id, adresse, dateLivraison);
    }

    /**
     * Supprime une commande
     */
    public boolean deleteCommande(int id) {
        return repo.deleteCommande(id);
    }
}