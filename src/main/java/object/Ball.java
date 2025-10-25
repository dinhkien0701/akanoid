package object;

import java.util.Deque;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import core.MovableObject;
import math.Position;
import core.GameObject;
import process.PlayingProcess;

public class Ball extends MovableObject {

  private static final double BALL_RADIUS = 10.0;
  private static final double BALL_SPEED = 5.0;

  private double speed;
  private double r;

  private final Deque<Position> previousPosition = new LinkedList<>();

  // private boolean justBounced = false;

  public Ball(double x, double y) {
    super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2, BALL_SPEED / 2, -BALL_SPEED);
    previousPosition.clear();
    this.setRadius(BALL_RADIUS);
  }

  @Override
  public void resetSpeed() {
    super.setDx(BALL_SPEED / 2);
    super.setDy(-BALL_SPEED);
  }

  public double getRadius() {
    return this.r;
  }

  public void setRadius(double r) {
    this.r = r;
    this.setWidth(r * 2);
    this.setHeight(r * 2);
  }

  public void bounceOff(GameObject other, BallCollision collision) {
    // if(!(other instanceof Paddle)) {
    // if (justBounced) {
    // justBounced = false;
    // return;
    // }
    // }
    final double SPEED_UP = 1.05;
    if (collision == BallCollision.CORNER) {
      bounceOffCorner();
    } else if (collision == BallCollision.VERTICAL) {
      bounceOffVertical();
      // if (dx > 0) {
      // this.setX(other.getX() - this.getWidth() - 0.5);
      // } else {
      // this.setX(other.getX() + other.getWidth() + 0.5);
      // }
    } else if (collision == BallCollision.HORIZONTAL) {
      bounceOffHorizontal();
      // if (dy > 0) {
      // this.setY(other.getY() - this.getHeight() - 0.5);
      // } else {
      // this.setY(other.getY() + other.getHeight() + 0.5);
      // }
      if (other instanceof Paddle) {
        dx += ((Paddle) other).getDx() * 0.3;
      }
    }

    double maxSpeed = 7.0;
    double speed = Math.sqrt(dx * dx + dy * dy);
    if (speed > maxSpeed) {
      dx = dx / speed * maxSpeed;
      dy = dy / speed * maxSpeed;
    }

    double randomFactor = 1.0 + (Math.random() - 0.5) * 0.05;
    dx *= randomFactor;
    dy *= randomFactor;

    if (dx == 0) {
      dx += dy / 2;
      dy -= dy / 2;
    } else if (dy == 0) {
      dy += 1.5;
      dx -= 1.5;
    }
  }

  private void bounceOffCorner() {
    dx = -dx * 1.05;
    dy = -dy * 1.05;
  }

  private void bounceOffVertical() {
    dx = -dx * 1.05;
  }

  private void bounceOffHorizontal() {
    dy = -dy * 1.05;
  }

  private double clamp(double v, double a, double b) {
    return Math.max(a, Math.min(b, v));
  }

  public BallCollision checkCollision(GameObject other) {
    double cx = this.getX() + this.r;
    double cy = this.getY() + this.r;
    double closestX = clamp(cx, other.getX(), other.getX() + other.getWidth());
    double closestY = clamp(cy, other.getY(), other.getY() + other.getHeight());
    double distX = cx - closestX;
    double distY = cy - closestY;
    if (distY * distY + distX * distX > this.r * this.r) {
      return BallCollision.NONE;
    } else {
      System.out.println(distX + " " + distY);
      distY = Math.abs(distY);
      distX = Math.abs(distX);
      final double EPS = 0.01;
      if (Math.abs(distY - distX) < EPS) {
        return BallCollision.CORNER;
      } else {
        if (Math.abs(distX - this.r) < EPS) {
          return BallCollision.VERTICAL;
        } else {
          return BallCollision.HORIZONTAL;
        }
      }
    }
  }

  private void savePosition() {
    previousPosition.addFirst(new Position(this.getX(), this.getY()));
    if (previousPosition.size() >= 60) {
      previousPosition.removeLast();
    }
  }

  @Override
  public void update(PlayingProcess gm) {
    move();
    if (this.getX() < gm.map.getX()) {
      this.setX(gm.map.getX());
      dx = -dx;
    }
    if (this.getX() + this.getWidth() > gm.map.getWidth() + gm.map.getX()) {
      this.setX(gm.map.getWidth() + gm.map.getX() - this.getWidth());
      dx = -dx;
    }
    if (this.getY() < 0) {
      this.setY(0);
      dy = -dy;
    }
    if (this.getY() > gm.map.getHeight()) {
      gm.onBallLost(this);
    }
  }

  private void drawEffect(GraphicsContext gc) {
    double i = 0;
    gc.setFill(Color.WHITE);
    for (Position ball : previousPosition) {
      gc.setFill(Color.PINK);
      gc.fillOval(ball.getX(), ball.getY(), this.getWidth() - i, this.getHeight() - i);
      i = i + 0.4;
      if (this.getWidth() < 0) {
        this.setWidth(0);
      }
      if (this.getHeight() < 0) {
        this.setHeight(0);
      }
    }
  }

  @Override
  public void render(GraphicsContext gc) {
    // this.drawEffect(gc);
    gc.setFill(Color.RED);
    gc.fillOval(this.getX(), this.getY(), this.getHeight(), this.getWidth());
  }

  public enum BallCollision {
    NONE, HORIZONTAL, VERTICAL, CORNER
  };
}
