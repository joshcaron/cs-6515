// An unchecked Exception thrown when attempting to access an Invalid
// Location without properly checking to see that it exists
class HotelNotFoundException extends RuntimeException {
    private String msg;

    HotelNotFoundException(String msg) {
        this.msg = msg;
    }

    HotelNotFoundException() {
        this.msg = "HotelNotFoundException";
    }

    public String toString() {
        return msg;
    }
}