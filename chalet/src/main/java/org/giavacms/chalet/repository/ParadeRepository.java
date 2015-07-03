package org.giavacms.chalet.repository;

import javax.ejb.Stateless;

import org.giavacms.base.repository.BaseRepository;
import org.giavacms.chalet.model.Parade;

@Stateless
public class ParadeRepository extends BaseRepository<Parade>
{

   private static final long serialVersionUID = 1L;

   @Override
   protected String getDefaultOrderBy()
   {
      return "data desc";
   }
}
