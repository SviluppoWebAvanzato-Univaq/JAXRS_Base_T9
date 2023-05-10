package org.univaq.swa.template.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.univaq.swa.template.exceptions.CustomException;

/**
 *
 * @author didattica
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<CustomException> {
    @Override
    public Response toResponse(CustomException exception) {
        //possiamo trasformare queste eccezioni in una risposta formattata, se non le catturiamo all'origine...
        return Response.serverError().entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
    }
}
