public class FoundResponse extends Response {
    private List<HLabel> hotels

    FoundResponse(List<HLabel> hotels) {
        super(Response.FOUND);
        this.hotels = hotels;
    }

    @Override
    public boolean isFoundResponse() { return true; }

    @Override
    public FoundResponse asFoundResponse() { return this; }

    public List<HLabel> getPossibleHotels() { return this.hotels; }

    public Integer numberOfPossibleHotels() { return this.hotels.size(); }
}