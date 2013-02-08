import java.util.Comparator;

// A Tile on an Acquire Board Game
public abstract class Tile {
    private Location loc;   // The Location of this Tile

    Tile(Location loc) {
        this.loc = loc;
    }

    public static Comparator<Tile> getComparator() {
        return new TileComparator();
    }

    // Get this Tile's Location
    public Location getLocation() { return this.loc; };

    // Is this Tile owned by a Hotel?
    public abstract boolean isOwned();

    // Is this Tile a Singleton?
    public abstract boolean isSingleton();

    // A Comparator over Tiles, which compares the Tiles by Location.
    private static class TileComparator implements Comparator<Tile> {

        public int compare(Tile t1, Tile t2) {
            return t1.getLocation().compareTo(t2.getLocation());
        }
    }
}