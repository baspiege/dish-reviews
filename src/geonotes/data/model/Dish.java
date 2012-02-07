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

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")

/**
 * Dish.
 *
 * @author Brian Spiegel
 */
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    public String lastReview;	

    @Persistent
    public String lastReviewUserId;	

    @Persistent
    public Long lastReviewImageId;

    @Persistent
    public Date lastUpdateTime;	

    @Persistent
    public String note;

    @Persistent
    public String noteLowerCase;

    @Persistent
    public long reviewCount;

    @Persistent
    public long storeId;

    @Persistent
    public String user;

    @Persistent
    public Long vote;

    @Persistent
    public Long yesVote;

    /**
     * Constructor.
     *
     */
    public Dish()
    {
    }

    public Key getKey() {
        return key;
    }
    
    public String getNote() {
        return note;
    }

    public String getLastReview() {
        return lastReview;
    }

    public String getLastReviewUserId() {
        return lastReviewUserId;
    }

    public long getLastReviewImageId() {
        return lastReviewImageId;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public long getStoreId() {
        return storeId;
    }

    public String getUser() {
        return user;
    }

    public long getYesVote() {
        return yesVote;
    }

    public void setNote(String aNote) {
        note=aNote;
        noteLowerCase=aNote.toLowerCase();
    }

    public void setLastReview(String aLastReview) {
        lastReview=aLastReview;
    }

    public void setLastReviewUserId(String aLastReviewUserId) {
        lastReviewUserId=aLastReviewUserId;
    }

    public void setLastReviewImageId(long aId) {
        lastReviewImageId=aId;
    }

    public void setLastUpdateTime(Date aLastUpdateTime) {
        lastUpdateTime=aLastUpdateTime;
    }

    public void setReviewCount(long aReviewCount) {
        reviewCount=aReviewCount;
    }

    public void setStoreId(long aStoreId) {
        storeId=aStoreId;
    }

    public void setUser(String aUser) {
        user=aUser;
    }

    public void setYesVote(long aYes) {
        yesVote=aYes;
    }
}