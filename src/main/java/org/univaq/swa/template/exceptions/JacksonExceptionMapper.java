package org.univaq.swa.template.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author didattica
 */
@Provider
public class JacksonExceptionMapper implements ExceptionMapper<JsonMappingException> {

    @Override
    public Response toResponse(JsonMappingException exception) {
        //utile per catturare tutte le eccezioni derivanti dalla serializzazione/deserializzazione automatica di oggetti
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON").build();
    }
}
