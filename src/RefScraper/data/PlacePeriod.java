/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RefScraper.data;

/**
 *
 * @author al
 */
public class PlacePeriod {
    private Period thePeriod = null;
    private String theVenueName = null;
    private Position thePosition = null;
    
    PlacePeriod(Period newPeriod, String venueName){
        thePeriod = newPeriod;
        theVenueName = venueName;
        String[] venueNameBits = theVenueName.split(" ");
        for(int i = 0; i < venueNameBits.length; ++i){
        // TODO - probably need to strip blanks and numbers             
        }

        thePosition = PositionMap.getInstance().getPosition(venueNameBits[0]);
    }

    public Period getPeriod() {
        return thePeriod;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlacePeriod other = (PlacePeriod) obj;
        if (this.thePeriod != other.thePeriod && (this.thePeriod == null || !this.thePeriod.equals(other.thePeriod))) {
            return false;
        }
        if ((this.theVenueName == null) ? (other.theVenueName != null) : !this.theVenueName.equals(other.theVenueName)) {
            return false;
        }
        if (this.thePosition != other.thePosition && (this.thePosition == null || !this.thePosition.equals(other.thePosition))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.thePeriod != null ? this.thePeriod.hashCode() : 0);
        hash = 59 * hash + (this.theVenueName != null ? this.theVenueName.hashCode() : 0);
        hash = 59 * hash + (this.thePosition != null ? this.thePosition.hashCode() : 0);
        return hash;
    }

    public Position getPosition() {
        return thePosition;
    }

    public String getVenueName() {
        return theVenueName;
    }

    public boolean isComplete(){
        boolean retVal = true;
        
        if(thePosition == null ||  theVenueName == null || thePeriod == null){
            retVal = false;
        }
        
        return retVal;
    }
}
