package RefScraper.data;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Model of Edinburgh Film festival detail page (contains details of period position).
 * @author al
 */
public class FilmFestEventDetailPage implements IDetailPage {

    private final URL theURL;
    private final String theDateString;
    private final Document theDocument;
    private final Logger theLogger;
    private NodeList summaryTableData;
    private NodeList datesTableData;
    private NodeList detailsData;
    private static String theBaseURL = "http://www.edfilmfest.org.uk";
    private String theCountry = "";
    private String theVenueName = "";
    private String theDuration = "";
    private List<String> theVenueNames = new ArrayList<String>();
    /**
     * Constructs model of Edinburgh Film festival detail page.
     * @param newURL 
     * @param theDateString 
     * @param logger
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException  
     */
    public FilmFestEventDetailPage(URL newURL,
            String theDateString,
            Logger logger) throws IOException, ParserConfigurationException, SAXException {
        theURL = newURL;
        theLogger = logger;
        this.theDateString = theDateString;
        HTMLPageParser theParser = new HTMLPageParser(theLogger);
        theDocument = theParser.getParsedPage(theURL);
    }

    /**
     * Finds the period from the page.
     * @return -valid period or null if unobtainable
     */
    public static String getBaseURL() {
        return theBaseURL;
    }

    /**
     * 
     * @return -valid URL of this page.
     */
    @Override
    public URL getURL() {
        return theURL;
    }

    /**
     * Finds the ages name from the page.
     * @return -valid ages string "" if unobtainable
     */
    public String getCountry() {
        if (theCountry.isEmpty()) {
            if (detailsData == null) {
                detailsData = getDetailsData();
            }

            if (detailsData != null) {
                theCountry = getCountryFromList(detailsData);
            }
        }

        return theCountry;
    }
    
    /**
     * Finds the ages name from the page.
     * @return -valid ages string "" if unobtainable
     */
    public String getDuration() {
        if (theDuration.isEmpty()) {
            if (detailsData == null) {
                detailsData = getDetailsData();
            }

            if (detailsData != null) {
                theDuration = getDurationFromList(detailsData);
            }
        }

        return theDuration;
    }

