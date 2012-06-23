package RefScraper.data;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to hold information for a HREF, a position and a duration.
 * @author al
 */
public class RefThree implements Comparable {

    String theName;
    private URL theLocationRef;
    private URL theURL;
    private String theHREF;
    private String theDatePeriodString = "";
    private List<PlacePeriod> thePlacePeriods = null;
    private Map<String, String> theExtendedData = null;
    private final Logger theLogger;

    /**
     * 
     * @param theTitle 
     * @param theHREF 
     * @param theDatePeriodString 
     * @param logger 
     */
    public RefThree(String theTitle,
            String theHREF,
            String theDatePeriodString,
            Logger logger) {
        this.theHREF = theHREF;
        String thePageHREF = theHREF;
        this.theDatePeriodString = theDatePeriodString;
        theLogger = logger;

        try {
            theURL = new URL(thePageHREF);
        } catch (MalformedURLException ex) {
            theLogger.log(Level.SEVERE, null, ex);
        }
        theName = theTitle;
    }

    /**
     * a copy constructor to make sure that the object is not shared in threads
     * @param theOther - the original to be copied
     */
    public RefThree(RefThree theOther) {
        theName = theOther.theName;
        theLocationRef = theOther.theLocationRef;
        theURL = theOther.theURL;
        thePlacePeriods = theOther.thePlacePeriods;
        theDatePeriodString = theOther.theDatePeriodString;
        theLogger = theOther.theLogger;
    }

    /**
     * Output the placemark data in different xml formats.
     * @param ps - the stream to where the data is written
     * @param asKML - whether the output format is for google maps (KML) or 
     * timeline (XML)
     */
    // TODO - get extended data from a map<string, string>
    public void outputAsXML(PrintStream ps,
            boolean asKML) {
        if (!isComplete()) {
            return;
        }

        DateFormat theDateTimeFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        theDateTimeFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        for (PlacePeriod thePlacePeriod : thePlacePeriods) {
            Period thePeriod = thePlacePeriod.getPeriod();
            String theVenueName = thePlacePeriod.getVenueName();
            Position thePosition = thePlacePeriod.getPosition();

            DateFormat theDateFormat = new SimpleDateFormat("EEE MMM dd");
            theDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            String dateString = theDateFormat.format(thePeriod.getStartDate());
            DateFormat theTimeFormat = new SimpleDateFormat("HH:mm");
            theTimeFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            String timeString = theTimeFormat.format(thePeriod.getStartDate());

            if (asKML) {
                ps.print("<Placemark>");
                ps.println();
                ps.print("<name>");
                ps.println();
                ps.print(theName.trim());
                ps.println();
                ps.print("</name>");
                ps.println();
                ps.print("<description>");
                ps.println();

                if (theURL != null) {
                    ps.print("&lt;p&gt;");
                    ps.println();
                    ps.print("&lt;a href=\"");
                    ps.print(theURL);
                    ps.print("\"&gt; more info&gt;&gt;&gt;");
                    ps.print("&lt;/a&gt;");
                    ps.println();
                    ps.print("&lt;/p&gt;");
                    ps.println();
                }

                ps.print("</description>");
                ps.println();

                if (thePeriod.hasDuration()) {
                    ps.print("<TimeSpan>");
                    ps.print("<begin>");
                    ps.print(theDateTimeFormat.format(thePeriod.getStartDate()));
                    ps.print("</begin>");
                    ps.print("<end>");
                    ps.print(theDateTimeFormat.format(thePeriod.getEndDate()));
                    ps.print("</end>");
                    ps.print("</TimeSpan>");
                    ps.println();
                } else {
                    ps.print("<TimeStamp>");
                    ps.print("<when>");
                    ps.print(theDateTimeFormat.format(thePeriod.getStartDate()));
                    ps.print("</when>");
                    ps.print("</TimeStamp>");
                    ps.println();
                }

                ps.print("<ExtendedData>");
                ps.println();

                for (Map.Entry<String, String> entry : theExtendedData.entrySet()) {
                    String key = entry.getKey();
                    ps.println("<Data name=\"" + key + "\">");
                    ps.print("<value>");
                    String value = entry.getValue();
                    ps.print(value);
                    ps.print("</value>");
                    ps.println();
                    ps.println("</Data>");
                }
                
                ps.println("<Data name=\"LocationName\">");
                ps.print("<value>");
                ps.print(theVenueName);
                ps.print(", Edinburgh");
                ps.print("</value>");
                ps.println();
                ps.println("</Data>");
                ps.println("<Data name=\"DateString\">");
                ps.print("<value>");
                ps.print(dateString);
                ps.print("</value>");
                ps.println();
                ps.println("</Data>");
                ps.println("<Data name=\"TimeString\">");
                ps.print("<value>");
                ps.print(timeString);
                ps.print("</value>");
                ps.println();
                ps.println("</Data>");
                ps.println("<Data name=\"LatLonStr\">");
                ps.print("<value>");
                ps.print(thePosition.getLatitude());
                ps.print(",");
                ps.print(thePosition.getLongitude());
                ps.print("</value>");
                ps.println();
                ps.println("</Data>");
                ps.println("<Data name=\"Url\">");
                ps.print("<value>");
                ps.print(theURL.toString());
                ps.print("</value>");
                ps.println();
                ps.println("</Data>");
                ps.print("</ExtendedData>");
                ps.println();

                ps.print("<Point>");
                ps.println();
                ps.print("<coordinates>");
                ps.print(thePosition.getLongitude());
                ps.print(",");
                ps.print(thePosition.getLatitude());
                ps.print("</coordinates>");
                ps.println();
                ps.print("</Point>");
                ps.println();
                ps.print("</Placemark>");
                ps.println();
            } else {
                ps.print("<event ");
                ps.print("start=\"");
                ps.print(theDateTimeFormat.format(thePeriod.getStartDate()));
                ps.print("\" ");
                if (thePeriod.hasDuration()) {
                    ps.print("end=\"");
                    ps.print(theDateTimeFormat.format(thePeriod.getEndDate()));
                    ps.print("\" ");
                }

                ps.print("title=\"");
                ps.print(theName.trim());
                ps.print("\">");
                ps.println();
                ps.print("</event>");
                ps.println();
            }
        }
    }

