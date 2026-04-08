package api;

import domain.Commande;
import infrastructure.MenuServiceClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import repositories.CommandeRepositoryInterface;
import usecases.CommandeService;

/**
 * Ressource JAX-RS exposant l'API REST des commandes
 * URL de base : /api/commandes
 */
@Path("/commandes")
@ApplicationScoped
public class CommandeRessource {

    private CommandeService service;

    public CommandeRessource() {}

    /**
     * Injection CDI du repository et du client Menus
     */
    @Inject
    public CommandeRessource(CommandeRepositoryInterface commandeRepo,
                            MenuServiceClient menuClient) {
        this.service = new CommandeService(commandeRepo, menuClient);
    }

    /**
     * GET /api/commandes?abonneId=X
     */
    @GET
    @Produces("application/json")
    public String getAllCommandes(@QueryParam("abonneId") @DefaultValue("-1") int abonneId) {
        return service.getAllCommandesJSON(abonneId);
    }

    /**
     * GET /api/commandes/{id}
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getCommande(@PathParam("id") int id) {
        String result = service.getCommandeJSON(id);
        if (result == null) throw new NotFoundException();
        return result;
    }

    /**
     * POST /api/commandes
     * Le body contient : abonneId, adresseLivraison, dateLivraison, lignes (menuId + quantite)
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createCommande(Commande commande) {
        if (commande.getLignes() == null || commande.getLignes().isEmpty())
            throw new BadRequestException("Au moins une ligne est requise");

        String result = service.createCommandeJSON(commande);

        if (result == null)
            throw new NotFoundException("Un ou plusieurs menus sont introuvables");

        return Response.status(Response.Status.CREATED)
                .entity(result)
                .header("Location", "/commandes/" + commande.getId())
                .build();
    }

    /**
     * PUT /api/commandes/{id}
     * Seuls adresseLivraison et dateLivraison sont modifiables
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateCommande(@PathParam("id") int id, Commande commande) {
        if (!service.updateCommande(id, commande.getAdresseLivraison(), commande.getDateLivraison()))
            throw new NotFoundException();

        return Response.ok("updated").build();
    }

    /**
     * DELETE /api/commandes/{id}
     */
    @DELETE
    @Path("{id}")
    public Response deleteCommande(@PathParam("id") int id) {
        if (!service.deleteCommande(id))
            throw new NotFoundException();

        return Response.noContent().build();
    }
}