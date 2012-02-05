package geonotes.controller;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import geonotes.data.ReviewGetSingle;
import geonotes.data.ReviewImageRemove;
import geonotes.data.ReviewImageUpdate;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;
import geonotes.utils.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
* Process image updates.
*/
public class ReviewImageServlet extends HttpServlet {

    /**
    * Display the page.  Everyone can see images.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_IMAGE);
    }

    /**
    * Update or delete images. Only review owners can update their own images.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);

        // Check ownerhip
        Review review=(Review)request.getAttribute(RequestUtils.REVIEW);
        boolean usersOwnReview=request.getUserPrincipal().getName().equalsIgnoreCase(review.user);
        if (!usersOwnReview) {
            throw new SecurityException("Review not own: " + review.getKey().getId());
        }

        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("uploadLabel")) && ServletFileUpload.isMultipartContent(request)) {
                Blob imageBlob=null;
                Blob imageBlobThumbnail=null;
                try {
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
                        imageBlob=new Blob(newImageData);
                        // Thumbnail
                        byte[] oldImageDataThumbnail=baos.toByteArray();
                        Image oldImageThumbnail = ImagesServiceFactory.makeImage(oldImageDataThumbnail);
                        int newPercent=10000/oldImageThumbnail.getHeight();
                        Transform resizeThumbnail = ImagesServiceFactory.makeResize(100, (oldImageThumbnail.getWidth() * newPercent)/100);
                        Image newImageThumbnail = imagesService.applyTransform(resizeThumbnail, oldImageThumbnail);
                        byte[] newImageDataThumbnail = newImageThumbnail.getImageData();
                        imageBlobThumbnail=new Blob(newImageDataThumbnail);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // Process if no edits
                if (!RequestUtils.hasEdits(request)) {
                    ReviewImageUpdate.execute(review, imageBlob, imageBlobThumbnail);
                }
            } else if (action.equals(bundle.getString("removeLabel"))) {		
                // Remove an image
                if (!RequestUtils.hasEdits(request)) {
                    ReviewImageRemove.execute(review);
                }
            }
        }

        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("dishId",review.dishId);
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_IMAGE);
        }
    }

    /**
    * Set-up the data.
    */
    private void setUpData(HttpServletRequest request) {

        // Get review
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",true);
        Review review=null;
        if (reviewId!=null) {
            review=ReviewGetSingle.execute(reviewId);
            request.setAttribute(RequestUtils.REVIEW, review);
        }
        if (review==null) {
            throw new RuntimeException("Review not found: " + reviewId);
        }

        request.setAttribute(RequestUtils.REVIEW, review);
    }
}