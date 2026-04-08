package api;

import domain.Utilisateur;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import repositories.UtilisateurRepositoryInterface;
import usecases.UtilisateurService;

/**
 * Ressource associée aux utilisateurs
 * (point d'accès de l'API REST)
 */
@Path("/utilisateurs")
@ApplicationScoped
public class UtilisateurResource {

    private UtilisateurService service;

    public UtilisateurResource() {}

    @Inject
    public UtilisateurResource(UtilisateurRepositoryInterface utilisateurRepo) {
        this.service = new UtilisateurService(utilisateurRepo);
    }

    /**
     * GET /api/utilisateurs
     * @return liste de tous les utilisateurs au format JSON
     */
    @GET
    @Produces("application/json")
    public String getAllUtilisateurs() {
        return service.getAllUtilisateursJSON();
    }

    /**
     * GET /api/utilisateurs/{id}
     * @param id identifiant de l'utilisateur
     * @return les informations de l'utilisateur au format JSON
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getUtilisateur(@PathParam("id") int id) {
        String result = service.getUtilisateurJSON(id);

        if (result == null)
            throw new NotFoundException();

        return result;
    }

    /**
     * POST /api/utilisateurs
     * @param utilisateur l'utilisateur à créer en JSON
     * @return réponse 201 si créé
     */
    @POST
    @Consumes("application/json")
    public Response createUtilisateur(Utilisateur utilisateur) {
        if (!service.createUtilisateur(utilisateur))
            throw new BadRequestException();

        return Response.status(Response.Status.CREATED).entity("created").build();
    }

    /**
     * PUT /api/utilisateurs/{id}
     * @param id identifiant de l'utilisateur à modifier
     * @param utilisateur les nouvelles informations en JSON
     * @return réponse 200 si mis à jour
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updateUtilisateur(@PathParam("id") int id, Utilisateur utilisateur) {
        if (!service.updateUtilisateur(id, utilisateur))
            throw new NotFoundException();

        return Response.ok("updated").build();
    }

    /**
     * DELETE /api/utilisateurs/{id}
     * @param id identifiant de l'utilisateur à supprimer
     * @return réponse 204 si supprimé
     */
    @DELETE
    @Path("{id}")
    public Response deleteUtilisateur(@PathParam("id") int id) {
        if (!service.deleteUtilisateur(id))
            throw new NotFoundException();

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
