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
  private double r;
  private final Deque<Position> previousPosition = new LinkedList<>();

  public Ball(double x, double y) {
    super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2, BALL_SPEED, -BALL_SPEED);
    previousPosition.clear();
    this.setRadius(BALL_RADIUS);
  }

  public double getRadius() {
    return this.r;
  }

  public void setRadius(double r) {
    this.r = r;
    this.setWidth(r * 2);
    this.setHeight(r * 2);
  }

  public void setDx(double dx) {
    this.dx = dx;
  }

  public void setDy(double dy) {
    this.dy = dy;
  }

  public void bounceOff(GameObject other, BallCollision collision) {
    if (collision == BallCollision.CORNER) {
      dx = -dx;
      dy = -dy;
    } else if (collision == BallCollision.VERTICAL) {
      dx = -dx;
    } else if (collision == BallCollision.HORIZONTAL) {
      dy = -dy;
      if (other instanceof Paddle) {
        dx += ((Paddle) other).getDx() * 0.3;
      }
    }
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
      distY = Math.abs(distY);
      distX = Math.abs(distX);
      final double EPS = 0.1;
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

  @Override
  public void update(PlayingProcess pp) {
    move();
    if (this.getX() < pp.map.getX()) {
      this.setX(pp.map.getX());
      dx = -dx;
    }
    if (this.getX() + this.getWidth() > pp.map.getWidth() + pp.map.getX()) {
      this.setX(pp.map.getWidth() + pp.map.getX() - this.getWidth());
      dx = -dx;
    }
    if (this.getY() < 0) {
      this.setY(0);
      dy = -dy;
    }
    if (this.getY() > pp.map.getHeight()) {
      pp.onBallLost(this);
    }
    this.savePosition();
  }

  private void savePosition() {
    previousPosition.addFirst(new Position(this.getX(), this.getY()));
    if (previousPosition.size() >= 70) {
      previousPosition.removeLast();
    }
  }

  private void drawEffect(GraphicsContext gc) {
    double i = 0;
    for (Position pos : previousPosition) {
      gc.setFill(Color.WHITE);
      gc.fillOval(pos.getX(), pos.getY(), this.getWidth() - i, this.getHeight() - i);
      i = i + 0.4;
    }
  }

  @Override
  public void render(GraphicsContext gc) {
    this.drawEffect(gc);
    gc.setFill(Color.RED);
    gc.fillOval(this.getX(), this.getY(), this.getHeight(), this.getWidth());
  }

  public enum BallCollision {
    NONE, HORIZONTAL, VERTICAL, CORNER
  };
}