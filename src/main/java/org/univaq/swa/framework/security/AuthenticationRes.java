package org.univaq.swa.framework.security;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author didattica
 */
@Path("auth")
public class AuthenticationRes {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@Context UriInfo uriinfo,
            //un altro modo per ricevere e iniettare i parametri con JAX-RS...
            @FormParam("username") String username,
            @FormParam("password") String password) {
        try {

            if (AuthHelpers.getInstance().authenticateUser(username, password)) {
                String authToken = AuthHelpers.getInstance().issueToken(uriinfo, username);
                //Restituiamolo in tutte le modalità, giusto per fare un esempio...
                return Response.ok(authToken)
                        .cookie(new NewCookie("token", authToken))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
            }
        } catch (Exception e) {
            //logging dell'errore 
        }
        return Response.status(UNAUTHORIZED).build();
    }

    @DELETE
    @Path("logout")
    @Logged
    public Response logout(@Context ContainerRequestContext req) {
        //proprietà estratta dall'authorization header 
        //e iniettata nella request dal filtro di autenticazione
        String token = (String) req.getProperty("token");
        AuthHelpers.getInstance().revokeToken(token);
        return Response.noContent()
                //eliminaimo anche il cookie con il token
                .cookie(new NewCookie(new Cookie("token", ""), "", 0, false))
                .build();
    }

    //Metodo per fare "refresh" del token senza ritrasmettere le credenziali
    @GET
    @Path("refresh")
    @Logged
    public Response refresh(@Context ContainerRequestContext req, @Context UriInfo uriinfo) {
        //proprietà iniettata nella request dal filtro di autenticazione
        String username = (String) req.getProperty("user");
        String newtoken = AuthHelpers.getInstance().issueToken(uriinfo, username);
        return Response.ok(newtoken)
                .cookie(new NewCookie("token", newtoken))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newtoken).build();

    }
}
