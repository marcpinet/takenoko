package takenoko;

public class BambooTile {
    public boolean isCultivable;
    public boolean[] irrigatedSides;

    public BambooTile() {
        this.isCultivable = true;
        this.irrigatedSides = new boolean[6];
    }
}
