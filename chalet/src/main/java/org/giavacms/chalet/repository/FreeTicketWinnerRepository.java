package org.giavacms.chalet.repository;

import org.giavacms.api.model.Search;
import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.FreeTicketWinner;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@Stateless
@LocalBean
public class FreeTicketWinnerRepository extends BaseRepository<FreeTicketWinner>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "created desc";
   }

   @Override
   protected void applyRestrictions(Search<FreeTicketWinner> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

      super.applyRestrictions(search, alias, separator, sb, params);

   }

   public List<String> getAllreadyWinners()
   {
      List<String> winners = (List<String>) getEm()
               .createNativeQuery(" select distinct(phonenumber) from " + FreeTicketWinner.TABLE_NAME)
               .getResultList();
      return winners;
   }

}
