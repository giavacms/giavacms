package org.giavacms.chalet.utils;

import java.util.Date;

import org.giavacms.api.model.Search;
import org.giavacms.chalet.model.Photo;

public class PhotoUtils
{
   /**
    * @param chaletId
    * @param accountId male non farà, al limite è nullo
    * @param approved nullo (tutte), vero (solo ok) o falso (solo ko)
    * @param evaluated nullo (tutte), vero (solo già valutate) o falso (solo in sospeso)
    * @return
    */
   public static Search<Photo> makeSearch(String chaletId, String accountId, Boolean approved,
            Boolean evaluated)
   {
      Search<Photo> sp = new Search<Photo>(Photo.class);
      sp.getObj().setChaletId(chaletId);
      sp.getObj().setAccountId(accountId);
      if (approved != null)
      {
         if (approved)
         {
            sp.getObj().setApproved(true);
         }
         else
         {
            sp.getNot().setApproved(true);
         }
      }
      if (evaluated != null)
      {
         if (evaluated)
         {
            sp.getObj().setApprovedDate(new Date());
         }
         else
         {
            sp.getNot().setApprovedDate(new Date());
         }
      }
      return sp;
   }
}
