import java.util.Vector;
import java.util.Set;
import java.util.HashSet;

// A Representation of an Acquire Game Board. 
// Contains all the Singleton Tiles and 
public class Board implements IBoard {
    private static final Integer MAX_HOTELS = 7;
    private static final Integer FOUND_SIZE = 1;

    private Vector<Tile> singletons;  // All the Singleton tiles on this board
    private Vector<Hotel> hotels;     // All the hotels in play on this board

    Board() {
        this.singletons = new Vector<Tile>();
        this.hotels = new Vector<Hotel>();
    }

    Board(Vector<Tile> singletons, Vector<Hotel> hotels) {
        this.singletons = singletons;
        this.hotels = hotels;
    } 
    
    // What effect will placing a tile at the specified location have on 
    // this board? Returns a Response with appropriate information.
    public Response query(Location loc) {
        if(this.canPlaceSingleton(loc)) {
            return new SingletonResponse();
        } else if(this.canFound(loc)) {
            Vector<HLabel> labels = new Vector<HLabel>();
            
            for(HLabel l : HLabel.values()) {
                if(!this.hasHotelByLabel(l)) {
                    labels.add(l);
                }
            }

            return new FoundResponse(labels);
        } else if(this.canGrow(loc)) {
            return new GrowResponse(this.whichHotelWillGrow(loc));
        } else if(this.canMerge(loc)) {
            Set<HLabel> hotels = this.getDistinctHotels(this.getNeighboringTiles(loc));
            Vector<HLabel> acquirers = new Vector<HLabel>();
            Vector<HLabel> acquirees = new Vector<HLabel>();

            for(HLabel h : hotels) {
                if(this.canHotelAcquire(loc, h)) {
                    acquirers.add(h);
                } else {
                    acquirees.add(h);
                }
            }

            return new MergeResponse(acquirers, acquirees);
        } else {
            return new ImpossibleResponse("Cannot place tile in this Location");
        }
    }

    // Get all the Singleton tiles that this Board contains
    public Vector<Tile> getSingletons() { 
        return this.singletons; 
    }

