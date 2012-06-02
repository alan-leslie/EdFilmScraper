package RefScraper.data;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Model of science festival page that contains lists of items to be processed
 * @author al
 */
public class FilmFestEventListPage {

    private final URL theURL;
    private final Document theDocument;
    private final Logger theLogger;
    private static String theBaseURL = "http://www.edfilmfest.org.uk";

    /**
     * Constructs model of Edinburgh film festival list page.
     * @param newURL 
     * @param logger
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException  
     */
    public FilmFestEventListPage(URL newURL,
            Logger logger) throws IOException, ParserConfigurationException, SAXException {
        theURL = newURL;
        theLogger = logger;
        HTMLPageParser theParser = new HTMLPageParser(theLogger);
        theDocument = theParser.getParsedPage(theURL);
    }

    /**
     * @return -the base url for this page
     */
    public static String getBaseURL() {
        return theBaseURL;
    }

    /**
     * @return -the page url
     */
    public URL getURL() {
        return theURL;
    }

    /**
     * Finds candidate links by looking various sections of the page.
     * @return - the candidate links
     */
    public List<HTMLLink> getCandidates() {
        List<HTMLLink> theCandidates = new ArrayList<HTMLLink>();
        getMainListCandidates(theCandidates);

        return theCandidates;
    }

    /**
     * Finds candidate links by looking up top level links in the page
     * @param theCandidates - the links to populate
     */
    private void getMainListCandidates(List<HTMLLink> theCandidates) {
        NodeList linkNodeList = null;

        try {
            String searchString = "html//li[@class='show_listing']";
            XPath linkXpath = XPathFactory.newInstance().newXPath();
            linkNodeList = (NodeList) linkXpath.evaluate(searchString, theDocument, XPathConstants.NODESET);

            int listLength = linkNodeList.getLength();

            for (int i = 0; i < listLength; ++i) {
                Node childNode = (Node) linkNodeList.item(i);
                
                String anchorSearchString = "./a";
                XPath anchorXPath = XPathFactory.newInstance().newXPath();
                Node anchorNode = (Node) anchorXPath.evaluate(anchorSearchString, childNode, XPathConstants.NODE);
                
                if(anchorNode != null){
                    short nodeType = anchorNode.getNodeType();

                    Element theElement = (Element) anchorNode;
                    String detailText = childNode.getTextContent();
                    int titleEnd = detailText.indexOf("   ");
                    String theTitle = detailText.substring(0, titleEnd);
                    String theHREF = theElement.getAttribute("href");

                    if(!theHREF.startsWith("http://")){
                        theHREF = getBaseURL() + theHREF;
                    }

                    if (!theHREF.isEmpty()) {
                        theLogger.log(Level.INFO, "Found candidate :{0}", theTitle);
//                        String cleanTitle = theTitle.replace("&", "and");
                        HTMLLink theCandidate = new HTMLLink(theTitle, theHREF);

                        theCandidates.add(theCandidate);
                    }
                }
            }
        } catch (Exception e) {
            theLogger.log(Level.SEVERE, "Exception on XPath: ", e);
        }
    }
        
    static String getAsciiText(String theText) {
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
                        theBuilder.append(FilmFestEventListPage.asciiFromUTF(codePoint));
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
