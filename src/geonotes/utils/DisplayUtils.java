package geonotes.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Display utilities.
 *
 * @author Brian Spiegel
 */
public class DisplayUtils
{
    /**
    * Display non breaking space if null or an HTML escaped string.
    */
    public static String getSpaceIfNull(String aString) {
        if (aString==null || aString.length()==0) {
            return "&nbsp;";
        } else {
            return HtmlUtils.escapeChars(aString);
        }
    }
    
    /**
    * Display non breaking space if null or an HTML escaped string.
    */
    public static String displayElapsedTime(long oldSeconds, long newSeconds) {
        String display="";
        long seconds=newSeconds-oldSeconds;
        if (seconds<60){
            display=Math.round(seconds)+" sec";
        } else {
            long minutes=seconds/60;
            if (minutes<60) {
                display=minutes+" min";
            } else {
                long hours=minutes/60;
                if (hours<24) {
                    display=hours+" hr";
                    } else {
                        long days=hours/24;
                        display=days+" days";
                    }
                }
            }
        return display;
    }
}
