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
 * Store.
 *
 * @author Brian Spiegel
 */
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

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
    private Double latitude;

    @Persistent
    private Double longitude;

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
    public Store()
    {
    }

    public Key getKey() {
        return key;
    }
    
    public long getDishCount() {
        return dishCount;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getUser() {
        return user;
    }

    public long getYes() {
        return yes;
    }

    public void setDishCount(long aDishCount) {
        dishCount=aDishCount;
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

    public void setLatitude(double aLatitude) {
        latitude2Decimal=NumberUtils.getNumber2DecimalPrecision(aLatitude);
        latitude=aLatitude;
    }

    public void setLongitude(double aLongitude)
    {
        longitude2Decimal=NumberUtils.getNumber2DecimalPrecision(aLongitude);
        longitude=aLongitude;
    }

    public void setUser(String aUser) {
        user=aUser;
    }

    public void setYes(long aYes) {
        yes=aYes;
    }
}