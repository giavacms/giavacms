package org.giavacms.paypal.controller.request;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.paypal.controller.session.ShoppingCartSessionController;
import org.giavacms.paypal.model.ShoppingCart;

/**
 * 
 * <form action="" method="post"> <h:outputText value="#{contactUsRequestController.returnMessage}"/> <br/>
 * Nome: <input name="name" type="text"/> <br/>
 * Email: <input name="email" type="text"/> <br/>
 * Message: <textarea name="message" cols="40" rows="6"></textarea> <input type="submit" name="submit" value="invia"/>
 * </form>
 * 
 * @author pisi79
 * 
 */
@Named
@RequestScoped
public class ShoppingCartRequestController extends
         AbstractRequestController<ShoppingCart> implements Serializable
{

   private static final long serialVersionUID = 1L;

   public static final String PARAM_ID_PRODUCT = "idProduct";
   public static final String PARAM_DESCRIPTION = "description";
   public static final String PARAM_PRICE = "price";
   public static final String PARAM_QUANTITY = "quantity";
   public static final String PARAM_VAT = "vat";
   public static final String[] PARAM_NAMES = new String[] { PARAM_ID_PRODUCT, PARAM_DESCRIPTION, PARAM_PRICE,
            PARAM_QUANTITY, PARAM_VAT };
   public static final String ID_PARAM = "unused_idParam";
   public static final String CURRENT_PAGE_PARAM = "unused_currentpage";

   @Inject
   ShoppingCartSessionController shoppingCartSessionController;

   public ShoppingCartRequestController()
   {
      super();
   }

   @Override
   public String[] getParamNames()
   {
      return PARAM_NAMES;
   }

   public void add()
   {
      if (params.get(PARAM_ID_PRODUCT) != null &&
               params.get(PARAM_DESCRIPTION) != null && params.get(PARAM_PRICE) != null &&
               params.get(PARAM_QUANTITY) != null && params.get(PARAM_VAT) != null)
      {
         String requestURI = ((HttpServletRequest) FacesContext.getCurrentInstance()
                  .getExternalContext().getRequest()).getRequestURI();
         logger.info(requestURI);
         shoppingCartSessionController.setLastPage(requestURI.replace("db:", "p/").replace(".jsf", ""));
         logger.info("LAST PAGE: " + shoppingCartSessionController.getLastPage());

         shoppingCartSessionController.addProduct(params.get(PARAM_VAT), params.get(PARAM_PRICE),
                  params.get(PARAM_ID_PRODUCT), params.get(PARAM_DESCRIPTION),
                  Integer.valueOf(params.get(PARAM_QUANTITY)));
      }
   }

   @Override
   public String getCurrentPageParam()
   {
      return null;
   }

   @Override
   protected String getIdParam()
   {
      return "id";
   }

}
