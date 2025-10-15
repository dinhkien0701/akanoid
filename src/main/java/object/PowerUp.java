package object;

import core.MovableObject;
import javafx.scene.canvas.GraphicsContext;
import process.PlayingProcess;

public abstract class PowerUp extends MovableObject {
    private static final double FALLING_SPEED = 2.0;

    protected PowerUp(double x, double y, double width, double height) {
        super(x, y, width, height, 0, FALLING_SPEED);
    }

    @Override
    public void update(PlayingProcess pp) {
        move();
    }

    public abstract void applyEffect(PlayingProcess pp);
}