    /**
     * Finds the period from the page.
     * @return -valid period or null if unobtainable
     */
    @Override
    public List<PlacePeriod> getPlacePeriods() {
        List<PlacePeriod> thePlacePeriods = new ArrayList<PlacePeriod>();

        if (datesTableData == null) {
            datesTableData = getDatesData();
        }

        SimpleDateFormat theDateFormat = new SimpleDateFormat("dd MMMM, HH:mm yyyy");
        theDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        
        int durationInMinutes = 0;
        theDuration = getDuration();
        if(!theDuration.isEmpty()){
            if(theDuration.endsWith("mins")){
                String theNoOfMins = theDuration.substring(0, theDuration.length() - 4).trim();
                durationInMinutes = Integer.parseInt(theNoOfMins);             
            }
        }

        if (datesTableData != null) {
            int noOfRows = datesTableData.getLength();

            for (int i = 0; i < noOfRows; ++i) {
                String startDateTime = "";
                String endDateTime = "";

                Node dateRow = datesTableData.item(i);
                String dateRowString = dateRow.getTextContent();
                int atIndex = dateRowString.indexOf(" at ");

                if (atIndex != -1) {
                    startDateTime = dateRowString.substring(0, atIndex) + " 2012";
                    String fullVenueName = dateRowString.substring(atIndex + 4);
                    theVenueNames.add(fullVenueName);
                        
                    try {
                        Date startDate = theDateFormat.parse(startDateTime);
                        Date endDate = new Date(startDate.getTime() + (durationInMinutes * 60 * 1000));
                        
                        Period thePeriod = new Period(startDate, endDate);
                        PlacePeriod thePlacePeriod = new PlacePeriod(thePeriod, fullVenueName);
                        thePlacePeriods.add(thePlacePeriod);
                    } catch (ParseException ex) {
                        theLogger.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return thePlacePeriods;
    }

    /*
     * Get the main table data from the page
     * @return - node list representing the main table section
     *
     */
    private NodeList getDatesData() {
        NodeList retVal = null;

        try {
            XPath mainTableXpath = XPathFactory.newInstance().newXPath();
            NodeList theData = (NodeList) mainTableXpath.evaluate("html//div[@id='show_booking']/ul/li/p", theDocument, XPathConstants.NODESET);

            if (theData != null) {
                int theLength = theData.getLength();

                if (theLength > 0) {
                    retVal = theData;
                }
            }
        } catch (XPathExpressionException ex) {
            theLogger.log(Level.SEVERE, null, ex);
        }

        return retVal;
    }
    /*
     * Get the main table data from the page
     * @return - node list representing the main table section
     *
     */

    private NodeList getDetailsData() {
        NodeList retVal = null;

        try {
            XPath mainTableXpath = XPathFactory.newInstance().newXPath();
            NodeList theData = (NodeList) mainTableXpath.evaluate("html//div[@id='show_details_inner']/ul/li", theDocument, XPathConstants.NODESET);

            if (theData != null) {
                int theLength = theData.getLength();

                if (theLength > 0) {
                    retVal = theData;
                }
            }
        } catch (XPathExpressionException ex) {
            theLogger.log(Level.SEVERE, null, ex);
        }

        return retVal;
    }

    /**
     * Try and get ages from the main table of the page
     * @param mainTableData 
     * @return valid ages string or "" if not obtainable
     */
    private String getCountryFromList(NodeList mainTableData) {
        int listLength = mainTableData.getLength();
        String retVal = "";
        
        if(listLength > 1){
            Node theValueNode = mainTableData.item(1);
            retVal = theValueNode.getTextContent().replace(" /", "").trim();
        }

        return retVal;
    }

    /**
     * Try and get the node that contains the value for the main table data item
     * with title dataName.
     * The main table is a table so looking for a tr node that has a th of dataName
     * @param mainTableData 
     * @param dataName 
     * @return - valid Node or null if not found
     */
    private Node getValueNodeFromTable(NodeList tableData,
            String dataName) {
        Node dataValueNode = null;

        try {
            int theLength = tableData.getLength();
            boolean dataFound = false;

            for (int i = 0; i < theLength && !dataFound; ++i) {
                String rowText = tableData.item(i).getTextContent();
                XPath detailsXpath = XPathFactory.newInstance().newXPath();
                NodeList theDetails = (NodeList) detailsXpath.evaluate("./td", tableData.item(i), XPathConstants.NODESET);

                if (theDetails != null) {
                    int theDetailsLength = theDetails.getLength();

                    if (theDetailsLength > 1) {
                        String theHeaderStr = theDetails.item(0).getTextContent();

                        if (theHeaderStr.equalsIgnoreCase(dataName)) {
                            dataValueNode = theDetails.item(1);
                            dataFound = true;
                        }
                    }
                }

            }
        } catch (XPathExpressionException ex) {
            theLogger.log(Level.SEVERE, null, ex);
        }

        return dataValueNode;
    }

    String getDirector() {
        String theDirector = "";

        if (detailsData == null) {
            detailsData = getDetailsData();
        }

        if (detailsData != null) {
            theDirector = getDirectorFromList(detailsData);
        }

        return theDirector;
    }

    private String getDurationFromList(NodeList detailsData) {
        int listLength = detailsData.getLength();
        String retVal = "";
        
        for(int i = 0; i < listLength && retVal.isEmpty(); ++i){
            Node theValueNode = detailsData.item(i);   
            String theText = theValueNode.getTextContent();
            if(theText.endsWith("mins")){
                retVal = theText;
            }
            
        }

        return retVal;
    }
    
       private String getDirectorFromList(NodeList detailsData) {
        int listLength = detailsData.getLength();
        String retVal = "";
        
        if(listLength > 0){
            Node theValueNode = detailsData.item(0);
            retVal = theValueNode.getTextContent().replace(" /", "").trim();
        }

        return retVal;
    }

    public Map<String, String> getExtendedData() {
        Map<String, String> retVal = new HashMap<String, String>();
        retVal.put("Country", getCountry());
        retVal.put("Duration", getDuration());
        retVal.put("Director", getDirector());
        
        return retVal;
    }
}