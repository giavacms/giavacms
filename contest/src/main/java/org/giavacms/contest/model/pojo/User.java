package org.giavacms.contest.model.pojo;

import java.io.Serializable;

/**
 * Created by fiorenzo on 01/07/15.
 */
public class User implements Serializable
{
   private String name;
   private String surname;
   private String phone;
   private String preference1;
   private String preferenceName;
   private String voteUid;
   private int position;

   public User()
   {
   }

   public User(String name, String phone, String preference1, String surname, String voteUid)
   {
      this.name = name;
      this.phone = phone;
      this.preference1 = preference1;
      this.surname = surname;
      this.voteUid = voteUid;
   }

   public User(String name, String phone, String preference1, String surname, String preferenceName, String voteUid)
   {
      this.name = name;
      this.phone = phone;
      this.preference1 = preference1;
      this.surname = surname;
      this.preferenceName = preferenceName;
      this.voteUid = voteUid;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   public String getPreference1()
   {
      return preference1;
   }

   public void setPreference1(String preference1)
   {
      this.preference1 = preference1;
   }

   public String getPreferenceName()
   {
      return preferenceName;
   }

   public void setPreferenceName(String preferenceName)
   {
      this.preferenceName = preferenceName;
   }

   public String getSurname()
   {
      return surname;
   }

   public void setSurname(String surname)
   {
      this.surname = surname;
   }

   public String getVoteUid()
   {
      return voteUid;
   }

   public void setVoteUid(String voteUid)
   {
      this.voteUid = voteUid;
   }

   public int getPosition()
   {
      return position;
   }

   public void setPosition(int position)
   {
      this.position = position;
   }
}
