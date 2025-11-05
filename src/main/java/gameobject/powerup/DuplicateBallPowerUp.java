package gameobject.powerup;

import gameobject.ball.Ball;
import gameobject.brick.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DuplicateBallPowerUp extends PowerUp {

  private final Image powerUpImage;

  public DuplicateBallPowerUp(double x, double y) {
    super(x, y, SIZE, SIZE);
    this.powerUpImage = LoadImage.getImage("/image/DuplicateBallPowerUp.png");
  }

  @Override
  public void applyEffect(PlayingProcess pp) {
    List<Ball> currentBalls = new ArrayList<>(pp.getListOfBall());
    for (Ball originalBall : currentBalls) {
      double rotationAngle = Math.toRadians(22.5);
      double cosAngle = Math.cos(rotationAngle);
      double sinAngle = Math.sin(rotationAngle);
      Ball ball1 = new Ball(originalBall.getX(), originalBall.getY());
      ball1.setDx(originalBall.getDx() * cosAngle - originalBall.getDy() * sinAngle);
      ball1.setDy(originalBall.getDx() * sinAngle + originalBall.getDy() * cosAngle);
      pp.getListOfBall().add(ball1);
      originalBall.setDx(originalBall.getDx() * cosAngle + originalBall.getDy() * sinAngle);
      originalBall.setDy(-originalBall.getDx() * sinAngle + originalBall.getDy() * cosAngle);
    }
    setIsEnd();
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
