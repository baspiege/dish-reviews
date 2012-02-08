package geonotes.controller;

import geonotes.data.StoreGetAll;
import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import java.util.ArrayList;
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
* Get stores in XML format.
*/
public class StoresXmlServlet extends HttpServlet {

    /**
    * Get data.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResourceBundle bundle = ResourceBundle.getBundle("Text");
        List<Store> stores = null;

        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
        if (storeId!=null) {
             Store store=RequestUtils.getStore(storeId);;
             // Add to list.
             stores=new ArrayList<Store>();
             stores.add(store);
        } else {
            Double latitude=RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
            Double longitude=RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
            stores=StoreGetAll.execute(latitude,longitude);
        }

        outputXml(stores,response);
    }
    
    /**
    * Out data in XML format.
    */
    private void outputXml(List<Store> stores, HttpServletResponse response) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("stores");
            doc.appendChild(root);
            if (stores!=null && stores.size()>0) {
                for (Store store:stores) {
                    Element child = doc.createElement("store");
                    root.appendChild(child);
                    child.setAttribute("storeId", new Long(store.getKey().getId()).toString() );
                    child.setAttribute("lat", store.getLatitude().toString());
                    child.setAttribute("lon", store.getLongitude().toString());
                    child.setAttribute("text", store.getNote());
                    child.setAttribute("dishCount", new Long(store.getDishCount()).toString());
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