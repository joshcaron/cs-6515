import java.util.Comparator;
import java.lang.Comparable;
import java.util.Vector;

// A Location on an Acquire game Board
public class Location implements Comparable {

    private static final Integer MIN_ROW = 1;
    private static final Integer MAX_ROW = 9;
    private static final Integer MIN_COL = 1;
    private static final Integer MAX_COL = 12;

    // Represents the row of this Location. Must be a Integer
    // value on the interval [1,9]
    private Integer row;    

    // Represents the column of this Location. Must be an Integer
    // value on the interval [1, 12]
    private Integer column;

    // Create a Location from a proper <String, Integer> pair
    Location(String row, Integer column) {
        this((row.toUpperCase().charAt(0) - 64), column);
    }

    // Create a Location with an <Integer, Integer> pair.
    Location(Integer row, Integer column) {
        if(row < Location.MIN_ROW || row > Location.MAX_ROW) {
            throw new IllegalArgumentException();
        } else {
            this.row = row;
        }

        if(column < Location.MIN_COL || column > Location.MAX_COL) {
            throw new IllegalArgumentException();
        } else {
            this.column = column;
        }
    }

    // Get a comparator over Locations.
    public static Comparator<Location> getComparator() {
        return new LocationComparator();
    }

    // Get the row of this Location as an Integer value
    public Integer getRowAsInteger() { return this.row; }

    // Get the row of this Location as its proper String representation
    public String getRowAsString() {
        return String.valueOf((char) (this.row + 64));
    }

    // Get the column of this Location
    public Integer getColumn() { return this.column; }

    // Does this Location have a Left Neighbor?
    public boolean hasLeftNeighbor() {
        return this.column != this.MIN_COL;
    }

    // Does this Location have a Right Neighbor?
    public boolean hasRightNeighbor() {
        return this.column != this.MAX_COL;
    }

    // Does this Location have a Top Neighbor?
    public boolean hasTopNeighbor() {
        return this.row != this.MIN_ROW;
    }

    // Does this Location have a Bottom Neighbor?
    public boolean hasBottomNeighbor() {
        return this.row != this.MAX_ROW;
    }

    // Get this Location's Left Neighbor
    public Location getLeftNeighbor() {
        if(this.hasLeftNeighbor()) {
            return new Location(this.row, this.column - 1);
        } else {
            throw new InvalidLocationException("Left neighbor does not exist.");
        }
    }

    // Get this Location's Left Neighbor
    public Location getRightNeighbor() {
        if(this.hasLeftNeighbor()) {
            return new Location(this.row, this.column + 1);
        } else {
            throw new InvalidLocationException("Right neighbor does not exist.");
        }
    }

    // Get this Location's Left Neighbor
    public Location getTopNeighbor() {
        if(this.hasLeftNeighbor()) {
            return new Location(this.row - 1, this.column);
        } else {
            throw new InvalidLocationException("Top neighbor does not exist.");
        }
    }

    // Get this Location's Left Neighbor
    public Location getBottomNeighbor() {
        if(this.hasLeftNeighbor()) {
            return new Location(this.row + 1, this.column);
        } else {
            throw new InvalidLocationException("Bottom neighbor does not exist.");
        }
    }

    public Vector<Location> getNeighboringLocations() {
        Vector<Location> locs = new Vector<Location>();

        if(this.hasLeftNeighbor()) {
            locs.add(getLeftNeighbor());
        }
        if(this.hasRightNeighbor()) {
            locs.add(getRightNeighbor());
        }
        if(this.hasTopNeighbor()) {
            locs.add(getTopNeighbor());
        }
        if(this.hasBottomNeighbor()) {
            locs.add(getBottomNeighbor());
        }

        return locs;
    }

    // Is this Location a Neighbor of the given other Location?
    public boolean isNeighbor(Location other) {
        return this.isLeftNeighbor(other) 
            || this.isRightNeighbor(other)
            || this.isTopNeighbor(other)
            || this.isBottomNeighbor(other);
    }

    // Is this Location a Left Neighbor of the Other Location?
    public boolean isLeftNeighbor(Location other) {
        if (other.hasLeftNeighbor()) {
            return this.equals(other.getLeftNeighbor());
        } else {
            return false;
        }
    }

   // Is this Location a Right Neighbor of the Other Location?
    public boolean isRightNeighbor(Location other) {
        if (other.hasRightNeighbor()) {
            return this.equals(other.getRightNeighbor());
        } else {
            return false;
        }
    }

       // Is this Location a Top Neighbor of the Other Location?
    public boolean isTopNeighbor(Location other) {
        if (other.hasTopNeighbor()) {
            return this.equals(other.getTopNeighbor());
        } else {
            return false;
        }
    }

       // Is this Location a Bottom Neighbor of the Other Location?
    public boolean isBottomNeighbor(Location other) {
        if (other.hasBottomNeighbor()) {
            return this.equals(other.getBottomNeighbor());
        } else {
            return false;
        }
    }

    // Returns the hashcode for this Location
    public int hashCode() {
        return (int) Math.pow(this.row, 3) + (int) Math.pow(this.column, 5);
    }

    // Compare this Location to that Object lexicographically,  
    // IF the Object is a Location
    public int compareTo(Object o) {
        if (!(o instanceof Location)) {
            throw new ClassCastException("Can only compare to another Location.");
        }
        Location loc = (Location) o;
        if (this.getRowAsInteger() != loc.getRowAsInteger()) {
            return this.getRowAsInteger().compareTo(loc.getRowAsInteger());
        } else {
            return this.getColumn().compareTo(loc.getColumn());
        }
    }

    // A Comparator over Locations.
    private static class LocationComparator implements Comparator<Location> {
        // Compares the given locations lexicographic order with precedence given
        // to the row
        public int compare(Location loc1, Location loc2) {
            if (loc1.getRowAsInteger() != loc2.getRowAsInteger()) {
                return loc1.getRowAsInteger().compareTo(loc2.getRowAsInteger());
            } else {
                return loc1.getColumn().compareTo(loc2.getColumn());
            }
        }
    }
}