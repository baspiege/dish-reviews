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
 * Review vote.
 *
 * @author Brian Spiegel
 */
public class ReviewVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    public long reviewId;

    @Persistent
    public String user;

    /**
     * Constructor.
     *
     */
    public ReviewVote()
    {
    }

    public Key getKey()
    {
        return key;
    }

    public void setReviewId(long aReviewId)
    {
        reviewId=aReviewId;
    }

    public void setUser(String aUser)
    {
        user=aUser;
    }
}