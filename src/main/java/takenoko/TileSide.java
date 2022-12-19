package takenoko;

public enum TileSide {
    UP,
    UP_RIGHT,
    DOWN_RIGHT,
    DOWN,
    DOWN_LEFT,
    UP_LEFT;

    public TileSide rightSide() {
        return values()[(ordinal() + 1) % values().length];
    }

    public TileSide leftSide() {
        return values()[(ordinal() + 5) % values().length];
    }

    public TileSide oppositeSide() {
        return values()[(ordinal() + 3) % values().length];
    }
}
