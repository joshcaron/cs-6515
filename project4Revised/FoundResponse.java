import java.util.Vector;
public class FoundResponse extends Response {
    private Vector<HLabel> hotels;

    FoundResponse(Vector<HLabel> hotels) {
        super(Response.FOUND);
        this.hotels = hotels;
    }

    @Override
    public boolean isFoundResponse() { return true; }

    @Override
    public FoundResponse asFoundResponse() { return this; }

    public Vector<HLabel> getPossibleHotels() { return this.hotels; }

    public Integer numberOfPossibleHotels() { return this.hotels.size(); }
}