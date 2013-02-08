public abstract class Response {
    private Integer responseType;

    Response(Integer type) {
        this.responseType = type;
    }

    // Valid Return Types
    public static final Integer IMPOSSIBLE = 0; // Denotes Impossible
    public static final Integer SINGLETON = 1;  // Denotes SingletonResponse
    public static final Integer FOUND = 2;      // Denotes FoundResponse
    public static final Integer GROW = 3;       // Denotes GrowResponse
    public static final Integer MERGE = 4;      // Denotes MergeResponse

    public Integer getResponseType() { return this.responseType; }

    public boolean isSingletonResponse() { return false; }

    public boolean isGrowResponse() { return false; }

    public boolean isFoundResponse() { return false; }

    public boolean isMergeResponse() { return false; }

    public boolean isImpossibleResponse() { return false; }

    public SingletonResponse asSingletonResponse() { 
        throw new ClassCastException("Invalid ResponseType Cast"); 
    }

    public GrowResponse asGrowResponse() { 
        throw new ClassCastException("Invalid ResponseType Cast"); 
    }

    public FoundResponse asFoundResponse() { 
        throw new ClassCastException("Invalid ResponseType Cast"); 
    }

    public MergeResponse asMergeResponse() { 
        throw new ClassCastException("Invalid ResponseType Cast"); 
    }

    public ImpossibleResponse asImpossibleResponse() { 
        throw new ClassCastException("Invalid ResponseType Cast"); 
    }
}