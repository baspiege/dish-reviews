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
 
    // Accessors for the fields.  JDO doesn't use these, but the application does. 

    public Key getKey()
    { 
        return key; 
    }  
        
    public void setNote(String aNote)
    { 
        note=aNote; 
        noteLowerCase=aNote.toLowerCase(); 
    }
    
    public void setLastReview(String aLastReview)
    { 
        lastReview=aLastReview; 
    }
    
    public void setLastReviewUserId(String aLastReviewUserId)
    { 
        lastReviewUserId=aLastReviewUserId; 
    }
    
    public void setLastReviewImageId(long aId)
    { 
        lastReviewImageId=aId; 
    }
        
    public void setLastUpdateTime(Date aLastUpdateTime)
    { 
        lastUpdateTime=aLastUpdateTime; 
    }

    public void setReviewCount(long aReviewCount)
    { 
        reviewCount=aReviewCount; 
    }
    
    public void setStoreId(long aStoreId)
    { 
        storeId=aStoreId; 
    }
     
    public void setUser(String aUser)
    { 
        user=aUser; 
    }
    
    public void setYesVote(long aYes)
    { 
        yesVote=aYes; 
    }
}