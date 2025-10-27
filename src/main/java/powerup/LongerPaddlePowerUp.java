package powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class LongerPaddlePowerUp extends PowerUp {
    private static final double SIZE = 25;
    private static final int DURATION_SECONDS = 6;

    public LongerPaddlePowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.paddle.extend();

        pp.addTimedEffect(DURATION_SECONDS, () -> {
            pp.paddle.resetSize();
        });
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillRect(getX(), getY() + SIZE / 3, getWidth(), getHeight() / 2);
        gc.setFill(Color.BLACK);
        gc.fillText("<-->", getX() + 3, getY() + 17);
    }
}
