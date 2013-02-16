/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.FileUtils;
import org.giavacms.base.pojo.Resource;
import org.giavacms.base.producer.BaseProducer;
import org.giavacms.base.repository.ResourceRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;


@Named
@SessionScoped
public class ResourceController extends AbstractLazyController<Resource>
{

   private static final long serialVersionUID = 1L;

   // --------------------------------------------------------

   @BackPage
   public static String BACK = "/private/administration.xhtml";
   @ListPage
   @ViewPage
   public static String LIST = "/private/resource/list.xhtml";
   @EditPage
   public static String EDIT = "/private/resource/edit.xhtml";

   public static String UPLOAD = "/private/resource/load.xhtml";

   // --------------------------------------------------------

   @Inject
   @OwnRepository(ResourceRepository.class)
   ResourceRepository resourceRepository;

   @Inject
   BaseProducer baseProducer;

   // --------------------------------------------------------

   private List<Resource> uploadedResources = null;

   private UploadedFile replacementFile;

   // ------------------------------------------------

   /**
    * Obbligatoria l'invocazione 'appropriata' di questo super construttore protetto da parte delle sottoclassi
    */
   public ResourceController()
   {
   }

   // ------------------------------------------------

   /**
    * Metodo da implementare per assicurare che i criteri di ricerca contengano sempre tutti i vincoli desiderati (es:
    * identità utente, selezioni esterne, ecc...)
    */
   @Override
   public void defaultCriteria()
   {
      getSearch().getObj().setType(
               baseProducer.getResourceItems()[0].getValue().toString());
   }

   /**
    * Metodo di navigazione per resettare lo stato interno e tornare alla pagina dell'elenco generale
    * 
    * @return
    */
   @Override
   public String reset()
   {
      this.uploadedResources = null;
      return super.reset();
   }

   // -----------------------------------------------------

   @Override
   public String addElement()
   {
      // impostazioni locali
      this.uploadedResources = new ArrayList<Resource>();
      super.addElement();
      return UPLOAD + REDIRECT_PARAM;
   }

   @Override
   public String modElement()
   {
      Resource t = (Resource) getModel().getRowData();
      t = resourceRepository.fetch(t.getType(), t.getId());
      super.setEditMode(true);
      super.setReadOnlyMode(false);
      setElement(t);
      return editPage();
   }

   @Override
   public String save()
   {
      for (Resource resource : uploadedResources)
      {
         if (resource.getType() == null)
            resource.setType(getElement().getType());
         resourceRepository.persist(resource);
      }
      // refresh locale
      refreshModel();
      // altre dipendenze
      //
      return listPage();
   }

   @Override
   public String update()
   {
      if (getElement().getType().equals("js")
               || getElement().getType().equals("css"))
      {
         logger.debug("don't exist new resource uploaded; let's persist modifications on text");
         resourceRepository.updateResource(getElement());
         setElement(resourceRepository.fetch(getElement().getType(),
                  getElement().getId()));
         // refresh locale
         refreshModel();
      }
      else
      {
         logger.debug("we need to cope with replacement via resource uploaded");

         if (getReplacementFile() != null)
         {
            try
            {
               String filename = getReplacementFile().getFileName();
               if (filename.contains("\\"))
                  filename = filename.substring(filename
                           .lastIndexOf("\\") + 1);
               Resource resource = new Resource();
               resource.setInputStream(getReplacementFile()
                        .getInputstream());
               resource.setBytes(getReplacementFile().getContents());
               resource.setName(filename);
               resource.setId(getElement().getId());
               resource.setType(FileUtils.getType(filename));
               resourceRepository.updateResource(resource);
               setElement(resource);
               // refresh locale
               refreshModel();
            }
            catch (IOException e)
            {
               logger.info(e.getMessage());
               super.addFacesMessage("Errori nel salvataggio della risorsa");
               return null;
            }
         }
      }
      return viewPage();
   }

   public String delElement(String tipo, String id)
   {
      logger.info("delElement: " + tipo + " -" + id);
      Resource resource = new Resource();
      resource.setId(id);
      resource.setName(id);
      resource.setType(tipo);
      resourceRepository.delete(resource);
      return listPage();
   }

   // -----------------------------------------------------

   public void handleFileUpload(FileUploadEvent event)
   {
      try
      {
         UploadedFile file = event.getFile();
         InputStream is = event.getFile().getInputstream();
         String filename = file.getFileName();
         if (filename.contains("\\"))
            filename = filename.substring(filename.lastIndexOf("\\") + 1);
         Resource resource = new Resource();
         resource.setInputStream(is);
         resource.setName(filename);
         resource.setType(FileUtils.getType(filename));

         resource.setBytes(file.getContents());
         uploadedResources.add(resource);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // -----------------------------------------------------
   public List<Resource> getUploadedResources()
   {
      return uploadedResources;
   }

   public Resource getResource(int index)
   {
      return uploadedResources.get(index);
   }

   public StreamedContent getResourceStream(int index)
   {
      Resource rs = uploadedResources.get(index);
      if (!"img".equals(rs.getType()))
         return null;
      StreamedContent image = new DefaultStreamedContent(rs.getInputStream(),
               rs.getType());
      return image;
   }

   public Resource getSingleResource(int row)
   {
      try
      {
         Resource resource = uploadedResources.get(row);
         return resource;
      }
      catch (Exception e)
      {
         return null;
      }
   }

   public String getExtensionsByType(String type)
   {
      return FileUtils.getRegExpByTypes(new String[] { type });
   }

   public UploadedFile getReplacementFile()
   {
      return replacementFile;
   }

   public void setReplacementFile(UploadedFile replacementFile)
   {
      this.replacementFile = replacementFile;
   }

}