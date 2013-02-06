// A Tile owned by a Hotel
public class Owned extends Tile {
    private HLabel hotelName;    // The name of the Hotel this Tile is owned by

    Owned(Location loc, HLabel label) {
        super(loc);
        this.hotelName = label;
    }

    public boolean isOwned() { return true; }

    public HLabel getHotelName() {
        return this.hotelName;
    }

    public boolean isSingleton() { return false; }
}