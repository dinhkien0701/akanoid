package powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import object.Ball;
import process.PlayingProcess;

public class BiggerBallPowerUp extends PowerUp {
    private static final double SIZE = 25;
    private static final int DURATION_SECONDS = 6;

    public BiggerBallPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        for (Ball ball : pp.getBalls()) {
            ball.upSize();
        }

        pp.addTimedEffect(DURATION_SECONDS, () -> {
            for (Ball ball : pp.getBalls()) {
                ball.resetSize();
            }
        });
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.DODGERBLUE);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
        gc.setFill(Color.WHITE);
        gc.fillText("Bigger", getX() + 8, getY() + 17);
    }

}
