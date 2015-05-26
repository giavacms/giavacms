package org.giavacms.catalogue.service.rs;

import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.catalogue.management.AppConstants;
import org.giavacms.catalogue.model.Feature;
import org.giavacms.catalogue.model.pojo.FeatureItems;
import org.giavacms.catalogue.repository.FeatureRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(AppConstants.BASE_PATH + AppConstants.FEATURES_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeatureRepositoryRs extends RsRepositoryService<Feature>
{

   private static final long serialVersionUID = 1L;

   public FeatureRepositoryRs()
   {
   }

   @Inject
   public FeatureRepositoryRs(FeatureRepository featureRepository)
   {
      super(featureRepository);
   }

   @GET
   @Path("/names")
   public List<String> getRefereanceableNames()
   {
      return ((FeatureRepository) getRepository()).getRefereanceableNames();
   }

   @GET
   @Path("/names/{name}")
   public List<String> getAvailableOptions(@PathParam("name") String name)
   {
      return ((FeatureRepository) getRepository()).getAvailableOptions(name);
   }

   @GET
   @Path("/names/{name}/options")
   public List<String> getOptions(@PathParam("name") String name)
   {
      return ((FeatureRepository) getRepository()).getOptions(name);
   }

   @GET
   @Path("/items")
   public List<FeatureItems> getFeatureItems()
   {
      return ((FeatureRepository) getRepository()).getItems();
   }

}
