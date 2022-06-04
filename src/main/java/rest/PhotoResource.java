package rest;

import businessfacades.PhotoDTOFacade;
import businessfacades.TagDTOFacade;
import datafacades.IDataExtra;
import dtos.TagDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datafacades.IDataFacade;
import datafacades.UserFacade;
import dtos.PhotoDTO;
import dtos.UserDTO;
import entities.User;
import errorhandling.EntityNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import utils.EMF_Creator;

@Path("photo")
public class PhotoResource {
       
    private static final IDataFacade<PhotoDTO> PHOTO_FACADE  =  PhotoDTOFacade.getFacade(EMF_Creator.createEntityManagerFactory());
    private static final IDataExtra<PhotoDTO> EXTRA_FACADE  =  PhotoDTOFacade.getExtraFacade(EMF_Creator.createEntityManagerFactory());
    private static final IDataFacade<TagDTO> TAG_FACADE =  TagDTOFacade.getFacade();
    private static final IDataFacade<UserDTO> USER_FACADE =  UserFacade.getFacade(EMF_Creator.createEntityManagerFactory());

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(PHOTO_FACADE.getAll())).build();
    }

    @GET
    @Path("paginated")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllPaginated(@QueryParam("index") int index,
                                    @QueryParam("number") int number) {
        return Response.ok().entity(GSON.toJson(EXTRA_FACADE.getAllPaginated(index, number))).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") String id) throws EntityNotFoundException {
        PhotoDTO p = PHOTO_FACADE .getById(id);
        return Response.ok().entity(GSON.toJson(p)).build();
    }

    @GET
    @Path("/allTags")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTags() {
        List<TagDTO> tagDtos = TAG_FACADE.getAll();
        return Response.ok().entity(GSON.toJson(tagDtos)).build();
    }
    
    @GET
    @Path("/property/{propname}/{propvalue}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getByProperty(@PathParam("propname") String propName, @PathParam("propvalue") String propValue) throws EntityNotFoundException {
//        System.out.println("GET BY PROPERTY: "+propName+": "+propValue);
//        if(propName == "location" && propValue == "all")
//            return Response.ok().entity(GSON.toJson(PHOTO_FACADE.getAll())).build();
        propValue = propValue.replace("-", "/");
        List<PhotoDTO> photos = PHOTO_FACADE .findByProperty(propName, propValue);
        return Response.ok().entity(GSON.toJson(photos)).build();
    }
    


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response create(String content) throws Exception {
        PhotoDTO pdto = GSON.fromJson(content, PhotoDTO.class);
        PhotoDTO newPdto = PHOTO_FACADE .create(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
    }
    
    @POST
    @Path("user")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response createUser(String content) throws Exception {
        UserDTO user = GSON.fromJson(content, UserDTO.class);
        user = USER_FACADE.create(user);
        user.setPassword(null);
        return Response.ok().entity(GSON.toJson(user)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response update(@PathParam("id") String id, String content) throws EntityNotFoundException {
        PhotoDTO pdto = GSON.fromJson(content, PhotoDTO.class);
        pdto.setName(id);
        PhotoDTO updated = PHOTO_FACADE .update(pdto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") String id) throws EntityNotFoundException {
        PhotoDTO deleted = PHOTO_FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }
    
    
}
