package RefScraper.data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author al
 */
public class RefThreeTest {

    Logger theLogger = makeLogger();

    /**
     * 
     */
//  TODO -  http://www.edfilmfest.org.uk/films/2012/sleepless-night
    @Test
    public void testRefThreeSun() {
        String theTitle = "Sun Don't Shine";
        String theHREF = "http://www.edfilmfest.org.uk/films/2012/sun-dont-shine";

        RefThree theTestRef = new RefThree(theTitle, theHREF, "", theLogger);
        boolean completed = theTestRef.complete();
        
        assertEquals(true, completed);
        
        PlacePeriod thePlacePeriod = theTestRef.getPlacePeriods().get(0);

        assertEquals(2, theTestRef.getPlacePeriods().size());
        Period thePeriod = thePlacePeriod.getPeriod();
        assertEquals(false, thePeriod.hasDuration());
        Calendar startDate = new GregorianCalendar(TimeZone.getTimeZone( "Europe/London" ));
        startDate.setTime(thePeriod.getStartDate());
//        Calendar endDate = new GregorianCalendar(TimeZone.getTimeZone( "Europe/London" ));
//        endDate.setTime(thePeriod.getEndDate());
        assertEquals(2012, startDate.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, startDate.get(Calendar.MONTH));
        assertEquals(21, startDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(18, startDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, startDate.get(Calendar.MINUTE));
//        assertEquals(2012, endDate.get(Calendar.YEAR));
//        assertEquals(Calendar.JUNE, endDate.get(Calendar.MONTH));
//        assertEquals(14, endDate.get(Calendar.DAY_OF_MONTH));
//        assertEquals(20, endDate.get(Calendar.HOUR_OF_DAY));
//        assertEquals(30, endDate.get(Calendar.MINUTE));
        
        Position thePosition = thePlacePeriod.getPosition();
        String theLat = thePosition.getLatitude();
        String theLong = thePosition.getLongitude();
        float theLatAsFloat = Float.parseFloat(theLat);
        float theLongAsFloat = Float.parseFloat(theLong);
        assert(theLatAsFloat > 55.5 && theLatAsFloat < 56.0);
        assert(theLongAsFloat > -3.3 && theLongAsFloat < -3.2);
    }
    
     
    /**
     *
     * @return - valid logger (single file).
     */
    private static Logger makeLogger() {
        Logger lgr = Logger.getLogger("RefThreeTest");
        lgr.setUseParentHandlers(false);
        lgr.addHandler(simpleFileHandler());
        return lgr;
    }

    /**
     *
     * @return - valid file handler for logger.
     */
    private static FileHandler simpleFileHandler() {
        try {
            FileHandler hdlr = new FileHandler("RefThreeTest.log");
            hdlr.setFormatter(new SimpleFormatter());
            return hdlr;
        } catch (Exception e) {
            System.out.println("Failed to create log file");
            return null;
        }
    }
}
