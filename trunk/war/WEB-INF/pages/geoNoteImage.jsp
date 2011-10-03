<%-- This JSP has the HTML for Geo Note image page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%> 
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.GeoNoteGetSingle" %>
<%@ page import="geonotes.data.GeoNoteImageRemove" %>
<%@ page import="geonotes.data.GeoNoteImageUpdate" %>
<%@ page import="geonotes.data.model.GeoNote" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%@ page import="org.apache.commons.fileupload.FileItemStream" %>
<%@ page import="org.apache.commons.fileupload.FileItemIterator" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="com.google.appengine.api.datastore.Blob" %>
<%@ page import="com.google.appengine.api.images.Image" %>
<%@ page import="com.google.appengine.api.images.ImagesService" %>
<%@ page import="com.google.appengine.api.images.ImagesServiceFactory" %>
<%@ page import="com.google.appengine.api.images.Transform" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!=null;

    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Long geoNoteId=RequestUtils.getNumericInput(request,"id","id",true);
    
    GeoNote geoNote=null;
    if (geoNoteId!=null) {
        new GeoNoteGetSingle().execute(request);
         
        // If note is null, forward to main page
        geoNote=(GeoNote)request.getAttribute("geoNote");
        if (geoNote==null) {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/geoNotesRedirect.jsp"/>
            <%
        }
        
        // Can only edit own note
        if (isSignedIn) {
            isSignedIn=request.getUserPrincipal().getName().equalsIgnoreCase(geoNote.user);
        }
    } else {    
        RequestUtils.resetAction(request);
        RequestUtils.removeEdits(request);
        %>
        <jsp:forward page="/geoNotesRedirect.jsp"/>
        <%
    }

    // Process based on action
    if (!StringUtils.isEmpty(action) && isSignedIn) {
        if (action.equals("Upload") && ServletFileUpload.isMultipartContent(request)) {		
            ServletFileUpload upload = new ServletFileUpload();             
            FileItemIterator iter = upload.getItemIterator(request);
            FileItemStream imageItem = iter.next();
            InputStream imgStream = imageItem.openStream();
            // Get stream
            int len;
            byte[] buffer = new byte[8192];
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            while ((len = imgStream.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            if (baos.size()>0){
                // Main image
                byte[] oldImageData=baos.toByteArray();
                ImagesService imagesService = ImagesServiceFactory.getImagesService();
                Image oldImage = ImagesServiceFactory.makeImage(oldImageData);
                int newPercent1=60000/oldImage.getHeight();
                Transform resize = ImagesServiceFactory.makeResize(600, (oldImage.getWidth() * newPercent1)/100);
                Image newImage = imagesService.applyTransform(resize, oldImage);
                byte[] newImageData = newImage.getImageData();
                Blob imageBlob=new Blob(newImageData);
                request.setAttribute("image",imageBlob);
                // Thumbnail
                byte[] oldImageDataThumbnail=baos.toByteArray();
                Image oldImageThumbnail = ImagesServiceFactory.makeImage(oldImageDataThumbnail);
                int newPercent=10000/oldImageThumbnail.getHeight();
                Transform resizeThumbnail = ImagesServiceFactory.makeResize(100, (oldImageThumbnail.getWidth() * newPercent)/100);
                Image newImageThumbnail = imagesService.applyTransform(resizeThumbnail, oldImageThumbnail);
                byte[] newImageDataThumbnail = newImageThumbnail.getImageData();
                Blob imageBlobThumbnail=new Blob(newImageDataThumbnail);
                request.setAttribute("imageThumbnail",imageBlobThumbnail);
                // Process if no edits
                if (!RequestUtils.hasEdits(request)) {
                    new GeoNoteImageUpdate().execute(request);
                    RequestUtils.resetAction(request);
                    %>
                    <jsp:forward page="/geoNotesRedirect.jsp"/>
                    <%
                }
            }
        } else if (action.equals(bundle.getString("removeLabel"))) {		
            // Remove an image
            if (!RequestUtils.hasEdits(request)) {
                new GeoNoteImageRemove().execute(request);
                RequestUtils.resetAction(request);
                %>
                <jsp:forward page="/geoNotesRedirect.jsp"/>
                <%
            }
        } else {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/geoNotesRedirect.jsp"/>
            <%
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("imageLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<style>
form {margin: 0px 0px 0px 0px; display: inline;}
</style>
</head>
<body>
<% if (geoNote!=null && geoNote.image!=null) { %>
<img src="geoNoteImage?id=<%=new Long(geoNote.getKey().getId()).toString()%>" alt="<%=bundle.getString("altPictureLabel")%>"/> <br/>
<% } %>
<%-- Signed In --%>
<% if (isSignedIn) { %>
<form method="post" enctype="multipart/form-data" action="geoNoteImage.jsp?action=Upload&id=<%=new Long(geoNote.getKey().getId()).toString()%>"> 
<input style="margin-bottom:1.5em" type="file" name="imageFile">
<br/>
<%-- Upload --%>
<input class="button" type="submit" name="action" value="Upload">
</form>
<form method="post" action="geoNoteImage.jsp?id=<%=new Long(geoNote.getKey().getId()).toString()%>" autocomplete="off">
<%-- Remove --%>
<input type="submit" name="action" value="Remove">
</form>
<% } %>  
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>