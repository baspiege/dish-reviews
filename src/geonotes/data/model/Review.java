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
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    public long dishId;

    @Persistent
    public Boolean hasImage;

    @Persistent
    public Blob image;	

    @Persistent
    public Blob imageThumbnail;		

    @Persistent
    public Date lastUpdateTime;	

    @Persistent
    public String note;

    @Persistent
    public Long yes;

    @Persistent
    public Long yesVote;

    @Persistent
    public String user;	

    /**
     * Constructor.
     *
     */
    public Review() {
    }

    public boolean getHasImage() {
        if (hasImage==null) {
            return false;
        } else {
            return hasImage.booleanValue();
        }
    }
        
    public long getDishId() {
        return dishId;
    }
    
    public Key getKey() {
        return key;
    }

    public String getNote() {
        return note;
    }

    public Blob getImage() {
        return image;
    }

    public Blob getImageThumbnail() {
        return imageThumbnail;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public long getYes() {
        return yes;
    }

    public long getYesVote() {
        return yesVote;
    }
    
    public String getUser() {
        return user;
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

    public void setYes(long aYes) {
        yes=aYes;
    }

    public void setYesVote(long aYes) {
        yesVote=aYes;
    }

    public void setUser(String aUser) {
        user=aUser;
    }
}