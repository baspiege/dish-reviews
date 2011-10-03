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
    public static String getSpaceIfNull(String aString)
    {
        if (aString==null || aString.length()==0)
        {
            return "&nbsp;";
        }
        else
        {
            return HtmlUtils.escapeChars(aString);
        }
    }
}
