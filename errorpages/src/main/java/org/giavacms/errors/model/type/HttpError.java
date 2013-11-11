package org.giavacms.errors.model.type;

import java.util.logging.Logger;

public enum HttpError
{

   /**
    * This should not happen since pages are always retrieved from db
    */
   HttpCode304(
            "not modified",
            "the document has not been modified since the date and time specified thus the server does not send the document body to the client"),

   HttpCode400("bad request",
            "the request had bad syntax or was inherently impossible to be satisfied"),

   HttpCode401(
            "unauthorized",
            "the parameter to this message gives a specification of authorization schemes which are acceptable. The client should retry the request with a suitable Authorization header"),

   HttpCode403("forbidden", "the request is for something forbidden"),

   HttpCode404("not found",
            "the server has not found anything matching the URI given"),

   HttpCode500(
            "internal error",
            "the server encountered an unexpected condition which prevented it from fulfilling the request"),

   HttpCode501("not implemented",
            "the server does not support the facility required"),

   HttpCode502(
            "service temporarily overloaded",
            "the server cannot process the request due to a high load, but this is a temporary condition which maybe alleviated at other times"),

   // javaLangException("internal error",
   // "use this to better specify http code 500"),
   //
   // javaLangThrowable("internal error",
   // "use this to better specify http code 500"),
   //
   // javaLangNullPointerException("internal error",
   // "use this to better specify http code 500"),

   ;

   String meaning;
   String description;

   static Logger logger = Logger.getLogger(HttpError.class.getName());

   private HttpError(String meaning, String description)
   {
      this.meaning = meaning;
      this.description = description;
   }

   public String getMeaning()
   {
      return meaning;
   }

   public String getDescription()
   {
      return description;
   }

   public String getDefaultContent()
   {
      return new StringBuffer("<html><head><title>").append(this.name())
               .append("</title></head><body><h1>").append(meaning)
               .append("</h1><hr/><p>").append(description)
               .append("</p></body></html>").toString();
   }

   public static void main(String[] args)
   {
      for (HttpError httpError : HttpError.values())
      {
         logger.info("<error-page>");
         logger.info("<error-code>"
                  + httpError.name().replace("HttpCode", "")
                  + "</error-code>");
         logger.info("<location>/errors/" + httpError.name()
                  + ".html</location>");
         logger.info("</error-page>");
      }

   }
}
