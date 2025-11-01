package gameobject.powerup;

import gameobject.ball.Ball;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class BiggerBallPowerUp extends PowerUp {

    public BiggerBallPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        for (Ball ball : pp.getListOfBall()) {
            ball.setRadius(20);
        }
    }

    @Override
    public void update(PlayingProcess pp) {
        super.update(pp);
        if (isApplying()) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - this.getTimer() >= DURATION_SECONDS) {
                for (Ball ball : pp.getListOfBall()) {
                    double ballRadius = ball.getRadius();
                    ball.resetSize();
                    System.out.println(-1);
                    System.out.println(ball.getRadius());
                }
                setIsEnd();
            } else {
                for (Ball ball : pp.getListOfBall()) {
                    ball.setRadius(20);
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(isFalling()) {
            gc.setFill(Color.DODGERBLUE);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
            gc.setFill(Color.WHITE);
            gc.fillText("Bigger", getX() + 8, getY() + 17);
        }
    }

}
