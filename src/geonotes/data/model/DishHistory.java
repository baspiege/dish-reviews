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
 * Dish history.
 *
 * @author Brian Spiegel
 */
public class DishHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long dishId;

    @Persistent
    private String lastReview;	

    @Persistent
    private Date lastUpdateTime;	

    @Persistent
    private String note;

    @Persistent
    private long reviewCount;

    @Persistent
    private long storeId;

    @Persistent
    private String user;

    @Persistent
    private long yesVote;

    /**
     * Constructor.
     *
     */
    public DishHistory() {
    }

    /**
     * Constructor.
     *
     */
    public DishHistory(Dish aDish) {
        setDishId(aDish.getKey().getId());
        setNote(aDish.getNote());
        setLastUpdateTime(aDish.getLastUpdateTime());
        setStoreId(aDish.getStoreId());
        setYesVote(aDish.getYesVote());
        setUser(aDish.getUser());
    }

    public Key getKey() {
        return key;
    }

    public void setDishId(long aDishId) {
        dishId=aDishId;
    }


    public void setNote(String aNote) {
        note=aNote;
    }

    public void setLastReview(String aLastReview) {
        lastReview=aLastReview;
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