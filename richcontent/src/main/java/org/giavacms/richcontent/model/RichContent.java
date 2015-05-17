package org.giavacms.richcontent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.giavacms.api.annotation.Active;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = RichContent.TABLE_NAME)
@XmlRootElement
public class RichContent implements Serializable
{

   private static final long serialVersionUID = 1L;
   public static final String TABLE_NAME = "RichContent";
   public static final String TABLE_FK = "RichContent_id";
   public static final String DOCUMENTS_JOINTABLE_NAME = "RichContent_Document";
   public static final String DOCUMENT_FK = "documents_id";
   public static final String IMAGES_JOINTABLE_NAME = "RichContent_Image";
   public static final String IMAGE_FK = "images_id";
   public static final String TAG_SEPARATOR = ",";

   private String id;
   private String title;
   private String preview;
   private String content;
   private String author;
   private Date date;
   private RichContentType richContentType;
   private List<Document> documents;
   private List<Image> images;
   private boolean highlight;
   private String tag;
   private List<String> tagList;
   private String tags;
   boolean active = true;
   private String language;

   public RichContent()
   {
      super();
   }

   @Id
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getLanguage()
   {
      return language;
   }

   public void setLanguage(String language)
   {
      this.language = language;
   }

   @JsonIgnore
   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = DOCUMENTS_JOINTABLE_NAME, joinColumns = @JoinColumn(name = TABLE_FK), inverseJoinColumns = @JoinColumn(name = DOCUMENT_FK))
   public List<Document> getDocuments()
   {
      if (this.documents == null)
         this.documents = new ArrayList<Document>();
      return documents;
   }

   public void setDocuments(List<Document> documents)
   {
      this.documents = documents;
   }

   public void addDocument(Document document)
   {
      getDocuments().add(document);
   }

   @Transient
   @JsonIgnore
   public int getDocSize()
   {
      return getDocuments().size();
   }

   @JsonIgnore
   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = IMAGES_JOINTABLE_NAME, joinColumns = @JoinColumn(name = TABLE_FK), inverseJoinColumns = @JoinColumn(name = IMAGE_FK))
   public List<Image> getImages()
   {
      if (this.images == null)
         this.images = new ArrayList<Image>();
      return images;
   }

   @Transient
   @JsonIgnore
   public Image getImage()
   {
      if (getImages() != null && getImages().size() > 0)
         return getImages().get(0);
      return null;
   }

   public void setImages(List<Image> images)
   {
      this.images = images;
   }

   public void addImage(Image image)
   {
      getImages().add(image);
   }

   @Transient
   @JsonIgnore
   public int getImgSize()
   {
      return getImages().size();
   }

   @ManyToOne
   public RichContentType getRichContentType()
   {
      if (richContentType == null)
         richContentType = new RichContentType();
      return richContentType;
   }

   public void setRichContentType(RichContentType richContentType)
   {
      this.richContentType = richContentType;
   }

   public boolean isHighlight()
   {
      return highlight;
   }

   public void setHighlight(boolean highlight)
   {
      this.highlight = highlight;
   }

   @Lob
   public String getPreview()
   {
      return preview;
   }

   public void setPreview(String preview)
   {
      this.preview = preview;
   }

   @Lob
   public String getContent()
   {
      return content;
   }

   public void setContent(String content)
   {
      this.content = content;
   }

   public String getAuthor()
   {
      return author;
   }

   public void setAuthor(String author)
   {
      this.author = author;
   }

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }

   public String getTags()
   {
      return tags;
   }

   public void setTags(String tags)
   {
      this.tags = tags;
      this.tagList = null;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   @Active
   public boolean isActive()
   {
      return active;
   }

   public void setActive(boolean active)
   {
      this.active = active;
   }

   @Transient
   @JsonIgnore
   public String getTag()
   {
      return tag;
   }

   public void setTag(String tag)
   {
      this.tag = tag;
   }

   @Transient
   @JsonIgnore
   public List<String> getTagList()
   {
      if (tagList != null)
      {
         return tagList;
      }
      tagList = new ArrayList<String>();
      if (tags == null)
      {
         return tagList;
      }
      String[] tagArray = tags.split(TAG_SEPARATOR);
      for (String tag : tagArray)
      {
         if (tag != null && tag.trim().length() > 0)
         {
            tagList.add(tag.trim());
         }
      }
      return tagList;
   }

   @Override
   public String toString()
   {
      return "RichContent{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", preview='" + preview + '\'' +
               ", content='" + content + '\'' +
               ", author='" + author + '\'' +
               ", date=" + date +
               ", richContentType=" + richContentType +
               ", documents=" + documents +
               ", images=" + images +
               ", highlight=" + highlight +
               ", tag='" + tag + '\'' +
               ", tagList=" + tagList +
               ", tags='" + tags + '\'' +
               ", active=" + active +
               ", language='" + language + '\'' +
               '}';
   }
}
