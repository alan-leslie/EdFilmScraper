/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RefScraper.data;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author al
 */
public class PageFactory {

    public static IListPage getListPage(URL newURL, Logger logger) {
        IListPage retVal = null;

        try {
            retVal = new FilmFestEventListPage(newURL, logger);
        } catch (IOException ex) {
            Logger.getLogger(PageFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PageFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PageFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retVal;
    }

    public static IDetailPage getDetailPage(URL newURL, String theDateString, Logger logger) {
        IDetailPage retVal = null;

        try {
            retVal = new FilmFestEventDetailPage(newURL, theDateString, logger);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return retVal;
    }
}
