package org.univaq.swa.framework.base;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class RESTWebApplicationException extends WebApplicationException {

    public RESTWebApplicationException() {
        super(Response.serverError().build());
    }

    public RESTWebApplicationException(String message) {
        super(Response.serverError()
                .entity(message)
                .type("text/plain")
                .build());
    }

    public RESTWebApplicationException(int status, String message) {
        super(Response.status(status)
                .entity(message)
                .type("text/plain")
                .build());
    }
}
