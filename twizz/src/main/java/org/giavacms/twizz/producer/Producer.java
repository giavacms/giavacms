package org.giavacms.twizz.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.twizz.repository.ArgumentRepository;

@Named
@SessionScoped
public class Producer implements Serializable
{
   private static final long serialVersionUID = 1L;
   List<String> languages;
   Map<String, List<String>> arguments;

   @Inject
   ArgumentRepository argumentRepository;

   public List<String> getLanguages()
   {
      if (languages == null)
      {
         languages = new ArrayList<String>();
         languages.add("it-IT");
         languages.add("en-EN");
      }
      return languages;
   }

   public List<String> getArguments(String language)
   {
      if (arguments == null)
      {
         arguments = argumentRepository.getArgumentsByLanguages();
      }
      return arguments.get(language);
   }

}
