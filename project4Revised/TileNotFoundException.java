class TileNotFoundException extends RuntimeException {
    private String msg;

    TileNotFoundException(String msg) {
        this.msg = msg;
    }

    TileNotFoundException() {
        this.msg = "TileNotFoundException";
    }

    public String toString() {
        return msg;
    }
}