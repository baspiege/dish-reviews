package geonotes.data.model;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import geonotes.utils.NumberUtils;
import java.io.Serializable;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")

/**
 * Store history.
 *
 * @author Brian Spiegel
 */
public class StoreHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long storeId;

    @Persistent
    private long dishCount;

    @Persistent
    private Blob image;	

    @Persistent
    private Blob imageThumbnail;	

    @Persistent
    private Date lastUpdateTime;	

    @Persistent
    private String note;

    @Persistent
    private double latitude;

    @Persistent
    private double longitude;

    @Persistent
    private double longitude2Decimal;

    @Persistent
    private double latitude2Decimal;

    @Persistent
    private String user;	

    @Persistent
    private long yes;

    /**
     * Constructor.
     *
     */
    public StoreHistory()
    {
    }
    
    /**
     * Constructor.
     *
     */
    public StoreHistory(Store aStore) {
        setStoreId(aStore.getKey().getId());
        setNote(aStore.getNote());
        setLastUpdateTime(aStore.getLastUpdateTime());
        setLongitude(aStore.getLongitude());
        setLatitude(aStore.getLatitude());
        setYes(aStore.getYes());
        setUser(aStore.getUser());
    }
    
    public Key getKey()
    {
        return key;
    }

    public void setDishCount(long aDishCount)
    {
        dishCount=aDishCount;
    }

    public void setNote(String aNote)
    {
        note=aNote;
    }

    public void setImage(Blob aImage)
    {
        image=aImage;
    }

    public void setImageThumbnail(Blob aImage)
    {
        imageThumbnail=aImage;
    }

    public void setLastUpdateTime(Date aLastUpdateTime)
    {
        lastUpdateTime=aLastUpdateTime;
    }

    public void setLatitude(double aLatitude)
    {
        latitude2Decimal=NumberUtils.getNumber2DecimalPrecision(aLatitude);
        latitude=aLatitude;
    }

    public void setLongitude(double aLongitude)
    {
        longitude2Decimal=NumberUtils.getNumber2DecimalPrecision(aLongitude);
        longitude=aLongitude;
    }

    public void setStoreId(long aStoreId)
    {
        storeId=aStoreId;
    }

    public void setUser(String aUser)
    {
        user=aUser;
    }

    public void setYes(long aYes)
    {
        yes=aYes;
    }
}