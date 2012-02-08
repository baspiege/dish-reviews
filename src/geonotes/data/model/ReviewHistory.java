package geonotes.data.model;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import java.io.Serializable;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import geonotes.utils.NumberUtils;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")

/**
 * Review.
 *
 * @author Brian Spiegel
 */
public class ReviewHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long dishId;

    @Persistent
    private Boolean hasImage;

    @Persistent
    private Blob image;	

    @Persistent
    private Blob imageThumbnail;		

    @Persistent
    private Date lastUpdateTime;	

    @Persistent
    private String note;

    @Persistent
    private Long yesVote;

    @Persistent
    private String user;	

    /**
     * Constructor.
     *
     */
    public ReviewHistory() {
    }

    /**
     * Constructor.
     *
     */
    public ReviewHistory(Review aReview) {
        setNote(aReview.getNote());
        setLastUpdateTime(aReview.getLastUpdateTime());
        setDishId(aReview.getDishId());
        setYesVote(aReview.getYesVote());
        setUser(aReview.getUser());
        setImage(aReview.getImage());
        setImageThumbnail(aReview.getImageThumbnail());
        setHasImage(aReview.getHasImage());
    }

    public boolean getHasImage() {
        if (hasImage==null) {
            return false;
        } else {
            return hasImage.booleanValue();
        }
    }

    public Key getKey() {
        return key;
    }

    public void setDishId(long aDishId) {
        dishId=aDishId;
    }

    public void setHasImage(Boolean aHasImage) {
        hasImage=aHasImage;
    }

    public void setNote(String aNote) {
        note=aNote;
    }

    public void setImage(Blob aImage) {
        image=aImage;
    }

    public void setImageThumbnail(Blob aImage) {
        imageThumbnail=aImage;
    }

    public void setLastUpdateTime(Date aLastUpdateTime) {
        lastUpdateTime=aLastUpdateTime;
    }

    public void setYesVote(long aYes) {
        yesVote=aYes;
    }

    public void setUser(String aUser) {
        user=aUser;
    }
}