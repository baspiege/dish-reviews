package geonotes.utils;

/**
 * JavaScript utilities.
 *
 * @author Brian Spiegel
 */
public class JSUtils
{

    /**
     * Escape characters: single quote, double quote, backslash,
     * newline, carriage return, tab, backspace, form feed.
     *
     * @param aInput the String to escape
     *
     * @return an escaped String
     */
    public static String escapeChars(String aInput)
    {
        if (aInput==null)
        {
            return "";
        }

        int inputLength = aInput.length();

        StringBuffer output = new StringBuffer(inputLength);

        for (int i = 0; i < inputLength; i++)
        {

            char currChar = aInput.charAt(i);

            if (currChar == '\\')
            {
                output.append("\\\\");
            }
            else if (currChar == '\"')
            {
                output.append("\\\"");
            }
            else if (currChar == '\'')
            {
                output.append("\\\'");
            }
            else if (currChar == '\n')
            {
                output.append("\\n");
            }
            else if (currChar == '\r')
            {
                output.append("\\r'");
            }
            else if (currChar == '\t')
            {
                output.append("\\t'");
            }
            else if (currChar == '\b')
            {
                output.append("\\b");
            }
            else if (currChar == '\f')
            {
                output.append("\\f");
            }
            else
            {
                output.append(currChar);
            }
        }

        return output.toString();
    }
}
