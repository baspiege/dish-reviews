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
 * Dish vote.
 *
 * @author Brian Spiegel
 */
public class DishVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    public long dishId;

    @Persistent
    public String user;

    /**
     * Constructor.
     *
     */
    public DishVote()
    {
    }

    // Accessors for the fields.  JDO doesn't use these, but the application does.

    public Key getKey()
    {
        return key;
    }

    public void setDishId(long aDishId)
    {
        dishId=aDishId;
    }

    public void setUser(String aUser)
    {
        user=aUser;
    }
}