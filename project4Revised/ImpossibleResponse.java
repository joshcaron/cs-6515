public class ImpossibleResponse extends Response {
    private String message;

    ImpossibleResponse(String message) {
        super(Response.IMPOSSIBLE);
        this.message = message;
    }

    public boolean isSingletonResponse() { return false; }

    public boolean isGrowResponse() { return false; }

    public boolean isFoundResponse() { return false; }

    public boolean isMergeResponse() { return false; }

    public boolean isImpossibleResponse() { return true; }

    public ImpossibleResponse asImpossibleResponse() { return this; }

    public String getMessage() { return this.message; }
}