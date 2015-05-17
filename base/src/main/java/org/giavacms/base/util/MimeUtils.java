package org.giavacms.base.util;

public class MimeUtils
{

   /**
    * PostScript ai eps ps application/postscript Microsoft Rich Text Format rtf application/rtf Adobe Acrobat PDF pdf
    * application/pdf application/x-pdf Maker Interchange Format (FrameMaker) mif application/vnd.mif application/x-mif
    * Troff document t tr roff application/x-troff Troff document with MAN macros man application/x-troff-man Troff
    * document with ME macros me application/x-troff-me Troff document with MS macros ms application/x-troff-ms LaTeX
    * document latex application/x-latex Tex/LateX document tex application/x-tex GNU TexInfo document texinfo texi
    * application/x-texinfo TeX dvi format dvi application/x-dvi MacWrite document ?? application/macwriteii MS word
    * document ?? application/msword WordPerfect 5.1 document ?? application/wordperfect5.1 SGML application (RFC 1874)
    * application/sgml Office Document Architecture oda application/oda Envoy Document evy application/envoy
    * 
    * @param filename
    * @return
    */
   public static String getContentType(String filename)
   {
      if (filename == null || filename.equals(""))
      {
         return "text/html";
      }
      else if (filename.toLowerCase().endsWith(".pdf"))
      {
         return "application/pdf";
      }
      else if (filename.toLowerCase().endsWith(".rtf"))
      {
         return "application/rtf";
      }
      else if (filename.toLowerCase().endsWith(".doc"))
      {
         return "application/msword";
      }
      else if (filename.toLowerCase().endsWith(".xls"))
      {
         return "application/excel";
      }
      else if (filename.toLowerCase().endsWith(".zip"))
      {
         return "application/zip";
      }
      else if (filename.toLowerCase().endsWith(".xml"))
      {
         return "text/xml";
      }
      // application/vnd.openxmlformats .docx .pptx .xlsx .xltx . xltm .dotx
      // .potx .ppsx
      else if (filename.toLowerCase().endsWith(".docx") || filename.toLowerCase().endsWith(".pptx")
               || filename.toLowerCase().endsWith(".xlsx") || filename.toLowerCase().endsWith(".xltx")
               || filename.toLowerCase().endsWith(".xltm") || filename.toLowerCase().endsWith(".dotx")
               || filename.toLowerCase().endsWith(".potx") || filename.toLowerCase().endsWith(".ppsx"))
      {
         return "application/vnd.openxmlformats";
      }
      else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")
               || filename.toLowerCase().endsWith(".jpe"))
      {
         return "image/jpeg";
      }
      else if (filename.toLowerCase().endsWith(".csv"))
      {
         return "text/csv";
      }
      else if (filename.toLowerCase().endsWith(".png"))
      {
         return "image/png";
      }
      else if (filename.toLowerCase().endsWith(".gif"))
      {
         return "image/gif";
      }
      else if (filename.toLowerCase().endsWith(".msg"))
      {
         return "application/vnd.ms-outlook";
      }
      else if (filename.toLowerCase().endsWith(".eml"))
      {
         return "message/rfc822";
      }
      else if (filename.toLowerCase().endsWith(".tif") || filename.toLowerCase().endsWith(".tiff"))
      {
         return "image/tiff";
      }
      else if (filename.toLowerCase().endsWith(".bmp"))
      {
         return "image/bmp";
      }
      else if (filename.toLowerCase().endsWith(".txt"))
      {
         return "text/plain";
      }

      return "text/html";
   }
}
