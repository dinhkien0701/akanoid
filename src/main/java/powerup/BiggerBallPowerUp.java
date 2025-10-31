package powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import object.Ball;
import process.PlayingProcess;

public class BiggerBallPowerUp extends PowerUp {
    private static final double SIZE = 25;
    private static final long DURATION_SECONDS = 6000;

    public BiggerBallPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        for (Ball ball : pp.getListOfBall()) {
            ball.setWidth(2*ball.getWidth());
            ball.setHeight(2*ball.getHeight());
        }
        timer = System.currentTimeMillis();
    }

    @Override
    public void update(PlayingProcess pp) {
        long startTime = System.currentTimeMillis();
    }

    @Override
    public void render(GraphicsContext gc) {
        if(!isFallOut()) {
            gc.setFill(Color.DODGERBLUE);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
            gc.setFill(Color.WHITE);
            gc.fillText("Bigger", getX() + 8, getY() + 17);
        }
    }

}
