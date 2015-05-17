package org.giavacms.base.filter;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException>
{

   @Override
   public Response toResponse(UnrecognizedPropertyException exception)
   {
      exception.printStackTrace();
      return Response.status(Response.Status.BAD_REQUEST).build();
   }

}
