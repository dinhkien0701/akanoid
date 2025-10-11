import javafx.scene.canvas.GraphicsContext;

abstract class PowerUp extends MovableObject {
    private static final double FALLING_SPEED = 2.0;

    protected PowerUp(double x, double y, double width, double height) {
        // dx = 0
        super(x, y, width, height, 0, FALLING_SPEED);
    }

    @Override
    void update(GameManager gm) {
        move();
    }

    abstract void applyEffect(GameManager gm);
}