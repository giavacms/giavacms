package org.giavacms.api.service;

import org.giavacms.api.model.Search;
import org.giavacms.api.repository.Repository;
import org.giavacms.api.util.RepositoryUtils;
import org.jboss.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
public abstract class RsRepositoryService<T> implements Serializable
{

   private static final long serialVersionUID = 1L;

   protected final Logger logger = Logger.getLogger(getClass());

   private Repository<T> repository;

   public RsRepositoryService()
   {

   }

   public RsRepositoryService(Repository<T> repository)
   {
      this.repository = repository;
   }

   /*
    * C
    */

   protected void prePersist(T object) throws Exception
   {
   }

   @POST
   public Response persist(T object) throws Exception
   {
      try
      {
         prePersist(object);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage());
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error before creating resource: " + e.getMessage());
      }
      try
      {
         T persisted = repository.persist(object);
         if (persisted == null || getId(persisted) == null)
         {
            logger.error("Failed to create resource: " + object);
            Map<String, String> map = new HashMap<>();
            map.put("Failed to create resource: ", object.toString());
            return jsonResponse(map, Status.INTERNAL_SERVER_ERROR);
         }
         else
         {
            return Response.status(Status.OK).entity(persisted)
                     .build();
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error creating resource: " + object.toString());
      }
      finally
      {
         try
         {
            postPersist(object);
         }
         catch (Exception e)
         {
            logger.error(e.getMessage(), e);
         }
      }
   }

   protected void postPersist(T object) throws Exception
   {
   }

   /*
    * R
    */

   @GET
   @Path("/{id}")
   public Response fetch(@PathParam("id") String id)
   {
      logger.info("@GET :" + id);
      try
      {
         T t = repository.fetch(repository.castId(id));
         if (t == null)
         {
            return jsonResponse(Status.NOT_FOUND, "msg", "Resource not found for ID: " + id);
         }
         else
         {
            return Response.status(Status.OK).entity(t).build();
         }
      }
      catch (NoResultException e)
      {
         logger.error(e.getMessage());
         return jsonResponse(Status.NOT_FOUND, "msg", "Resource not found for ID: " + id);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error reading resource for ID: " + id);
      }
   }

   /*
    * U
    */

   protected void preUpdate(T object) throws Exception
   {
   }

   @PUT
   @Path("/{id}")
   public Response update(@PathParam("id") String id, T object) throws Exception
   {
      logger.info("@PUT update:" + object.toString());
      try
      {
         preUpdate(object);
      }
      catch (Exception e)
      {
         return jsonResponse(Status.BAD_REQUEST, "msg", "Errore before updating resource: "
                  + e.getMessage());
      }
      try
      {
         repository.update(object);
         return Response.status(Status.OK).entity(object).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error updating resource: " + object);
      }
      finally
      {
         try
         {
            postUpdate(object);
         }
         catch (Exception e)
         {
            logger.error(e.getMessage(), e);
         }
      }
   }

   protected void postUpdate(T object) throws Exception
   {
   }

   /*
    * D
    */

   @DELETE
   @Path("/{id}")
   public Response delete(@PathParam("id") String id) throws Exception
   {
      logger.info("@DELETE:" + id);
      try
      {
         preDelete(id);
      }
      catch (Exception e)
      {
         return jsonResponse(Status.BAD_REQUEST, "msg", "Errore before deleting resource: " + id);
      }
      try
      {
         repository.delete(repository.castId(id));
         return jsonResponse(Status.NO_CONTENT, "msg", "Resource deleted for ID: " + id);
      }
      catch (NoResultException e)
      {
         logger.error(e.getMessage());
         return jsonResponse(Status.NOT_FOUND, "msg", "Resource not found for ID: " + id);
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error deleting resource for ID: " + id);
      }
      finally
      {
         try
         {
            postDelete(id);
         }
         catch (Exception e)
         {
            logger.error(e.getMessage(), e);
         }
      }
   }

   protected void preDelete(Object key) throws Exception
   {
   }

   protected void postDelete(Object key) throws Exception
   {
   }

   /*
    * E
    */

