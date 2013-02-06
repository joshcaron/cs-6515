public class Impossible extends Response {
    private String message;

    Impossible(String message) {
        super(Response.IMPOSSIBLE);
        this.message = message;
    }

    public boolean isSingletonResponse() { return false; }

    public boolean isGrowResponse() { return false; }

    public boolean isFoundResponse() { return false; }

    public boolean isMergeResponse() { return false; }

    public boolean isImpossible() { return true; }

    public Impossible asImpossible() { return this; }

    public String getMessage() { return this.message; }
}