import java.util.List;

// A Hotel in play on an Acquire Game Board. Contains an HLabel which 
// represents the Hotel's name, and a List of tiles that are owned by
// this Hotel.
public class Hotel {
    private static final Integer SAFE = 11; // Number of tiles a safe Hotel contains
    private static final Integer FOUND_NUM = 2;
    private HLabel name;        // The name of this Hotel
    private List<Tile> tiles;   // The Tiles owned by this Hotel
    private Integer size;       // The number of Tiles this Hotel owns

    Hotel(HLabel name, List<Location> locs) {
        this.name = name;

        if(locs.size() < Hotel.FOUND_NUM) {
            String msg = "Hotel's must have at least " + Hotel.FOUND_NUM + " tiles";
            throw new IllegalArgumentException(msg);
        } else {
            for(Location loc : locs) {
                this.tiles.add(new Owned(loc, name));
            }
        }

        this.size = this.tiles.size();
    }
    
    // Get this Hotel's name
    public HLabel getName() {
        return this.name;
    }
    
    // Get this Hotel's tiles
    public List<Tile> getTiles() {
        return this.tiles;
    }
    
    // Get the number of tiles this Hotel owns
    public Integer size() {
        return this.size;
    }
    
    // Is this Hotel safe?
    public boolean isSafe() {
        return this.size >= Hotel.SAFE;
    }
    
    // Merge that Hotel with this Hotel, by having this Hotel acquire all tiles
    // contained in that hotel.
    public void mergeWith(Hotel h) {
        List<Tile> otherTiles = h.getTiles();

        for(Tile t : otherTiles) {
            this.tiles.add(t);
            this.size++;
        }
    }
    
    // Grow the Hotel by adding a Hotel at the given 
    public void grow(Location loc) {
        if (this.hasNeighborInHotel(loc)) {
            this.tiles.add(new Owned(loc, this.name));
            this.size++;
        } else {
            String msg = "Location must neighbor at least one tile in this Hotel";
            throw new IllegalArgumentException(msg);
        }
    }
    
    // Does this Hotel contain a tile at the given Location?
    public boolean contains(Location loc) {
        for(Tile t : this.tiles) {
            if(t.getLocation().equals(loc)) {
                return true;
            }
        }
        return false;
    }

    public Tile getOwnedByLoc(Location loc) {
        for(Tile t : this.tiles) {
            if(t.getLocation().equals(loc)) {
                return t;
            }
        }
        String msg = "Tile not found in Hotel " + this.name + " at the given Location";
        throw new TileNotFoundException(msg);
    }

    // Does the given Location have at least one neighbor in this Hotel?
    public boolean hasNeighborInHotel(Location loc) {
        boolean neighborFound = false;
        for (Tile t : this.tiles) {
            if (loc.isNeighbor(t.getLocation())) {
                neighborFound = true;
                break;
            } else {
                continue;
            }
        }
        return neighborFound;
    }
}