   @GET
   @Path("/{id}/exist")
   public Response exist(@PathParam("id") String id)
   {
      logger.info("@GET exist:" + id);
      try
      {
         boolean exist = repository.exist(repository.castId(id));
         if (!exist)
         {
            return jsonResponse(Status.NOT_FOUND, "msg", "Resource not found for ID: " + id);
         }
         else
         {
            return jsonResponse(Status.FOUND, "msg", "Resource exists for ID: " + id);
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error reading resource for ID: " + id);
      }
   }

   /*
    * Q
    */

   @GET
   public Response getList(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @Context UriInfo ui)
   {
      logger.info("@GET list");
      try
      {
         Search<T> search = getSearch(ui, orderBy);
         int listSize = repository.getListSize(search);
         List<T> list = repository.getList(search, startRow, pageSize);
         // PaginatedListWrapper<T> wrapper = new PaginatedListWrapper<>();
         // wrapper.setList(list);
         // wrapper.setListSize(listSize);
         // wrapper.setStartRow(startRow);
         return Response.status(Status.OK).entity(list)
                  .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize")
                  .header("startRow", startRow)
                  .header("pageSize", pageSize)
                  .header("listSize", listSize)
                  .build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return jsonResponse(Status.INTERNAL_SERVER_ERROR, "msg", "Error reading resource list");
      }
   }

   protected Search<T> getSearch(UriInfo ui, String orderBy)
   {
      Search<T> s = new Search<T>(getClassType());
      if (orderBy != null && !orderBy.trim().isEmpty())
      {
         s.setOrder(orderBy);
      }
      if (ui != null && ui.getQueryParameters() != null
               && !ui.getQueryParameters().isEmpty())
      {
         MultivaluedMap<String, String> queryParams = ui
                  .getQueryParameters();
         // TODO - DA TESTARE:
         // tutte le prop su search.getObj() possono essere in "chiaro" - senza prefissi
         // tutte le prop su oggetti search.getFrom() - search.getTo() sono con prefisso: es.from.id - from.dataInit

         makeSearch(queryParams, s);

      }

      return s;
   }

   <T> void makeSearch(MultivaluedMap<String, String> queryParams, Search<T> s)
   {
      for (String key : queryParams.keySet())
      {
         try
         {
            T instance = s.getObj();
            String value = queryParams.getFirst(key);
            value = URLDecoder.decode(value, "UTF-8");

            String fieldName = key;
            if (key.startsWith("obj."))
            {
               instance = s.getObj();
               fieldName = key.substring(4);
            }
            else if (key.startsWith("from."))
            {
               instance = s.getFrom();
               fieldName = key.substring(5);
            }
            else if (key.startsWith("to."))
            {
               instance = s.getTo();
               fieldName = key.substring(3);
            }
            else if (key.startsWith("like."))
            {
               instance = s.getLike();
               fieldName = key.substring(5);
            }
            else if (key.startsWith("not."))
            {
               instance = s.getNot();
               fieldName = key.substring(4);
            }

            RepositoryUtils.setFieldByName(instance.getClass(), instance, fieldName, value);

         }
         catch (Exception e)
         {
            logger.error(e.getMessage());
         }
      }

   }

   protected Repository<T> getRepository()
   {
      return repository;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private Class<T> getClassType()
   {
      Class clazz = getClass();
      while (!(clazz.getGenericSuperclass() instanceof ParameterizedType))
      {
         clazz = clazz.getSuperclass();
      }
      ParameterizedType parameterizedType = (ParameterizedType) clazz
               .getGenericSuperclass();
      return (Class<T>) parameterizedType.getActualTypeArguments()[0];
   }

   /**
    * Override this is needed
    *
    * @param t
    * @return
    */

   protected Object getId(T t)
   {
      return RepositoryUtils.getId(t);
   }

   @OPTIONS
   public Response options()
   {
      logger.info("@OPTIONS");
      return Response.ok().build();
   }

   @OPTIONS
   @Path("{path:.*}")
   public Response allOptions()
   {
      logger.info("@OPTIONS ALL");
      return Response.ok().build();
   }

   @GET
   @Path("/up")
   public Response up()
   {
      return Response.status(Response.Status.OK).entity(true).build();
   }

   public static Response jsonResponse(Map<String, String> toJson, Response.Status status)
   {
      JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
      for (String key : toJson.keySet())
      {
         jsonObjBuilder.add(key, toJson.get(key));
      }

      JsonObject jsonObj = jsonObjBuilder.build();
      return Response.status(status)
               .entity(jsonObj.toString())
               .build();
   }

   public static Response jsonResponse(Response.Status status, String key, Object value)
   {
      JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
      jsonObjBuilder.add(key, value.toString());
      JsonObject jsonObj = jsonObjBuilder.build();
      return Response.status(status)
               .entity(jsonObj.toString())
               .build();
   }

}