    /**
     * 
     * @return - a unique id for the placemark
     */
    public String getId() {
        String strId = theName.replaceAll(" ", "");
        return strId;
    }

    /**
     * @return - the href of this placemark
     */
    public String getHREF() {
        return theHREF;
    }

    /**
     * attempt to fill in all of the placemark data
     * @return - whether all of the required data has been completed
     */
    public boolean complete() {
        try {
            IDetailPage thePage = PageFactory.getDetailPage(theURL, theDatePeriodString, theLogger);
            thePlacePeriods = thePage.getPlacePeriods();
            theExtendedData = thePage.getExtendedData();

//            theDuration = thePage.getValue("duration");
//            theDirector = thePage.getValue("director");
//            theCountry = thePage.getValue("country");
        } catch (Exception exc) {
            theLogger.log(Level.SEVERE, "Unable to parse: " + getId(), exc);
        }

        if (isComplete()) {
            return true;
        } else {
            theLogger.log(Level.WARNING, "Unable to complete {0}", getId());
            return false;
        }
    }

    /**
     * @return - whether all of the required data has been set
     */
    private boolean isComplete() {
        boolean retVal = !thePlacePeriods.isEmpty();

        int i = 0;
        for (PlacePeriod thePlacePeriod : thePlacePeriods) {
            if (!thePlacePeriod.isComplete()) {
                theLogger.log(Level.WARNING, "Unable to complete {0}", getId());
                theLogger.log(Level.WARNING, "Unable to complete index {0}", i);
                retVal = false;
            }
            ++i;
        }

        return retVal;
    }

    public int compareTo(Object anotherPlacemark) throws ClassCastException {
        if (!(anotherPlacemark instanceof RefThree)) {
            throw new ClassCastException("A RefThree object expected.");
        }
        String anotherPlacemarkName = ((RefThree) anotherPlacemark).getId();
        return this.getId().compareTo(anotherPlacemarkName);
    }

    public List<PlacePeriod> getPlacePeriods() {
        List<PlacePeriod> retVal = new ArrayList<PlacePeriod>();

        for (PlacePeriod thePlacePeriod : thePlacePeriods) {
            PlacePeriod theCopy = new PlacePeriod(thePlacePeriod.getPeriod(), thePlacePeriod.getVenueName());
            retVal.add(theCopy);
        }

        return retVal;
    }
}
