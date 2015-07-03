package org.giavacms.chalet.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.FreeTicket;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.*;

@Named
@Stateless
@LocalBean
public class FreeTicketRepository extends BaseRepository<FreeTicket>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "created desc";
   }

   @Override
   protected void applyRestrictions(Search<FreeTicket> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   public Map<String, List<FreeTicket>> getFreeTicketForChalet(Date date)
   {
      Map<String, List<FreeTicket>> freeTicketMap = new HashMap<>();
      List<FreeTicket> freeTickets = new ArrayList<>();
      for (FreeTicket freeTicket : freeTickets)
      {
         List<FreeTicket> forChalet;
         if (freeTicketMap.containsKey(freeTicket.getChalet().getLicenseNumber()))
         {
            forChalet = freeTicketMap.get(freeTicket.getChalet().getLicenseNumber());
            freeTicketMap.put(freeTicket.getChalet().getLicenseNumber(), forChalet);
         }
         else
         {
            forChalet = new ArrayList<>();
         }
         forChalet.add(freeTicket);
      }
      return freeTicketMap;
   }

}
