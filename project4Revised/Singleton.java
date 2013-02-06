// A Tile on an Acquire Board that is not associated with a Hotel
public class Singleton extends Tile {

    Singleton(Location loc) {
        super(loc);
    } 
    
    public boolean isOwned() { return false; }

    public HLabel getHotelName() throws UnassociatedTileException {
        String msg = "Attempted to acess Hotel name of a Singleton Tile";
        throw new UnassociatedTileException(msg);
    }

    public boolean isSingleton() { return true; }
}