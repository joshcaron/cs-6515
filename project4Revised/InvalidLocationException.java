// An unchecked Exception thrown when attempting to access an Invalid
// Location without properly checking to see that it exists
class InvalidLocationException extends RuntimeException {
    private String msg;

    InvalidLocationException(String msg) {
        this.msg = msg;
    }

    InvalidLocationException() {
        this.msg = "InvalidLocationException";
    }

    public String toString() {
        return msg;
    }
}