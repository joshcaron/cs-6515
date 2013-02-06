public class SingletonResponse extends Response {

    SingletonResponse() {
        super(Response.SINGLETON);
    }

    @Override
    public boolean isSingletonResponse() { return true; }

    @Override
    public SingletonResponse asSingletonResponse() { return this; }
}