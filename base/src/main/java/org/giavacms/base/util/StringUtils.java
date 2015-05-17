package org.giavacms.base.util;

import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.StringTokenizer;

public class StringUtils
{

   static Logger logger = Logger.getLogger(StringUtils.class);

   public static boolean isNullOrEmpty(String value)
   {
      if (value == null || value.trim().isEmpty())
         return true;
      return false;

   }

   public static void evaluateNullOrEmpty(String value, String name)
            throws Exception
   {
      if (value == null)
         throw new Exception(name + " is null");
      if (value.trim().isEmpty())
         throw new Exception(name + " is empty");
      logger.info(name + ": " + value);
   }

   /**
    * Return a String equal to string.trim() or null: it's equivalent to string.trim() but can't return a void String.
    *
    * @param string the String to be managed or <b>null</b>
    * @return If string is not void, not null and not equal to spaces return <b>string.trim()</b>, else return
    * <b>null</b>.
    */
   public static String stNul(String string)
   {
      if (string == null || string.trim().equals(""))
         return null;
      else
         return string.trim();
   }

   /**
    * Return a String equal to string.trim(): it's equivalent to string.trim() but accept null String.
    *
    * @param string the String to be managed or <b>null</b>
    * @return <b>string.trim()</b>, if string is equal to <b>null</b> return "" (void String).
    */
   public static String stZL(String string)
   {
      if (string == null)
         return "";
      else
         return string.trim();
   }

   /**
    * Return a String equal to string.trim(): it's equivalent to string.trim() but accept null String.
    *
    * @param string the String to be managed or <b>null</b>
    * @return <b>string.trim()</b>, if string is equal to <b>null</b> return " " (one 'space' String).
    */
   public static String stSp(String string)
   {
      if (stNul(string) == null)
         return " ";
      else
         return string.trim();
   }

   /**
    * Return a new String of desired length from given String, optionally aligned and padded with given char
    *
    * @param string the String to be managed or <b>null</b>
    * @param len    desired length
    * @param al     desired alignement: <b>R</b>: to right after truncation of leading and trailing spaces <b>L</b>: to left
    *               after truncation of leading and trailing spaces <b>N</b>: to left whithout any truncation <b>M</b>: to
    *               right whithout any truncation
    * @return new String obtained after requested operations
    */
   public static String stPadLen(String string, int len, char al, char pad)
   {
      if (string == null)
      {
         string = "";
      }
      if (al != 'N' && al != 'M')
      {
         string = string.trim();
      }

      if (al == 'N')
         al = 'L';
      if (al == 'M')
         al = 'R';

      if (string.length() == len)
      {
         return string;
      }
      if (string.length() > len)
      {
         if (al == 'R')
         {
            return string.substring(string.length() - len);
         }
         else
         {
            return string.substring(0, len);
         }
      }
      char chArr[] = new char[len - string.length()];
      Arrays.fill(chArr, pad);
      if (al == 'R')
      {
         return new String(chArr) + string;
      }
      else
      {
         return string + new String(chArr);
      }
   }

   /**
    * Return a String[] tokenized from the given String using separators <b>sep</b>.
    *
    * @param string the String to be tokenized. This String can be formatted: the "." separator will be ignored. If
    *               string represent a decimal value, its decimal part will be ignored
    * @return a int obtained parsing <b>string</b> or <b>0</b> if string is null
    * @throws java.lang.NumberFormatException if string can't be converted to int
    */
   public static String[] split(String string, String sep)
   {
      StringTokenizer strTok = new StringTokenizer(string, sep);
      String[] retStrArr = new String[strTok.countTokens()];
      for (int idx = 0; strTok.hasMoreTokens(); idx++)
      {
         retStrArr[idx] = strTok.nextToken();
      }
      return retStrArr;
   }

   public static String clean(String in)
   {
      if (in == null)
      {
         return "";
      }
      else
      {
         return in.replaceAll("[^a-zA-Z0-9\\s]", "")
                  .replaceAll("[\\s]", "-");
      }
   }

   /**
    * Sostituisce {@code input} con {@code replacement} se input è vuota.
    *
    * @param input
    * @param replacement
    * @return
    */
   public static String replaceIfEmpty(String input, String replacement)
   {
      if (StringUtils.isEmpty(input))
         return replacement;

      return input;
   }

   /**
    * Concatena la stringa 2 alla stringa 1.
    *
    * @param str1
    * @param str2
    * @return
    */
   public static String concat(String str1, String str2)
   {
      if (isEmpty(str1))
      {
         return str2;
      }

      return str1.concat(str2);
   }

   public static boolean isEmpty(String str1)
   {
      return str1 == null || str1.length() == 0;
   }

   /**
    * This removes characters that are invalid for xml encoding
    *
    * @param text Text to be encoded
    * @return Text with invalid xml characters removed
    */
   public static String cleanInvalidXmlChars(String in)
   {
      if (in == null)
      {
         return "";
      }
      else
      {
         return in.replaceAll("[^\\x09\\x0A\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-x10FFFF]", "");
      }
   }

   public static String trim(String src, int size)
   {
      if (src == null || src.length() <= size)
         return src;
      int pos = src.lastIndexOf(" ", size - 3);
      if (pos < 0)
         return src.substring(0, size);
      return src.substring(0, pos) + "...";
   }

   public static String substring(String str, int len, boolean dots)
   {
      if (len < 0)
      {
         return str;
      }
      if (str == null)
      {
         return null;
      }
      if (str.length() < len)
      {
         return str;
      }
      return str.substring(0, len) + (dots ? "..." : "");
   }

}
