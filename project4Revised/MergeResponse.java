public class MergeResponse extends Response {
    private List<HLabel> canAcquire;
    private List<HLabel> canBeAcquired;

    MergeResponse(List<HLabel> canAcquire, List<HLabel> canBeAcquired) {
        super(Response.MERGE);
        this.canAcquire = canAcquire;
        this.canBeAcquired = canBeAcquired;
    }

    @Override
    public boolean isMergeResponse() { return true; }

    @Override
    public MergeResponse asMergeResponse() { return this; }

    public List<HLabel> getPossibleAcquirerers() { return this.canAcquire; }

    public List<HLabel> getAcquirees() { return this.canBeAcquired; }

    public Integer numberPossibleAcquirerers() { return this.canAcquire.size(); }

    public Integer numberAcquirees() { return this.canBeAcquired.size(); }
}