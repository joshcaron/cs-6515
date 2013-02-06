import java.util.List;

// The External API of the representation of an Acquire Game Board.
// 
public interface IBoard {
    
    public Response query(Location loc);

    public List<Tile> getSingletons();

    public List<Hotel> getHotels();

    public boolean hotelInPlay(HLabel label);

    public Hotel getHotelInPlay(HLabel label);

    public boolean canPlaceSingleton(Location loc);

    public boolean canGrow(Location loc);

    public HLabel whichHotelWillGrow(Location loc) throws Exception;

    public boolean canFoundHotel(Location loc, HLabel label);

    public boolean canFound(Location loc);
    
    public boolean canMerge(Location loc);
    
    public boolean canHotelAcquire(Location loc, HLabel label);
    
    public void foundHotel(Location loc, HLabel label) throws Exception;
    
    public void placeSingleton(Location loc) throws Exception;
    
    public void growHotel(Location loc) throws Exception;
    
    public void mergeHotels(Location loc, HLabel label) throws Exception;
    
    public Neighbors getNeighboringTiles(Location loc);

}