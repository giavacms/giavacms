package org.giavacms.chalet.management;

import java.security.Identity;

public class AppConstants extends org.giavacms.base.management.AppConstants
{

   public static final String CHALET_PATH = "/chalets";
   public static final String FREETICKET_PATH = "/freetickets";
   public static final String PARADE_PATH = "/parades";
   public static final String NOTIFICATION_PATH = "/notifications";
   public static final String PHOTO_PATH = "/photos";
   public static final String TAG_PATH = "/tags";
   public static final String QRCODE_PATH = "/qr";

   public static final String PHOTO_FOLDER = "photo";

   public static final String QUEUE_NOTIFICATION_SENDER = "java:/jms/queue/giavacms.parade";
   public static final String QUEUE_RESIZE_IMAGE = "java:/jms/queue/giavacms.resizeimage";


   public static final String RS_MSG = "msg";

   public static final String ER7 = "ER7 - Chalet not exist ";
   public static final String ER8 = "ER8 - Account not exist ";
   public static final String ER9 = "ER9 - Photo not exist ";
   public static final String ER10 = "ER10 - The account isn't the owner of photo  ";
   public static final String ER11 = "ER11 - You are not authorized to do this";
   
   public static final String ROLE_ADMIN = "ADMIN";
   public static final String ROLE_SUPERVISOR = "SUPERVISOR";

}