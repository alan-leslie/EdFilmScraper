package RefScraper.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author al
 */
public class PageTest {

    /**
     * 
     */
    @Test
    public void testDetailPage() {
        try {
            URL theURL = new URL("http://www.edfilmfest.org.uk/films/2012/sun-dont-shine");
            Logger theLogger = Logger.getLogger(PageTest.class.getName());

            IDetailPage thePage = new FilmFestEventDetailPage(theURL, "", theLogger);

            URL pageURL = thePage.getURL();

            List<PlacePeriod> thePlacePeriods = thePage.getPlacePeriods();     // this could be multiples
            assertEquals(2, thePlacePeriods.size());
            
            PlacePeriod thePlacePeriod = thePlacePeriods.get(0);
            Position thePosition = thePlacePeriod.getPosition();
            String theVenueName = thePlacePeriod.getVenueName();
            Period thePeriod = thePlacePeriod.getPeriod();

            assertEquals(theURL, pageURL);

            assertEquals(true, thePeriod.hasDuration());
            Calendar startDate = new GregorianCalendar(TimeZone.getTimeZone( "Europe/London" ));
            startDate.setTime(thePeriod.getStartDate());
            assertEquals(2012, startDate.get(Calendar.YEAR));
            assertEquals(5, startDate.get(Calendar.MONTH));
            assertEquals(21, startDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(18, startDate.get(Calendar.HOUR_OF_DAY));
            assertEquals(30, startDate.get(Calendar.MINUTE));
            
            Calendar endDate = new GregorianCalendar(TimeZone.getTimeZone( "Europe/London" ));
            endDate.setTime(thePeriod.getEndDate());
            assertEquals(2012, endDate.get(Calendar.YEAR));
            assertEquals(Calendar.JUNE, endDate.get(Calendar.MONTH));
            assertEquals(21, endDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(19, endDate.get(Calendar.HOUR_OF_DAY));
            assertEquals(49, endDate.get(Calendar.MINUTE));

            assert (thePosition.getLatitude().equalsIgnoreCase("55.941098"));
            assert (thePosition.getLongitude().equalsIgnoreCase("-3.217729"));
            assert (theVenueName.equalsIgnoreCase("Cineworld"));
            
            Map<String, String> extendedData = thePage.getExtendedData();
            
            assert(extendedData.get("Country").equalsIgnoreCase("USA"));
            assert(extendedData.get("Director").equalsIgnoreCase("Amy Seimetz"));
            String theDuration = extendedData.get("Duration");
            assert(theDuration.equalsIgnoreCase("79 mins"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     */
    @Test
    public void testKillerDetailPage() {
        try {
            URL theURL = new URL("http://www.edfilmfest.org.uk/films/2012/killer-joe");
            Logger theLogger = Logger.getLogger(PageTest.class.getName());

            IDetailPage thePage = new FilmFestEventDetailPage(theURL, "", theLogger);

            URL pageURL = thePage.getURL();

            List<PlacePeriod> thePlacePeriods = thePage.getPlacePeriods();     // this could be multiples
            assertEquals(1, thePlacePeriods.size());
            
            PlacePeriod thePlacePeriod = thePlacePeriods.get(0);
            Position thePosition = thePlacePeriod.getPosition();
            String theVenueName = thePlacePeriod.getVenueName();
            Period thePeriod = thePlacePeriod.getPeriod();

            assertEquals(theURL, pageURL);

            assertEquals(true, thePeriod.hasDuration());
            Calendar startDate = new GregorianCalendar(TimeZone.getTimeZone( "Europe/London" ));
            startDate.setTime(thePeriod.getStartDate());
            assertEquals(2012, startDate.get(Calendar.YEAR));
            assertEquals(Calendar.JUNE, startDate.get(Calendar.MONTH));
            assertEquals(20, startDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(21, startDate.get(Calendar.HOUR_OF_DAY));
            assertEquals(30, startDate.get(Calendar.MINUTE));
            
            Calendar endDate = new GregorianCalendar(TimeZone.getTimeZone( "Europe/London" ));
            endDate.setTime(thePeriod.getEndDate());
            assertEquals(2012, endDate.get(Calendar.YEAR));
            assertEquals(Calendar.JUNE, endDate.get(Calendar.MONTH));
            assertEquals(20, endDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(23, endDate.get(Calendar.HOUR_OF_DAY));
            assertEquals(13, endDate.get(Calendar.MINUTE));            
            
            DateFormat theDateFormat = new SimpleDateFormat("EEE MMM dd");
            theDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            String dateString = theDateFormat.format(thePeriod.getStartDate());
            DateFormat theTimeFormat = new SimpleDateFormat("HH:mm");
            theTimeFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            String timeString = theTimeFormat.format(thePeriod.getStartDate());

            assert (thePosition.getLatitude().equalsIgnoreCase("55.946821"));
            assert (thePosition.getLongitude().equalsIgnoreCase("-3.18608"));
            assert (theVenueName.equalsIgnoreCase("Festival Theatre"));
            
            Map<String, String> extendedData = thePage.getExtendedData();
            
            assert(extendedData.get("Country").equalsIgnoreCase("USA")); 
            assert(extendedData.get("Director").equalsIgnoreCase("William Friedkin"));
            String theDuration = extendedData.get("Duration");
            assert(theDuration.equalsIgnoreCase("103 mins"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    /**
     * 
     */
    @Test
    public void testListPage() {
        try {
            URL theURL = new URL("http://www.edfilmfest.org.uk/films?src=cal&date=2012-06-21");
            Logger theLogger = Logger.getLogger(PageTest.class.getName());

            IListPage thePage = new FilmFestEventListPage(theURL, theLogger);
            List<HTMLLink> candidates = thePage.getCandidates();
            
            int candSize = candidates.size();

            assertEquals(15, candSize);
            String theFirstURL = candidates.get(0).getHREF();
            String theFirstTitle = candidates.get(0).getText().trim();

            assert (theFirstURL.equalsIgnoreCase("http://www.edfilmfest.org.uk/films/2012/pp-rider"));
            assert (theFirstTitle.equalsIgnoreCase("PP Rider (Shonben raida)"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
