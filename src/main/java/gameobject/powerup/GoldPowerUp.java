package gameobject.powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class GoldPowerUp extends PowerUp {
    private static final double SIZE = 25;

    public GoldPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.getPaddle().takeHit();
        pp.points += 1000;
        setIsEnd();
    }

    @Override
    public void update(PlayingProcess pp) {
        if (!isEnd()) {
            super.update(pp);
        }
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
