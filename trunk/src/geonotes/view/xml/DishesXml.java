package geonotes.view.xml;

import geonotes.data.model.Dish;
import java.io.OutputStream;
import java.util.List;
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
public class DishesXml {
    
    /**
    * Out data in XML format.
    */
    public static void outputXml(List<Dish> aDishes, OutputStream aOutputStream) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("dishes");
            doc.appendChild(root);
            if (aDishes!=null && aDishes.size()>0) {
                for (Dish dish:aDishes) {
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
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.transform(new DOMSource(doc), new StreamResult(aOutputStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}