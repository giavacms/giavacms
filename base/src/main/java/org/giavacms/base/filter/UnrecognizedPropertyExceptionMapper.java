package org.giavacms.base.filter;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.jboss.logging.Logger;

import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

    Logger logger = Logger.getLogger(getClass().getName());


    @Override
    public Response toResponse(UnrecognizedPropertyException exception) {
        logger.info(exception.getMessage());
//        exception.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
