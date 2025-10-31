package gameobject.powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class FallBoomPowerUp extends PowerUp {
    private static final double SIZE = 25;

    public FallBoomPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.getPaddle().takeHit();
    }

    @Override
    public void update(PlayingProcess pp) {
        super.update(pp);
        setIsEnd();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isFalling()) {
            gc.setFill(Color.BLACK);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
            gc.setFill(Color.RED);
            gc.fillOval(getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 10);
        }
    }
}
