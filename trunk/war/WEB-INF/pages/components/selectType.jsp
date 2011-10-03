<%-- This JSP creates the select options for type. --%>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
%>
<select id='type' name='type' title='<%=bundle.getString("typeLabel")%>'>
<%
    Long typeSelected=(Long)request.getAttribute("type");
 
    // Get order
    String displayOrderProperty=bundle.getString("displayOrderType");
    String typeOrderString[] = displayOrderProperty.split(",");
    List<Integer> typeOrder=new ArrayList<Integer>();
    for (int i=0;i<typeOrderString.length;i++) {
        typeOrder.add( new Integer(typeOrderString[i]) );
    }
                    
    // Spin through list
    for (Integer currentType: typeOrder) {
        String value="";
        String key="type_" + currentType.toString();
        value=bundle.getString(key);
        out.write("<option");
        // Selected
        if (typeSelected!=null && typeSelected.intValue()==currentType.intValue())
        {
            out.write(" selected=\"true\"");
        }
        out.write(" value=\"" + currentType.toString() + "\">");
        out.write( HtmlUtils.escapeChars(value) );
        out.write("</option>");
    }
%>
</select>