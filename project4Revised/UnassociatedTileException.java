// An unchecked Exception thrown when attempting to access an Invalid
// Location without properly checking to see that it exists
class UnassociatedTileException extends RuntimeException {
    private String msg;

    UnassociatedTileException(String msg) {
        this.msg = msg;
    }

    UnassociatedTileException() {
        this.msg = "UnassociatedTileException";
    }

    public String toString() {
        return msg;
    }
}