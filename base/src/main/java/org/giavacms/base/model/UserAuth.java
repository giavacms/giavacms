/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class UserAuth implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Long id;
   private String username;
   private String name;
   private String password;
   private List<UserRole> userRoles;

   private boolean newElement = false;
   private String oldPassword;
   private String newPassword;
   private String confirmPassword;
   private List<String> roles;
   private String role;
   private boolean admin;
   private boolean random;

   public UserAuth()
   {
      // TODO Auto-generated constructor stub
   }

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId()
   {
      return id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   @Transient
   public List<String> getRoles()
   {
      if (this.roles == null)
         this.roles = new ArrayList<String>();
      return roles;
   }

   public void setRoles(List<String> roles)
   {
      this.roles = roles;
   }

   public void addRole(String role)
   {
      getRoles().add(role);
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   @Transient
   public String getRoleNames()
   {
      StringBuffer roles = new StringBuffer();
      for (UserRole role : getUserRoles())
      {
         roles.append("," + role.getRoleName());
      }
      return roles.length() > 0 ? roles.toString().substring(1) : "";
   }

   @Transient
   public boolean isNewElement()
   {
      return newElement;
   }

   public void setNewElement(boolean newElement)
   {
      this.newElement = newElement;
   }

   @Transient
   public String getOldPassword()
   {
      return oldPassword;
   }

   public void setOldPassword(String oldPassword)
   {
      this.oldPassword = oldPassword;
   }

   @Transient
   public String getNewPassword()
   {
      return newPassword;
   }

   public void setNewPassword(String newPassword)
   {
      this.newPassword = newPassword;
   }

   @Transient
   public String getConfirmPassword()
   {
      return confirmPassword;
   }

   public void setConfirmPassword(String confirmPassword)
   {
      this.confirmPassword = confirmPassword;
   }

   @Transient
   public boolean isAdmin()
   {
      return admin;
   }

   public void setAdmin(boolean admin)
   {
      this.admin = admin;
   }

   @Transient
   public void verifyIfAdmin()
   {
      if (getRoleNames().contains("admin"))
      {
         setAdmin(true);
      }
      else
      {
         setAdmin(false);

      }
   }

   @Transient
   public boolean isRandom()
   {
      return random;
   }

   public void setRandom(boolean random)
   {
      this.random = random;
   }

   @OneToMany(mappedBy = "userAuth", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
   public List<UserRole> getUserRoles()
   {
      if (this.userRoles == null)
      {
         this.userRoles = new ArrayList<UserRole>();
      }
      return this.userRoles;
   }

   public void setUserRoles(List<UserRole> userRoles)
   {
      this.userRoles = userRoles;
   }

   public void addUserRole(UserRole userRole)
   {
      getUserRoles().add(userRole);
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @Override
   public String toString()
   {
      return "UserAuth [id=" + id + ", username=" + username + ", name="
               + name + ", password=" + password + ", userRoles=" + userRoles
               + ", newElement=" + newElement + ", oldPassword=" + oldPassword
               + ", newPassword=" + newPassword + ", confirmPassword="
               + confirmPassword + ", roles=" + roles + ", admin=" + admin
               + ", random=" + random + "]";
   }

   @Transient
   public String getRole()
   {
      return role;
   }

   public void setRole(String role)
   {
      this.role = role;
   }

}
