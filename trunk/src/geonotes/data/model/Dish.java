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
    private String lastReview;	

    @Persistent
    private String lastReviewUserId;	

    @Persistent
    private Long lastReviewImageId;

    @Persistent
    private Date lastUpdateTime;	

    @Persistent
    private String note;

    @Persistent
    private String noteLowerCase;

    @Persistent
    private long reviewCount;

    @Persistent
    private long storeId;

    @Persistent
    private String user;

    @Persistent
    private Long vote;

    @Persistent
    private Long yesVote;

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