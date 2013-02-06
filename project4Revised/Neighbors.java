public class Neighbors {
    private Tile top;
    private Tile bottom;
    private Tile left;
    private Tile right;
    private Vector<Tile> tiles;

    Neighbors(Tile left, Tile right, Tile top, Tile bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.tiles = new Vector<Tile>();
        this.tiles.add(left);
        this.tiles.add(right);
        this.tiles.add(top);
        this.tiles.add(bottom);
    }

    public Integer numberSingletons() {
        for (Tile t : this.tiles) {

        }
    }

    public Integer numberOwned() {

    }

    public List<HLabel> getPresentHotels() {

    }

    public List<Tile> getPresentSingletons() {

    }

    public boolean hasLeftNeighbor() {
        return this.left != null;
    }

    public boolean hasRightNeighbor() {
        return this.right != null;
    }

    public boolean hasTopNeighbor() {
        return this.top != null;
    }

    public boolean hasBottomNeighbor() {
        return this.bottom != null;
    }

    public Tile getLeftNeighbor() {
        if(this.hasLeftNeighbor()) {
            return this.left;
        } else {
            String msg = "Left Neighbor does not exist";
            throw new TileDoesNotExistException(msg);
        }
    }

    public Tile getRightNeighbor() {
        if(this.hasRightNeighbor()) {
            return this.right;
        } else {
            String msg = "Right Neighbor does not exist";
            throw new TileDoesNotExistException(msg);
        }
    }

    public Tile getTopNeighbor() {
        if(this.hasTopNeighbor()) {
            return this.top;
        } else {
            String msg = "Top Neighbor does not exist";
            throw new TileDoesNotExistException(msg);
        }
    }

    public Tile getBottomNeighbor() {
        if(this.hasBottomNeighbor()) {
            return this.bottom;
        } else {
            String msg = "Bottom Neighbor does not exist";
            throw new TileDoesNotExistException(msg);
        }
    }
}