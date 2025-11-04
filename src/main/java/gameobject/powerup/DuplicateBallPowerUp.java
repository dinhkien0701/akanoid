package gameobject.powerup;

import gameobject.ball.Ball;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DuplicateBallPowerUp extends PowerUp {

  private static final double SIZE = 25;
  private static Image powerUpImage;

  static {
    try {
      InputStream imageStream =
          DuplicateBallPowerUp.class.getResourceAsStream("/images/powerup_2x.png");
      if (imageStream == null) {
        throw new IllegalArgumentException("Không tìm thấy file ảnh: /images/powerup_2x.png");
      }
      powerUpImage = new Image(imageStream);
    } catch (Exception e) {
      System.err.println("Lỗi tải ảnh cho power-up nhân đôi bóng.");
      powerUpImage = null;
    }
  }

  public DuplicateBallPowerUp(double x, double y) {
    super(x, y, SIZE, SIZE);
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
    if(isFalling()) {
        if (powerUpImage != null) {
            gc.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight());
        } else {
            gc.setFill(Color.GREENYELLOW);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        }
    }
  }
}
