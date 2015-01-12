package org.giavacms.faq.controller.request;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.faq.model.Faq;
import org.giavacms.faq.repository.FaqRepository;

@Named
@RequestScoped
public class FaqRandomRequestController
         extends FaqRequestController implements Serializable
{

   private static final long serialVersionUID = 1L;

   Random random = new Random();

   @Inject
   @OwnRepository(FaqRepository.class)
   FaqRepository faqRepository;

   @Override
   public List<Faq> loadPage(int startRow, int pageSize)
   {
      int size = faqRepository.getListSize(getSearch());
      startRow = random.nextInt(size);
      int maxIterations = 10;
      while ((maxIterations > 0) && ((size - startRow) < pageSize))
      {
         startRow = random.nextInt(size);
         maxIterations--;
      }
      return super.loadPage(startRow, pageSize);
   }
}
