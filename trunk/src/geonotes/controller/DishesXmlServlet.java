package geonotes.controller;

import geonotes.data.DishesGetAll;
import geonotes.data.model.Dish;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
* Get dishes in XML format.
*/
public class DishesXmlServlet extends HttpServlet {

    /**
    * Get data.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Fields
        Long storeId=RequestUtils.getNumericInput(request,"storeId",bundle.getString("storeId"),true);
        Long start=RequestUtils.getNumericInput(request,"start",bundle.getString("startLabel"),true);
        String sortBy=RequestUtils.getAlphaInput(request,"sortBy",bundle.getString("sortByLabel"),false);

        // Get data
        List<Dish> dishes=DishesGetAll.execute(storeId, start, sortBy);
        request.setAttribute("dishes", dishes);

        outputXml(dishes,response);
    }
    
    /**
    * Out data in XML format.
    */
    private void outputXml(List<Dish> dishes, HttpServletResponse response) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("dishes");
            doc.appendChild(root);
            if (stores!=null && stores.size()>0) {
                for (Store store:stores) {
                    Element child = doc.createElement("dish");
                    root.appendChild(child);
                    child.setAttribute("dishId", new Long(dish.getKey().getId()).toString() );
                    child.setAttribute("yes", new Long(dish.getYesVote()).toString());
                    child.setAttribute("lastReviewText", dish.getLastReview());
                    child.setAttribute("lastReviewUserId", new Long(dish.getLastReviewUserId()).toString() );
                    child.setAttribute("lastReviewImageId", new Long(dish.getLastReviewImageId()).toString() );
                    String hasImage=null;
                    if (dish.getLastReviewImageId()!=null && dish.getLastReviewImageId()!=0l) {
                        hasImage="true";
                    } else {
                        hasImage="false";
                    }    
                    child.setAttribute("lastReviewImageId", hasImage );
                }
            }
            response.setHeader("Content-Type", "text/xml; charset=UTF-8");
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
    }
}