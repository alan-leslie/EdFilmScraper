package RefScraper.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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

            FilmFestEventDetailPage thePage = new FilmFestEventDetailPage(theURL, "", theLogger);

            URL pageURL = thePage.getURL();

            List<PlacePeriod> thePlacePeriods = thePage.getPlacePeriods();     // this could be multiples
            assertEquals(2, thePlacePeriods.size());
            
            PlacePeriod thePlacePeriod = thePlacePeriods.get(0);
            Position thePosition = thePlacePeriod.getPosition();
            String theVenueName = thePlacePeriod.getVenueName();
            Period thePeriod = thePlacePeriod.getPeriod();

            assertEquals(theURL, pageURL);

            assertEquals(false, thePeriod.hasDuration());
            Calendar startDate = new GregorianCalendar(TimeZone.getTimeZone( "Europe/London" ));
            startDate.setTime(thePeriod.getStartDate());
            assertEquals(2012, startDate.get(Calendar.YEAR));
            assertEquals(5, startDate.get(Calendar.MONTH));
            assertEquals(21, startDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(18, startDate.get(Calendar.HOUR_OF_DAY));
            assertEquals(30, startDate.get(Calendar.MINUTE));

            assert (thePosition.getLatitude().equalsIgnoreCase("55.941098"));
            assert (thePosition.getLongitude().equalsIgnoreCase("-3.217729"));
            assert (theVenueName.equalsIgnoreCase("Cineworld 11"));
            
            assert(thePage.getCountry().equalsIgnoreCase("USA"));
            assert(thePage.getDirector().equalsIgnoreCase("Amy Seimetz"));
            String theDuration = thePage.getDuration();
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
    public void testListPage() {
        try {
            URL theURL = new URL("http://www.edfilmfest.org.uk/films?src=cal&date=2012-06-21");
            Logger theLogger = Logger.getLogger(PageTest.class.getName());

            FilmFestEventListPage thePage = new FilmFestEventListPage(theURL, theLogger);
            List<HTMLLink> candidates = thePage.getCandidates();

            assertEquals(10, candidates.size());
            String theFirstURL = candidates.get(0).getHREF();
            String theFirstTitle = candidates.get(0).getText();

            assert (theFirstURL.equalsIgnoreCase("http://www.sciencefestival.co.uk/whats-on/categories/talk/science-festival-church-service"));
            assert (theFirstTitle.equalsIgnoreCase("Science Festival Church service"));
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
