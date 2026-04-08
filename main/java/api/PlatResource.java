package api;

import domain.Plat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import repositories.PlatRepositoryInterface;
import usecases.PlatService;

/**
 * Ressource associée aux plats
 * (point d'accès de l'API REST)
 */
@Path("/plats")
@ApplicationScoped
public class PlatResource {

    /**
     * Service utilisé pour accéder aux données des plats
     */
    private PlatService service;

    /**
     * Constructeur par défaut
     */
    public PlatResource() {}

    /**
     * Constructeur permettant d'injecter le service via CDI
     * @param platRepo objet implémentant l'interface d'accès aux données
     */
    @Inject
    public PlatResource(PlatRepositoryInterface platRepo) {
        this.service = new PlatService(platRepo);
    }

    /**
     * Endpoint GET /api/plats
     * @return liste de tous les plats au format JSON
     */
    @GET
    @Produces("application/json")
    public String getAllPlats() {
        return service.getAllPlatsJSON();
    }

    /**
     * Endpoint GET /api/plats/{id}
     * @param id identifiant du plat recherché
     * @return les informations du plat au format JSON
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getPlat(@PathParam("id") int id) {
        String result = service.getPlatJSON(id);

        if (result == null)
            throw new NotFoundException();

        return result;
    }

    /**
     * Endpoint POST /api/plats
     * @param plat le plat à créer transmis en JSON
     * @return une réponse "created" si la création a réussi
     */
    @POST
    @Consumes("application/json")
    public Response createPlat(Plat plat) {
        if (!service.createPlat(plat))
            throw new BadRequestException();

        return Response.status(Response.Status.CREATED).entity("created").build();
    }

    /**
     * Endpoint PUT /api/plats/{id}
     * @param id identifiant du plat à modifier
     * @param plat le plat avec les nouvelles informations transmis en JSON
     * @return une réponse "updated" si la mise à jour a réussi
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updatePlat(@PathParam("id") int id, Plat plat) {
        if (!service.updatePlat(id, plat))
            throw new NotFoundException();

        return Response.ok("updated").build();
    }

    /**
     * Endpoint DELETE /api/plats/{id}
     * @param id identifiant du plat à supprimer
     * @return une réponse "deleted" si la suppression a réussi
     */
    @DELETE
    @Path("{id}")
    public Response deletePlat(@PathParam("id") int id) {
        if (!service.deletePlat(id))
            throw new NotFoundException();

        return Response.ok("deleted").build();
    }
}
