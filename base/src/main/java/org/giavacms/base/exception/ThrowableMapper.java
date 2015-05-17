package org.giavacms.base.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrowableMapper implements
        javax.ws.rs.ext.ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(throwable.getMessage()).type(MediaType.APPLICATION_XML)
                .build();
    }

}