    // Get all the Hotels that this Board contains
    public Vector<Hotel> getHotels() { 
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
        Vector<Tile> neighbors = this.getNeighboringTiles(loc);
        
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
            return neighbors.size() == 0;
        }
    }

    // Is it valid to place a tile at the given location with the effect of
    // growing a Hotel on this Board?
    public boolean canGrow(Location loc) {
        Vector<Tile> neighbors = this.getNeighboringTiles(loc);
        
        return !this.hasBeenPlaced(loc) && this.howManyDistinctHotels(neighbors) != 1;
    }

    // Return the label of the Hotel that will grow if a tile is place at the
    // given location on this Board IF: this.canGrow(loc) == true
    public HLabel whichHotelWillGrow(Location loc) {
        if(this.canGrow(loc)) {
            Vector<Tile> neighbors = this.getNeighboringTiles(loc);
            Set<HLabel> hotels = this.getDistinctHotels(neighbors);
            // Because can grow checks to make sure there is only one hotel neighbor
            return hotels.iterator().next();
        }
        String msg = "No Hotel will grow at this Location";
        throw new HotelNotFoundException(msg);
    }

    // Is it valid to place a tile at the given location with the effect of
    // founding a hotel on this Board?
    public boolean canFound(Location loc) {
        Vector<Tile> neighbors = this.getNeighboringTiles(loc);
        
        return this.hotels.size() < Board.MAX_HOTELS
            && neighbors.size() == Board.FOUND_SIZE
            && this.howManySingletons(neighbors) == Board.FOUND_SIZE
            && !this.hasBeenPlaced(loc);
    }
    
    // Is it valid to place a tile at the given location with the effect of 
    // founding the Hotel with the given label on this Board?
    public boolean canFoundHotel(Location loc, HLabel label) {
        return this.canFound(loc) && !this.hasHotelByLabel(label);
    }
    
    // Is it valid to place a tile at the given location with the effect of 
    // merging Hotels on this Board?
    public boolean canMerge(Location loc) {
        Vector<Tile> neighbors = this.getNeighboringTiles(loc);

        if(this.hasBeenPlaced(loc)
        || this.howManyDistinctHotels(neighbors) < 2) {
            return false;
        }

        for(HLabel label : this.getDistinctHotels(neighbors)) {
            if(!this.getHotelByLabel(label).isSafe()) {
                return false;
            }
        }

        return true;
    }
    
    // Is it valid to place a tile at the given location on this Board 
    // with the effect of merging Hotels, with the Hotel specified by the 
    // given label acquiring all other hotels?
    public boolean canHotelAcquire(Location loc, HLabel label) {
        Set<HLabel> hotels = this.getDistinctHotels(this.getNeighboringTiles(loc));

        if(!this.canMerge(loc) || !hotels.contains(label)) {
            return false;
        }

        Hotel h = this.getHotelByLabel(label);

        for(HLabel l : hotels) {
            if(this.getHotelByLabel(l).size() > h.size()) {
                return false;
            }
        }

        return true;
    }
    
    // Found the hotel specified by the given Label by placing a tile at the
    // given Location on this Board IF: this.canFoundHotel(loc, label) == true
    public void foundHotel(Location loc, HLabel label) {
        if(!this.canFoundHotel(loc, label)) {
            throw new InvalidPlacementException();
        }

        Vector<Tile> neighbors = this.getNeighboringTiles(loc);
        Vector<Location> locs = new Vector<Location>();

        for(Tile t : neighbors) {
            this.singletons.remove(t);
            locs.add(t.getLocation());
        }

        this.hotels.add(new Hotel(label, locs));
    }
    
    // Place a Singleton tile at the given Location on this Board IF:
    // this.canPlaceSingleton(loc) == true
    public void placeSingleton(Location loc) {
        if(!this.canPlaceSingleton(loc)) {
            throw new InvalidPlacementException();
        }
        
        this.singletons.add(new Singleton(loc));
    }
    
    // Place a tile at the given Location on this Board with the effect of 
    // growing a Hotel, IF: this.canGrow(loc) == true
    public void growHotel(Location loc) {
        if(!this.canGrow(loc)) {
            throw new InvalidPlacementException();
        }

        Set<HLabel> labels = this.getDistinctHotels(this.getNeighboringTiles(loc));

        HLabel label = labels.iterator().next();

        this.getHotelByLabel(label).grow(loc);
    }
    
    // Place a tile at the given Location on this Board with the effect of
    // merging hotels, with the Hotel specified by the given label as the 
    // Acquirer, IF: this.canHotelAcquire(loc, label) == true
    public void mergeHotels(Location loc, HLabel label) {
        if(!this.canHotelAcquire(loc, label)) {
            throw new InvalidPlacementException();
        }

        Set<HLabel> labels = this.getDistinctHotels(this.getNeighboringTiles(loc));
        Hotel acquiring = this.getHotelByLabel(label);
        for(HLabel l : labels) {
            if(label.equals(l)) {
                continue;
            }

            Hotel h = this.getHotelByLabel(l);
            acquiring.mergeWith(h);
            this.hotels.remove(h);
        }

    }
    
    // Get all neighboring Tiles at this Location
    public Vector<Tile> getNeighboringTiles(Location loc) {
        Vector<Tile> neighbors = new Vector<Tile>();
        Vector<Location> locs = loc.getNeighboringLocations();

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
        throw new HotelNotFoundException(msg);
    }

    private Hotel getHotelByLabel(HLabel label) {
        for (Hotel h : this.hotels) {
            if (h.getName().equals(label)) {
                return h;
            }
        }
        String msg = "Hotel not found with the given label " + label;
        throw new HotelNotFoundException(msg);
    }

    private boolean hasHotelByLabel(HLabel label) {
        for (Hotel h : this.hotels) {
            if (h.getName().equals(label)) {
                return true;
            }
        }
        return false;
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
        Set<HLabel> labels = new HashSet<HLabel>();

        for(Tile t : tiles) {
            if(t.isOwned()) {
                labels.add(((Owned) t).getHotelName());
            }
        }

        return labels.size();
    }

    private Set<HLabel> getDistinctHotels(Vector<Tile> tiles) {
        Set<HLabel> labels = new HashSet<HLabel>();

        for(Tile t : tiles) {
            if (t.isOwned()) {
                labels.add(((Owned) t).getHotelName());
            }
        }

        return labels;
    }

    private boolean hasBeenPlaced(Location loc) {
        return this.hasSingletonByLoc(loc)
            || this.hasOwnedByLoc(loc);
    }
}