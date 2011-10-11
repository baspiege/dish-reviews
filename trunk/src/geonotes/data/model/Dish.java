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
    public long storeId;
    
    @Persistent 
    public Date lastUpdateTime;	

    @Persistent 
    public String note; 
    
    @Persistent 
    public long yes;
    
    @Persistent 
    public String user;	
 
    /**
     * Constructor.
     * 
     */ 
    public Dish()
    {
    } 
 
    // Accessors for the fields.  JDO doesn't use these, but the application does. 

    public Key getKey()
    { 
        return key; 
    }  
    
    public void setNote(String aNote)
    { 
        note=aNote; 
    }
    
    public void setLastUpdateTime(Date aLastUpdateTime)
    { 
        lastUpdateTime=aLastUpdateTime; 
    }
    
    public void setStoreId(long aStoreId)
    { 
        storeId=aStoreId; 
    }
        
    public void setYes(long aYes)
    { 
        yes=aYes; 
    }
     
    public void setUser(String aUser)
    { 
        user=aUser; 
    }
}