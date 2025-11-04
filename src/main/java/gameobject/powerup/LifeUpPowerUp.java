package gameobject.powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class LifeUpPowerUp extends PowerUp {
    private static final double SIZE = 25;

    public LifeUpPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.getPaddle().addLife();
        setIsEnd();
    }

    @Override
    public void update(PlayingProcess pp) {
        if(!isEnd()) {
            super.update(pp);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isFalling()) {
            gc.setFill(Color.LIGHTPINK);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
            gc.setFill(Color.RED);
            gc.fillText("+1", getX() + 6, getY() + 17);
        }
    }
}
