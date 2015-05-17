package org.giavacms.api.service;

import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.Serializable;

public abstract class RsService implements Serializable {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private static final long serialVersionUID = 1L;

    @GET
    @Path("/up")
    public Response up() {
        return Response.status(Response.Status.OK).entity(true).build();
    }

}
