public class GrowResponse extends Response {
    private HLabel hotel;

    GrowResponse(HLabel hotel) {
        super(Response.GROW);
        this.hotel = hotel;
    }

    @Override
    public boolean isGrowResponse() { return true; }

    @Override
    public GrowResponse asGrowResponse() { return this; }

    public HLabel getGrowingHotel() { return this.hotel; }
}