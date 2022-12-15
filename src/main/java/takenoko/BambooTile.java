package takenoko;

public class BambooTile implements Tile {
    private int bambooSize;

    public BambooTile() {
        bambooSize = 0;
    }

    public void growBamboo() {
        if (bambooSize < 4) {
            bambooSize++;
        }
    }

    public int getBambooSize() {
        return bambooSize;
    }

    public void shrinkBamboo() {
        if (bambooSize > 0) {
            bambooSize--;
        }
    }

    @Override
    public boolean isCultivable() {
        return bambooSize < 4;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BambooTile bambooTile && bambooTile.bambooSize == bambooSize;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
