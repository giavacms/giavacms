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

      Page defaultBasePage = null;
      try
      {
         defaultBasePage = (Page) em
                  .createQuery(
                           "select p from " + Page.class.getSimpleName()
                                    + " p where p.extension = :EXTENSION and p.clone = :CLONE ")
                  .setParameter("EXTENSION", RichContent.EXTENSION).setParameter("CLONE", false).setMaxResults(1)
                  .getSingleResult();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException("Deve esistere almeno una pagina base da usare come default per le news importate");
      }

      List<String> richNewsTypes = em.createQuery(
               "select distinct(rnt.name) from " + RichNewsType.class.getSimpleName() + " rnt ")
               .getResultList();

      Map<String, RichContentType> richContentTypes = doImport(em, richNewsTypes, defaultBasePage);

      List<String> richNewsIds = em.createQuery("select rn.id from " + RichNews.class.getSimpleName() + " rn ")
               .getResultList();

      for (String richNewsId : richNewsIds)
      {
         RichNews richNews = fetch(em, richNewsId);
         doImport(em, richNews, richContentTypes, defaultBasePage);
      }

   }

   private Map<String, RichContentType> doImport(EntityManager em, List<String> richNewsTypes, Page defaultBasePage)
   {
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
            rct.getPage().setId(defaultBasePage.getId());
            rct = richContentTypeRepository.persist(rct);
            if (rct == null)
            {
               throw new RuntimeException("Errore nel salvataggio del tipo di contenuto: " + richNewsType);
            }
            else
            {
               map.put(rct.getName(), rct);
            }
         }
      }
      return map;
   }

   private void doImport(EntityManager em, RichNews rn, Map<String, RichContentType> rctMap, Page defaultBasePage)
   {
      RichContent rc = new RichContent();
      rc.setActive(rn.isActive());
      rc.setAuthor(rn.getAuthor());
      rc.setClone(true);
      rc.setContent(rn.getContent());
      rc.setDate(rn.getDate());
      rc.setDescription(rn.getPreview());
      rc.setDescription(rn.getTitle());
      rc.setExtension(RichContent.EXTENSION);
      rc.setHighlight(false);
      // non posso settare in fase di persist dei documenti o delle immagini gia esistenti. le associo in fase di update
      rc.setDocuments(null);
      rc.setImages(null);
      // da aggiornare una volta generato l'id
      rc.setLang1id(null);
      rc.setPreview(rn.getPreview());
      rc.setRichContentType(rctMap.get(rn.getRichNewsType().getName()));
      rc.setTemplate(defaultBasePage.getTemplate());
      rc.setTitle(rn.getTitle());
      rc = richContentRepository.persist(rc);
      if (rc == null)
      {
         throw new RuntimeException("Errore nel salvataggio della news #" + rn.getId() + ": " + rn.getTitle());
      }
      // em.createQuery("update " + RichContent.class.getSimpleName() + " c set c.lang1id = c.id where c.id = :ID ")
      // .setParameter("ID", rc.getId()).executeUpdate();
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
      rc.setLang1id(rc.getId());
      richContentRepository.update(rc);

   }

   private RichNews fetch(EntityManager em, String richNewsId)
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
