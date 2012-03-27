package geonotes.view.xml;

import geonotes.data.model.Dish;
import geonotes.data.model.Store;
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
    public static void outputXml(Store aStore, List<Dish> aDishes, OutputStream aOutputStream) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("store");
            root.setAttribute("id", "store");
            root.setAttribute("storeName", aStore.getNote());
            doc.appendChild(root);
            if (aDishes!=null && aDishes.size()>0) {
                for (Dish dish:aDishes) {
                    Element child = doc.createElement("dish");
                    root.appendChild(child);
                    child.setAttribute("dishId", Long.toString(dish.getKey().getId()));
                    child.setAttribute("yes", dish.getYesVote().toString());
                    child.setAttribute("dishText", dish.getNote());
                    child.setAttribute("lastReviewText", dish.getLastReview());
                    child.setAttribute("lastReviewUserId", dish.getLastReviewUserId());
                    String hasImage=null;
                    String lastReviewImageId=null;
                    if (dish.getLastReviewImageId()!=null && dish.getLastReviewImageId()!=0l) {
                        hasImage="true";
                        lastReviewImageId=dish.getLastReviewImageId().toString();
                    } else {
                        hasImage="false";
                        lastReviewImageId="";
                    }
                    child.setAttribute("img", hasImage);
                    child.setAttribute("lastReviewImageId", lastReviewImageId);
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