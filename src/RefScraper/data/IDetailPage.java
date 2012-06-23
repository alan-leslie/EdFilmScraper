/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RefScraper.data;

import java.net.URL;
import java.util.List;

/**
 *
 * @author al
 */
public interface IDetailPage {

    /**
     * Gets the value of the key from the page
     * @param key 
     * @return -the required value or empty string if unobtainable
     */
    String getValue(String key);

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
