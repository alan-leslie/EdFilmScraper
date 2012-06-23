
package RefScraper.utils;

/**
 *
 * @author al
 */
public class UnicodeUtils {
        
    public static String getAsciiText(String theText) {
        StringBuilder theBuilder = new StringBuilder();
        int lengthInChars = theText.length();
        int noOfCodePoints = theText.codePointCount(0, lengthInChars - 1);

        try {
            if (lengthInChars > 0
                    && lengthInChars > noOfCodePoints) {
                for (int offset = 0; offset < lengthInChars;) {
                    final int codePoint = theText.codePointAt(offset);
                    char theCharAt = theText.charAt(offset);

                    if (codePoint >= 0 && codePoint < 128) {
                        theBuilder.append(theCharAt);
                    } else {
                        theBuilder.append(UnicodeUtils.asciiFromUTF(codePoint));
                    }

                    offset += Character.charCount(codePoint);
                }
            } else {
                theBuilder.append(theText);
            }
        } catch (IndexOutOfBoundsException exc) {
            System.out.println("out of bounds");
        }

        return theBuilder.toString();
    }
    
    static char asciiFromUTF(int codePoint) {
        char retVal = ' ';
        switch (codePoint) {
            case 8211:
                retVal = '-';
                break;
            case 8217:
                retVal = '\'';
                break;
        }

        return retVal;
    }    
}
