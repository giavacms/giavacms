package org.giavacms.richnews10importer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.repository.RichContentRepository;
import org.giavacms.richcontent.repository.RichContentTypeRepository;
import org.giavacms.richnews10importer.model.RichNews;
import org.giavacms.richnews10importer.model.RichNewsType;

@Named
@Stateless
@LocalBean
public class Richnews10ImporterService
{

   @Inject
   RichContentRepository richContentRepository;
   @Inject
   RichContentTypeRepository richContentTypeRepository;

   @SuppressWarnings("unchecked")
   public void doImport()
   {
      EntityManager em = richContentRepository.getEm();

      List<Long> richNewsIds = em.createQuery("select rn.id from " + RichNews.class.getSimpleName() + " rn ")
               .getResultList();

      List<String> richNewsTypes = em.createQuery(
               "select distinct(rnt.name) from " + RichNewsType.class.getSimpleName() + " rnt ")
               .getResultList();

      Map<String, RichContentType> richContentTypes = doImport(em, richNewsTypes);

      for (Long richNewsId : richNewsIds)
      {
         RichNews richNews = fetch(em, richNewsId);
         doImport(richNews, richContentTypes);
      }

   }

   private Map<String, RichContentType> doImport(EntityManager em, List<String> richNewsTypes)
   {
      String basePageId = null;
      try
      {
         basePageId = (String) em
                  .createQuery(
                           "select p.id from " + Page.class.getSimpleName()
                                    + " p where p.extension = :EXTENSION and p.clone = :CLONE ").setMaxResults(1)
                  .getSingleResult();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException("Deve esistere almeno una pagina base da usare come default per le news importate");
      }
      List<RichContentType> richContentTypes = richContentTypeRepository.getAllList();
      Map<String, RichContentType> map = new HashMap<String, RichContentType>();
      for (RichContentType richContentType : richContentTypes)
      {
         if (richContentType.isActive())
         {
            map.put(richContentType.getName(), richContentType);
         }
      }

      for (String richNewsType : richNewsTypes)
      {
         if (map.get(richNewsType) == null)
         {
            RichContentType rct = new RichContentType();
            rct.setActive(true);
            rct.setName(richNewsType);
            rct.setPage(new Page());
            rct.getPage().setId(basePageId);
            rct = richContentTypeRepository.persist(rct);
            if (rct == null)
            {
               throw new RuntimeException("Errore nel salvataggio del tipo di contenuto: " + richNewsType);
            }
         }
      }
      return map;
   }

   private void doImport(RichNews rn, Map<String, RichContentType> rctMap)
   {
      RichContent rc = new RichContent();
      rc.setActive(rn.isActive());
      rc.setAuthor(rn.getAuthor());
      rc.setClone(true);
      rc.setContent(rn.getContent());
      rc.setDate(rn.getDate());
      rc.setDescription(rn.getPreview());
      rc.setDescription(rn.getTitle());
      if (rn.getDocuments() != null)
      {
         rc.setDocuments(new ArrayList<Document>());
         for (Document d : rn.getDocuments())
         {
            Document nd = new Document();
            nd.setId(d.getId());
            rc.getDocuments().add(nd);
         }
      }
      rc.setExtension(RichContent.EXTENSION);
      rc.setHighlight(rn.isHighlight());
      if (rn.getImages() != null)
      {
         rc.setImages(new ArrayList<Image>());
         for (Image i : rn.getImages())
         {
            Image ni = new Image();
            ni.setId(i.getId());
            rc.getImages().add(ni);
         }
      }
      rc = richContentRepository.persist(rc);
      if (rc == null)
      {
         throw new RuntimeException("Errore nel salvataggio della news #" + rn.getId() + ": " + rn.getTitle());
      }
   }

   private RichNews fetch(EntityManager em, Long richNewsId)
   {
      RichNews rn = em.find(RichNews.class, richNewsId);
      if (rn.getImages() != null)
      {
         for (Image rni : rn.getImages())
         {
            rni.toString();
         }
      }
      if (rn.getDocuments() != null)
      {
         for (Document rnd : rn.getDocuments())
         {
            rnd.toString();
         }
      }
      return rn;
   }
}
