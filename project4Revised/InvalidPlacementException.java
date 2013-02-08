// An unchecked Exception thrown when attempting to place a tile in an 
// invalid location
class InvalidPlacementException extends RuntimeException {
    private String msg;

    InvalidPlacementException(String msg) {
        this.msg = msg;
    }

    InvalidPlacementException() {
        this.msg = "InvalidPlacementException";
    }

    public String toString() {
        return msg;
    }
}