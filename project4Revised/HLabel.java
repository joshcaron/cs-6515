// Labels for all valid Hotels that can be played in a game of Acquire
public enum HLabel {
    AMERICAN ("American"),
    CONTINENTAL ("Continental"),
    FESTIVAL ("Festival"),
    IMPERIAL ("Imperial"),
    SACKSON ("Sackson"),
    TOWER ("Tower"),
    WORLDWIDE ("Worldwide");

    private final String name;     // The name of this HLabel

    HLabel(String name) {
        this.name = name;
    }

    // Return the value of this HLabel
    public String toString() {
        return this.name;
    }
}