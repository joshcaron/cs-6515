import java.util.Vector;

public class MergeResponse extends Response {
    private Vector<HLabel> canAcquire;
    private Vector<HLabel> canBeAcquired;

    MergeResponse(Vector<HLabel> canAcquire, Vector<HLabel> canBeAcquired) {
        super(Response.MERGE);
        this.canAcquire = canAcquire;
        this.canBeAcquired = canBeAcquired;
    }

    @Override
    public boolean isMergeResponse() { return true; }

    @Override
    public MergeResponse asMergeResponse() { return this; }

    public Vector<HLabel> getPossibleAcquirerers() { return this.canAcquire; }

    public Vector<HLabel> getAcquirees() { return this.canBeAcquired; }

    public Integer numberPossibleAcquirerers() { return this.canAcquire.size(); }

    public Integer numberAcquirees() { return this.canBeAcquired.size(); }
}