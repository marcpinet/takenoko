package takenoko.game.tile;

public class PowerUpNotAvailableException extends Exception {
    public PowerUpNotAvailableException() {
        super("Power up not available");
    }
}
