package gameobject.powerup;

import gameobject.ball.Ball;
import gameobject.brick.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class BiggerBallPowerUp extends PowerUp {

    private final Image powerUpImage;

    public BiggerBallPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
        this.powerUpImage = LoadImage.getImage("/image/BiggerBallPowerUp.png");
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
        if (isFalling()) {
            if (powerUpImage != null) {
                gc.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}
