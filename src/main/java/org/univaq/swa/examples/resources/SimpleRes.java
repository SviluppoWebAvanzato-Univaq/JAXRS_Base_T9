package org.univaq.swa.examples.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import org.univaq.swa.framework.base.RESTWebApplicationException;

/**
 *
 * @author didattica
 */
@Path("simple")
public class SimpleRes {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @Context UriInfo uriinfo,
            @QueryParam("p") String parametro) throws RESTWebApplicationException {

        List<String> l = new ArrayList();
        l.add("ciao1");
        l.add("ciao2");

        return Response.ok(l).build();
    }

    @GET
    @Path("{item: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("item") int itemID) {
        if (itemID < 1000) {
            /* non presente */
            return Response.status(404).entity("item not found").build();
        } else {
            return Response.ok(itemID).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMe(
            @Context UriInfo uriinfo, String payload) {
        return Response.created(
                uriinfo.getAbsolutePathBuilder()
                        .path(this.getClass(), "getItem").build(1000))
                .build();
    }

}
