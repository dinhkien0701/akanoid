package powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class ShortenPaddlePowerUp extends PowerUp {
    private static final double SIZE = 25;

    public ShortenPaddlePowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.paddle.shrink();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.PURPLE);
        gc.fillRect(getX(), getY() + SIZE / 3, getWidth(), getHeight() / 2);
        gc.setFill(Color.WHITE);
        gc.fillText(">-<", getX() + 3, getY() + 17);
    }
}
