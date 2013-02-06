import java.util.Vector;
import java.util.Set;

// A Representation of an Acquire Game Board. 
// Contains all the Singleton Tiles and 
public class Board implements IBoard {
    private static final Integer MAX_HOTELS = 7;
    private Vector<Tile> singletons;  // All the Singleton tiles on this board
    private Vector<Hotel> hotels;     // All the hotels in play on this board

    Board() {
        this.singletons = new Vector<Singleton>();
        this.hotels = new Vector<Hotel>();
    }

    Board(Vector<Tile> singletons, Vector<Hotel> hotels) {
        this.singletons = singletons;
        this.hotels = hotels;
    } 
    
    // What effect will placing a tile at the specified location have on 
    // this board? Returns a Response with appropriate information.
    public Response query(Location loc);

    // Get all the Singleton tiles that this Board contains
    public List<Tile> getSingletons() { 
        return this.singletons; 
    }

    // Get all the Hotels that this Board contains
    public List<Hotel> getHotels() { 
        return this.hotels; 
    }

    // Is the Hotel with the given label in play on this Board?
    public boolean hotelInPlay(HLabel label) {
        for(Hotel h : this.hotels) {
            if(h.getName().equals(label)) {
                return true;
            }
        }
        return false;
    }

    // Get the Hotel with the given label that is in play on this board IF:
    // this.hotelInPlay(label) == true
    public Hotel getHotelInPlay(HLabel label) {
        for(Hotel h : this.hotels) {
            if(h.getName().equals(label)) {
                return h;
            }
        }
        String msg = "Hotel " + label + " not in play.";
        throw new HotelNotFoundException(msg);
    }

    // Is it valid to place a Singleton tile at the given location on 
    // this Board?
    public boolean canPlaceSingleton(Location loc) {
        Vector<Tile> neighbors = this.getNeighbors(loc);
        
        if(this.hasBeenPlaced(loc)) {
            return false;
        }

        if(this.hotels.size() == Board.MAX_HOTELS) {
            for(Tile t : neighbors) {
                if(t.isOwned()) {
                    return false;
                }
            }
            return true;
        } else {
            return this.neighbors.size() == 0;
        }
    }

    // Is it valid to place a tile at the given location with the effect of
    // growing a Hotel on this Board?
    public boolean canGrow(Location loc) {
        Vector<Tile> neighbors = this.getNeighbors(loc);
        
        return !this.hasBeenPlaced(loc) && this.howManyDistinctHotels(neighbors) != 1;
    }

    // Return the label of the Hotel that will grow if a tile is place at the
    // given location on this Board IF: this.canGrow(loc) == true
    public HLabel whichHotelWillGrow(Location loc) {
        if(this.canGrow(loc)) {
            Vector<Tile> neighbors = this.getNeighbors(loc);
            Set<Hotel> hotels = this.getDistinctHotels(neighbors);
            // Because can grow checks to make sure there is only one hotel neighbor
            return hotels.get(0);
        }
        String msg = "No Hotel will grow at this Location";
        throw new HotelNotFoundException(msg);
    }

    // Is it valid to place a tile at the given location with the effect of
    // founding a hotel on this Board?
    public boolean canFound(Location loc) {
        Vector<Tile> neighbors = this.getNeighbors(loc);
        
        return neighbors.size() == 1 && this.howManySingletons(neighbors) == 1;
    }
    
    // Is it valid to place a tile at the given location with the effect of 
    // founding the Hotel with the given label on this Board?
    public boolean canFoundHotel(Location loc, HLabel label) {

    }
    
    // Is it valid to place a tile at the given location with the effect of 
    // merging Hotels on this Board?
    public boolean canMerge(Location loc);
    
    // Is it valid to place a tile at the given location on this Board 
    // with the effect of merging Hotels, with the Hotel specified by the 
    // given label acquiring all other hotels?
    public boolean canHotelAcquire(Location loc, HLabel label);
    
    // Found the hotel specified by the given Label by placing a tile at the
    // given Location on this Board IF: this.canFoundHotel(loc, label) == true
    public void foundHotel(Location loc, HLabel label) throws Exception;
    
    // Place a Singleton tile at the given Location on this Board IF:
    // this.canPlaceSingleton(loc) == true
    public void placeSingleton(Location loc) throws Exception;
    
    // Place a tile at the given Location on this Board with the effect of 
    // growing a Hotel, IF: this.canGrow(loc) == true
    public void growHotel(Location loc) throws Exception;
    
    // Place a tile at the given Location on this Board with the effect of
    // merging hotels, with the Hotel specified by the given label as the 
    // Acquirer, IF: this.canHotelAcquire(loc, label) == true
    public void mergeHotels(Location loc, HLabel label) throws Exception;
    
    // Get all neighboring Tiles at this Location
    public Vector<Tile> getNeighboringTiles(Location loc) {
        Vector<Tile> neighbors = new Vector<Tile>();
        Vector<Location> locs = loc.getNeighbors();

        for(Location l : locs) {
            if(this.hasSingletonByLoc(loc)) {
                neighbors.add(this.getSingletonByLoc(loc));
            }
            
            if(this.hasOwnedByLoc(loc)) {
                HLabel ownerName = this.whichHotelHasLoc(loc);
                Hotel owner = this.getHotelByLabel(ownerName);
                neighbors.add(owner.getOwnedByLoc(loc));
            }
        }

        return neighbors;
    }

    public boolean hasSingletonByLoc(Location loc) {
        for(Tile t : this.singletons) {
            if(t.getLocation().equals(loc)) {
                return true;
            }
        }

        return false;
    }

    public Tile getSingletonByLoc(Location loc) {
        for(Tile t : this.singletons) {
            if(t.getLocation().equals(loc)) {
                return t;
            }
        }
        String msg = "Singleton not found at the given location.";
        throw new TileNotFoundException(msg);
    }

    public boolean hasOwnedByLoc(Location loc) {
        for(Hotel h : this.hotels) {
            if(h.contains(loc)) {
                return true;
            }
        }
        return false;
    }

    public HLabel whichHotelHasLoc(Location loc) {
        for (Hotel h : this.hotels) {
            if (h.contains(loc)) {
                return h.getName();
            }
        }
        String msg = "No hotel found having the given location";
        return new HotelNotFoundException(msg);
    }

    private getHotelByLabel(HLabel label) {
        for (Hotel h : this.hotels) {
            if (h.getName().equals(label)) {
                return h;
            }
        }
        String msg = "Hotel not found with the given label " + label;
        throw new HotelNotFoundException(msg);
    }

    private Integer howManySingletons(Vector<Tile> tiles) {
        Integer count = 0;
        for(Tile t : tiles) {
            if(t.isSingleton()) {
                count++;
            }
        }

        return count;
    }

    private Integer howManyDistinctHotels(Vector<Tile> tiles) {
        Set<HLabel> labels = new Set<HLabel>();

        for(Tile t : tiles) {
            if(t.isOwned()) {
                labels.add(t.getHotel());
            }
        }

        return this.labels.size();
    }

    private Set<HLabel> getDistinctHotels(Vector<Tile> tiles) {
        Set<HLabel> labels = new Set<HLabel>();

        for(Tile t : tiles) {
            if (t.isOwned()) {
                labels.add(t.getHotel());
            }
        }

        return labels;
    }

    private boolean hasBeenPlaced(Location loc) {
        return this.hasSingletonByLoc(loc)
            || this.hasOwnedByLoc(loc);
    }
}