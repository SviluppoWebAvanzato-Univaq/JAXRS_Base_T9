package org.univaq.swa.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
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
public class AutenticazioneRes {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@Context UriInfo uriinfo,
            //un altro modo per ricevere e iniettare i parametri con JAX-RS...
            @FormParam("username") String username,
            @FormParam("password") String password) {
        try {
            if (authenticateUser(username, password)) {
                String authToken = issueToken(uriinfo, username);
                //return Response.ok(authToken).build();
                //return Response.ok().cookie(new NewCookie("token", authToken)).build();
                //return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
                //Restituiamolo in tutte le modalità, giusto per fare un esempio..
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
    @AuthLevel1
    public Response logout(@Context ContainerRequestContext req) {
        String token = (String) req.getProperty("token");
        revokeToken(token);
        return Response.noContent().build();
    }

    private boolean authenticateUser(String username, String password) {
        return true;
    }

    private String issueToken(UriInfo context, String username) {
        String token = username + "skfjsdkj";

//        JWT        
//        Key key = JWTHelpers.getInstance().getJwtKey();
//        String token = Jwts.builder()
//                .setSubject(username)
//                .setIssuer(context.getAbsolutePath().toString())
//                .setIssuedAt(new Date())
//                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(15L).atZone(ZoneId.systemDefault()).toInstant()))
//                .signWith(key)
//                .compact();
        return token;
    }

    private void revokeToken(String token) {
        /* invalidate il token */
    }

/////////////////    
    //Metodo per fare "refresh" del token JWT senza ritrasmettere le credenziali
    @GET
    @Path("/refresh")
    @AuthLevel1
    public Response refreshJWTToken(@Context HttpHeaders headers, @Context UriInfo uriinfo) {
        try {
            String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
            String token = authorizationHeader.substring("Bearer".length()).trim();
            Key key = JWTHelpers.getInstance().getJwtKey();
            Claims jwsc = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            String newtoken = issueToken(uriinfo, jwsc.getSubject());
            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + newtoken).build();
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

}
