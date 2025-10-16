package powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import object.Ball;
import process.PlayingProcess;

import java.io.InputStream;

public class DuplicateBallPowerUp extends PowerUp {

  private static final double SIZE = 25;
  private static Image powerUpImage;
  static {
    try {
      InputStream imageStream = DuplicateBallPowerUp.class.getResourceAsStream("/images/powerup_2x.png");
      if (imageStream == null) {
        throw new IllegalArgumentException("Không tìm thấy file ảnh: /images/powerup_2x.png");
      }
      powerUpImage = new Image(imageStream);
    } catch (Exception e) {
      System.err.println("Lỗi tải ảnh cho power-up nhân đôi bóng.");
      powerUpImage = null;
    }
  }

  private Ball ball1;
  private boolean isBall1Fall;
  private Ball ball2;
  private boolean isBall2Fall;

  public DuplicateBallPowerUp(double x, double y) {
    super(x, y, SIZE, SIZE);
  }

  @Override
  public void applyEffect(PlayingProcess pp) {
      pp.addPowerUp(this);

      double rotationAngle = Math.toRadians(22.5);
      double cosAngle = Math.cos(rotationAngle);
      double sinAngle = Math.sin(rotationAngle);
      ball1 = new Ball(pp.ball.getX(), pp.ball.getY());
      ball1.setDx(pp.ball.getDx() * cosAngle - pp.ball.getDy() * sinAngle);
      ball1.setDy(pp.ball.getDx() * sinAngle + pp.ball.getDy() * cosAngle);
      ball2 = new Ball(pp.ball.getX(), pp.ball.getY());
      ball2.setDx(pp.ball.getDx() * cosAngle + pp.ball.getDy() * sinAngle);
      ball2.setDy(-pp.ball.getDx() * sinAngle + pp.ball.getDx() * cosAngle);
      isBall2Fall = false;
      isBall1Fall = false;
  }

  @Override
  public boolean isEnd() {
    return (this.isBall2Fall && this.isBall1Fall);
  }

  @Override
  public void update(PlayingProcess pp) {
    if(!isApply) {
      move();
      if (checkCollisionPaddle(pp.paddle)) {
        applyEffect(pp);
        isApply = true;
        isBall2Fall = false;
        isBall1Fall = false;
      }
      if (this.y > pp.paddle.getY()) {
        this.stop();
        this.isBall1Fall = true;
        this.isBall2Fall = true;
      }
    } else {
      if(!isBall2Fall) {
        ball2.update(pp);
        if (ball2.checkCollision(pp.paddle) != Ball.BallCollision.NONE) {
          ball2.bounceOff(pp.paddle, ball2.checkCollision(pp.paddle));
          ball2.setY(pp.paddle.getY() - ball2.getHeight() - 1);
        }
        pp.checkBricksList(ball2);
        if (ball2.getY() > pp.paddle.getY()){
          isBall2Fall = true;
        }
      } else {
        ball2.stop();
      }
      if(!isBall1Fall) {
        ball1.update(pp);
        if (ball1.checkCollision(pp.paddle) != Ball.BallCollision.NONE) {
          ball1.bounceOff(pp.paddle, ball1.checkCollision(pp.paddle));
          ball1.setY(pp.paddle.getY() - ball1.getHeight() - 1);
        }
        pp.checkBricksList(ball1);
        if (ball1.getY() > pp.paddle.getY()){
          isBall1Fall = true;
        }
      } else {
        ball1.stop();
      }
    }
  }


  @Override
  public void render(GraphicsContext gc) {
    if (powerUpImage != null) {
      gc.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight());
    } else {
      if(!isApply) {
        gc.setFill(Color.GREENYELLOW);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
      } else {
        if(!isBall1Fall) {
          ball1.render(gc);
          gc.setFill(Color.ORANGE);
          gc.fillOval(ball1.getX(), ball1.getY(), ball1.getWidth(), ball1.getHeight());
        }
        if (!isBall2Fall) {
          ball2.render(gc);
          gc.setFill(Color.SKYBLUE);
          gc.fillOval(ball2.getX(), ball2.getY(), ball2.getWidth(), ball2.getHeight());
        }
      }
    }
  }
}