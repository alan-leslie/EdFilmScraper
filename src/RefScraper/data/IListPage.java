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
public interface IListPage {

    /**
     * Finds candidate links by looking various sections of the page.
     * @return - the candidate links
     */
    List<HTMLLink> getCandidates();

    /**
     * @return -the page url
     */
    URL getURL();
    
}
