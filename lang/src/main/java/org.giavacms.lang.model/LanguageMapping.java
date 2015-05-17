package org.giavacms.lang.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class LanguageMapping implements Serializable
{
   private String pageId;
   private String type;
   private String langName;

   private String pageId1;
   private String type1;
   private String langName1;

   private String pageId2;
   private String type2;
   private String langName2;

   private String pageId3;
   private String type3;
   private String langName3;

   private String pageId4;
   private String type4;
   private String langName4;

   private String pageId5;
   private String type5;
   private String langName5;

   public LanguageMapping()
   {
   }

   public LanguageMapping(String pageId, String type, String langName)
   {
      this.pageId = pageId;
      this.type = type;
      this.langName = langName;
   }

   public LanguageMapping(String pageId, String type, String langName, String pageId1, String type1,
            String langName1)
   {
      this.pageId = pageId;
      this.type = type;
      this.langName = langName;
      this.pageId1 = pageId1;
      this.type1 = type1;
      this.langName1 = langName1;
   }

   public LanguageMapping(String pageId, String type, String langName, String pageId1, String type1,
            String langName1, String pageId2, String type2, String langName2)
   {
      this.pageId = pageId;
      this.type = type;
      this.langName = langName;
      this.pageId1 = pageId1;
      this.type1 = type1;
      this.langName1 = langName1;
      this.pageId2 = pageId2;
      this.type2 = type2;
      this.langName2 = langName2;
   }

   public LanguageMapping(String pageId, String type, String langName, String pageId1, String type1,
            String langName1, String pageId2, String type2, String langName2, String pageId3, String type3,
            String langName3)
   {
      this.pageId = pageId;
      this.type = type;
      this.langName = langName;
      this.pageId1 = pageId1;
      this.type1 = type1;
      this.langName1 = langName1;
      this.pageId2 = pageId2;
      this.type2 = type2;
      this.langName2 = langName2;
      this.pageId3 = pageId3;
      this.type3 = type3;
      this.langName3 = langName3;
   }

   public LanguageMapping(String pageId, String type, String langName, String pageId1, String type1,
            String langName1, String pageId2, String type2, String langName2, String pageId3, String type3,
            String langName3, String pageId4, String type4, String langName4)
   {
      this.pageId = pageId;
      this.type = type;
      this.langName = langName;
      this.pageId1 = pageId1;
      this.type1 = type1;
      this.langName1 = langName1;
      this.pageId2 = pageId2;
      this.type2 = type2;
      this.langName2 = langName2;
      this.pageId3 = pageId3;
      this.type3 = type3;
      this.langName3 = langName3;
      this.pageId4 = pageId4;
      this.type4 = type4;
      this.langName4 = langName4;
   }

   public LanguageMapping(String pageId, String type, String langName, String pageId1, String type1,
            String langName1, String pageId2, String type2, String langName2, String pageId3, String type3,
            String langName3, String pageId4, String type4, String langName4, String pageId5, String type5,
            String langName5)
   {
      this.pageId = pageId;
      this.type = type;
      this.langName = langName;
      this.pageId1 = pageId1;
      this.type1 = type1;
      this.langName1 = langName1;
      this.pageId2 = pageId2;
      this.type2 = type2;
      this.langName2 = langName2;
      this.pageId3 = pageId3;
      this.type3 = type3;
      this.langName3 = langName3;
      this.pageId4 = pageId4;
      this.type4 = type4;
      this.langName4 = langName4;
      this.pageId5 = pageId5;
      this.type5 = type5;
      this.langName5 = langName5;
   }

   @Id
   public String getPageId()
   {
      return pageId;
   }

   public void setPageId(String pageId)
   {
      this.pageId = pageId;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public String getLangName()
   {
      return langName;
   }

   public void setLangName(String langName)
   {
      this.langName = langName;
   }

   public String getPageId1()
   {
      return pageId1;
   }

   public void setPageId1(String pageId1)
   {
      this.pageId1 = pageId1;
   }

   public String getType1()
   {
      return type1;
   }

   public void setType1(String type1)
   {
      this.type1 = type1;
   }

   public String getLangName1()
   {
      return langName1;
   }

   public void setLangName1(String langName1)
   {
      this.langName1 = langName1;
   }

   public String getPageId2()
   {
      return pageId2;
   }

   public void setPageId2(String pageId2)
   {
      this.pageId2 = pageId2;
   }

   public String getType2()
   {
      return type2;
   }

   public void setType2(String type2)
   {
      this.type2 = type2;
   }

   public String getLangName2()
   {
      return langName2;
   }

   public void setLangName2(String langName2)
   {
      this.langName2 = langName2;
   }

   public String getPageId3()
   {
      return pageId3;
   }

   public void setPageId3(String pageId3)
   {
      this.pageId3 = pageId3;
   }

   public String getType3()
   {
      return type3;
   }

   public void setType3(String type3)
   {
      this.type3 = type3;
   }

   public String getLangName3()
   {
      return langName3;
   }

   public void setLangName3(String langName3)
   {
      this.langName3 = langName3;
   }

   public String getPageId4()
   {
      return pageId4;
   }

   public void setPageId4(String pageId4)
   {
      this.pageId4 = pageId4;
   }

   public String getType4()
   {
      return type4;
   }

   public void setType4(String type4)
   {
      this.type4 = type4;
   }

   public String getLangName4()
   {
      return langName4;
   }

   public void setLangName4(String langName4)
   {
      this.langName4 = langName4;
   }

   public String getPageId5()
   {
      return pageId5;
   }

   public void setPageId5(String pageId5)
   {
      this.pageId5 = pageId5;
   }

   public String getType5()
   {
      return type5;
   }

   public void setType5(String type5)
   {
      this.type5 = type5;
   }

   public String getLangName5()
   {
      return langName5;
   }

   public void setLangName5(String langName5)
   {
      this.langName5 = langName5;
   }
}
