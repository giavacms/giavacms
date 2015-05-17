package org.giavacms.api.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Id;

import org.giavacms.api.annotation.Active;

public class RepositoryUtils
{

   private static final String LONG = "java.lang.Long";

   public static String getActiveFieldName(@SuppressWarnings("rawtypes") Class clazz) throws Exception
   {
      return findFieldOrMethodName(clazz, Active.class);
   }

   private static String findFieldOrMethodName(Class<?> clazz, @SuppressWarnings("rawtypes") Class annotationClass)
   {
      for (Field field : clazz.getDeclaredFields())
      {
         Annotation[] annotations = field.getDeclaredAnnotations();
         for (Annotation annotation : annotations)
         {
            if (annotation.annotationType().equals(annotationClass))
            {
               field.setAccessible(true);
               return field.getName();
            }
         }
      }
      for (Method method : clazz.getDeclaredMethods())
      {
         Annotation[] annotations = method.getDeclaredAnnotations();
         for (Annotation annotation : annotations)
         {
            if (annotation.annotationType().equals(annotationClass))
            {
               int index = 0;
               if (method.getName().startsWith("is"))
               {
                  index = method.getName().indexOf("is") + 2;
               }
               else
               {
                  index = method.getName().indexOf("get") + 3;
               }
               return method.getName().substring(index, index + 1).toLowerCase()
                        + method.getName().substring(index + 1);
            }
         }
      }

      // cerco anche nella classe padre se ce n'è una
      if (clazz.getGenericSuperclass() != null)
      {
         return findFieldOrMethodName(clazz.getSuperclass(), annotationClass);
      }

      return null;
   }

   public static String getIdFieldName(@SuppressWarnings("rawtypes") Class clazz) throws Exception
   {
      String idFieldName = findFieldOrMethodName(clazz, Id.class);
      if (idFieldName != null)
      {
         return idFieldName;
      }
      throw new Exception("not found field/method with @Id org.giavacms.web.annotation");
   }

   public static Class<?> getIdFieldClass(@SuppressWarnings("rawtypes") Object t)
   {
      return (Class<?>) findFieldOrMethodValueOrType(t.getClass(), t, javax.persistence.Id.class, false);
   }

   public static java.lang.Object getId(Object t)
   {

      return findFieldOrMethodValueOrType(t.getClass(), t, javax.persistence.Id.class, true);
   }

   private static <U> Object findFieldOrMethodValueOrType(Class<?> clazz, U u, Class annotationClass,
            boolean returnValue)
   {
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields)
      {
         Object id_anno = field.getAnnotation(annotationClass);
         // javax.persistence.Id.class);
         if (id_anno != null)
         {
            try
            {
               field.setAccessible(true);
               if (returnValue)
               {
                  return field.get(u);
               }
               else
               {
                  return field.getType();
               }
            }
            catch (Exception e)
            {
               return null;
            }
         }
      }

      for (Method method : clazz.getDeclaredMethods())
      {
         Object id_anno = method.getAnnotation(annotationClass);
         if (id_anno != null)
         {
            try
            {
               method.setAccessible(true);
               if (returnValue)
               {
                  return method.invoke(u);
               }
               else
               {

                  return method.getReturnType();
               }
            }
            catch (Exception e)
            {
               return null;
            }

         }
      }
      // cerco anche nella classe padre se ce n'è una
      if (clazz.getGenericSuperclass() != null)
      {
         return findFieldOrMethodValueOrType(clazz.getSuperclass(), u, annotationClass, returnValue);
      }

      return null;
   }

   public static Object castId(String key, @SuppressWarnings("rawtypes") Class clazz) throws Exception
   {
      @SuppressWarnings("rawtypes")
      Class idField = RepositoryUtils.getIdFieldClass(clazz.newInstance());
      switch (idField.getCanonicalName())
      {
      case LONG:
         return Long.parseLong((String) key);
      default:
         return key;
      }
   }

   public static <U> void setFieldByName(Class<?> clazz, U instance, String fieldName, String fieldValue)
            throws Exception
   {
      if (fieldName == null || fieldName.trim().isEmpty())
      {
         return;
      }

      fieldName = fieldName.trim();

      if (fieldName.contains("."))
      {
         int indexOfDot = fieldName.indexOf(".");
         String subFieldName = fieldName.substring(0, indexOfDot);
         Object subFieldInstance = getFieldByName(clazz, instance, subFieldName);
         if (subFieldInstance != null)
         {
            setFieldByName(subFieldInstance.getClass(), subFieldInstance, fieldName.substring(indexOfDot + 1),
                     fieldValue);
         }
         else
         {
            return;
         }
      }

      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields)
      {
         if (field.getName().equals(fieldName))
         {
            field.setAccessible(true);
            field.set(instance, valueOf(field.getType(), fieldValue));
            return;
         }
      }

      String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + (fieldName.length() == 1 ?
               "" :
               fieldName.substring(1));
      for (Method method : clazz.getDeclaredMethods())
      {
         if (method.getName().equals(methodName))
         {
            method.setAccessible(true);
            method.invoke(instance, valueOf(method.getParameterTypes()[0], fieldValue));
            return;
         }
      }

      // cerco anche nella classe padre se ce n'è una
      if (clazz.getSuperclass() != null && !clazz.getSuperclass().getClass().equals(Object.class))
      {
         setFieldByName(clazz.getSuperclass(), instance, fieldName, fieldValue);
      }

      return;
   }

   public static <U> Object getFieldByName(Class<?> clazz, U instance, String fieldName) throws Exception
   {
      if (fieldName == null || fieldName.trim().isEmpty())
      {
         return null;
      }

      fieldName = fieldName.trim();

      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields)
      {
         if (field.getName().equals(fieldName))
         {
            field.setAccessible(true);
            Object fieldInstance = field.get(instance);
            if (fieldInstance == null)
            {
               fieldInstance = field.getType().newInstance();
               field.set(instance, fieldInstance);
            }
            return fieldInstance;

         }
      }

      // cerco anche nella classe padre se ce n'è una
      if (clazz.getSuperclass() != null && !clazz.getSuperclass().getClass().equals(Object.class))

      {
         return getFieldByName(clazz.getSuperclass(), instance, fieldName);
      }

      return null;

   }

   private static Object valueOf(Class<?> aClass, String fieldValue)
   {
      if (fieldValue == null)
      {
         return null;
      }
      if (aClass.equals(Integer.class))
      {
         return new Integer(fieldValue);
      }
      if (aClass.equals(Long.class))
      {
         return new Long(fieldValue);
      }
      if (aClass.equals(BigDecimal.class))
      {
         return new BigDecimal(fieldValue);
      }
      if (aClass.equals(Boolean.class))
      {
         return new Boolean(fieldValue);
      }
      if (aClass.equals(Date.class))
      {
         try
         {
            return new SimpleDateFormat("dd/MM/yyyy").parse(fieldValue);
         }
         catch (Exception e)
         {
         }
         try
         {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fieldValue);
         }
         catch (Exception e)
         {
         }
         try
         {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(fieldValue);
         }
         catch (Exception e)
         {
         }
      }
      if (aClass.isEnum())
      {
         for (Object enumConstant : aClass.getEnumConstants())
         {
            if (enumConstant.toString().equals(fieldValue))
            {
               return enumConstant;
            }
         }
      }
      return fieldValue;
   }
}
