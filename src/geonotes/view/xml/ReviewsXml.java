package geonotes.view.xml;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.Store;
import geonotes.utils.MemCacheUtils;
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
* Get reviews in XML format.
*/
public class ReviewsXml {

    /**
    * Out data in XML format.
    *
    * @param aReviews list of reviews
    * @param aUser to check
    * @param aIncludeStoreDishDetails boolean to indicate if store and dish info is included in output
    * @param aOutputStream output stream to write to
    */
    public static void outputXml(List<Review> aReviews, String aUser, boolean aIncludeStoreDishDetails, OutputStream aOutputStream) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("dishes");
            doc.appendChild(root);
            if (aReviews!=null && aReviews.size()>0) {
                for (Review review:aReviews) {
                    Element child = doc.createElement("review");
                    root.appendChild(child);
                    child.setAttribute("reviewId", new Long(review.getKey().getId()).toString());
                    child.setAttribute("yes", new Long(review.getYesVote()).toString());
                    child.setAttribute("text", review.getNote());
                    child.setAttribute("time", new Long(review.getLastUpdateTime().getTime()/1000).toString());
                    child.setAttribute("userId", review.getUser());
                    String hasImage=null;
                    if (review.getImageThumbnail()!=null) {
                        hasImage="true";
                    } else {
                        hasImage="false";
                    }
                    child.setAttribute("img", hasImage);
                    // User
                    String isUser="";
                    if (aUser!=null && aUser.equalsIgnoreCase(review.getUser())) {
                        isUser="true";
                    } else {
                        isUser="false";
                    }
                    child.setAttribute("user", isUser);
                    // Store and dish info
                    if (aIncludeStoreDishDetails) {
                        Dish dish=MemCacheUtils.getDish(review.getDishId());
                        child.setAttribute("dishId", new Long(dish.getKey().getId()).toString());
                        child.setAttribute("dishText", dish.getNote());
                        Store store=MemCacheUtils.getStore(dish.getStoreId());
                        child.setAttribute("storeId", new Long(store.getKey().getId()).toString());
                        child.setAttribute("storeText", store.getNote());
                    }
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