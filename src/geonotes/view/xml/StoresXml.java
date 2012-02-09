package geonotes.view.xml;

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
* Get stores in XML format.
*/
public class StoresXml {
    
    /**
    * Out data in XML format.
    */
    public static void outputXml(List<Store> aStores, OutputStream aOutputStream) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("stores");
            doc.appendChild(root);
            if (aStores!=null && aStores.size()>0) {
                for (Store store:aStores) {
                    Element child = doc.createElement("store");
                    root.appendChild(child);
                    child.setAttribute("storeId", new Long(store.getKey().getId()).toString() );
                    child.setAttribute("lat", store.getLatitude().toString());
                    child.setAttribute("lon", store.getLongitude().toString());
                    child.setAttribute("text", store.getNote());
                    child.setAttribute("dishCount", new Long(store.getDishCount()).toString());
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