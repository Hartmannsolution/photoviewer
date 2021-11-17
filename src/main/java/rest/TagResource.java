package rest;

import businessfacades.PhotoDTOFacade;
import businessfacades.TagDTOFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datafacades.IDataFacade;
import dtos.PhotoDTO;
import dtos.TagDTO;
import errorhandling.API_Exception;
import errorhandling.EntityNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("tag")
public class TagResource {
    private static final IDataFacade<TagDTO> TAG_FACADE =  TagDTOFacade.getFacade();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(TAG_FACADE .getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") String id) throws EntityNotFoundException {
        TagDTO t = TAG_FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(t)).build();
    }

    @GET
    @Path("/allTags")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTags() {
        List<TagDTO> tagDtos = TAG_FACADE.getAll();
        return Response.ok().entity(GSON.toJson(tagDtos)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response create(String content) throws API_Exception {
        TagDTO pdto = GSON.fromJson(content, TagDTO.class);
        TagDTO newPdto = TAG_FACADE .create(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response update(@PathParam("id") String id, String content) throws EntityNotFoundException {
        TagDTO pdto = GSON.fromJson(content, TagDTO.class);
        TagDTO updated = TAG_FACADE .update(pdto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") String id) throws EntityNotFoundException {
        TagDTO deleted = TAG_FACADE .delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }
}
