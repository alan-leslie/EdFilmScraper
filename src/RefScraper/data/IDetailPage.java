/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RefScraper.data;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 * @author al
 */
public interface IDetailPage {

    /**
     * Gets the extended data for the page
     * @return -the map of extended data
     */
    Map<String, String> getExtendedData();

    /**
     * Finds the period from the page.
     * @return -valid period or null if unobtainable
     */
    List<PlacePeriod> getPlacePeriods();

    /**
     *
     * @return -valid URL of this page.
     */
    URL getURL();
    
}
