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

  //private boolean justBounced = false;

  public Ball(double x, double y) {
    super(x, y, BALL_RADIUS*2,BALL_RADIUS*2, BALL_SPEED/2, -BALL_SPEED);
    previousPosition.clear();
    this.setRadius(BALL_RADIUS);
  }

  @Override
  public void resetSpeed(){
    super.setDx(BALL_SPEED/2);
    super.setDy(-BALL_SPEED);
  }

  public double getRadius() {
    return this.r;
  }

  public void setRadius(double r) {
    this.r = r;
    this.setWidth(r*2);
    this.setHeight(r*2);
  }

  public void bounceOff(GameObject other, BallCollision collision) {
      final double SPEED_UP = 1.05;
    if (collision == BallCollision.CORNER) {
      bounceOffCorner();
    } else if (collision == BallCollision.VERTICAL) {
      bounceOffVertical();
    } else if (collision == BallCollision.HORIZONTAL) {
      bounceOffHorizontal();
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

    if(dx == 0) {
      dx += dy/2;
      dy -= dy/2;
    } else if(dy == 0) {
      dy += dx/3;
      dx -= dx/3;
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

  private int clamp(int v, int a, int b) {
    return Math.max(a, Math.min(b, v));
  }

    public BallCollision checkCollision(GameObject other) {

        int cX = (int)(this.x + this.r/2);
        int cY = (int)(this.y + this.r/2);

        int left = (int) other.getX();
        int right = (int) (other.getX() + other.getWidth());
        int top = (int) other.getY();
        int bottom = (int) (other.getY() + other.getHeight());

        int nearestX = clamp(cX, left, right);
        int nearestY = clamp(cY, top, bottom);

        int distX = cX - nearestX;
        int distY = cY - nearestY;

        if (distX * distX + distY * distY <= this.r * this.r) {

            boolean hitLeftOrRight = (nearestX == left || nearestX == right);
            boolean hitTopOrBottom = (nearestY == top || nearestY == bottom);

            if (hitLeftOrRight && hitTopOrBottom)
                return BallCollision.CORNER;
            else if (hitLeftOrRight)
                return BallCollision.VERTICAL;
            else if (hitTopOrBottom)
                return BallCollision.HORIZONTAL;
        }
        return BallCollision.NONE;
    }



  private void savePosition() {
    previousPosition.addFirst(new Position(this.getX(), this.getY()));
    if(previousPosition.size() >= 60) {
      previousPosition.removeLast();
    }
  }

  @Override
  public void update(PlayingProcess gm) {
    move();
    if (this.getX() < gm.map.getX()) {
      this.setX(gm.map.getX());
      bounceOffVertical();
    }
    if (this.getX() + this.getWidth() > gm.map.getWidth() + gm.map.getX()) {
      this.setX(gm.map.getWidth() + gm.map.getX() - this.getWidth());
      bounceOffVertical();
    }
    if (this.getY() < 0) {
      this.setY(0);
      bounceOffHorizontal();
    }
    if (this.getY() > gm.map.getHeight()) {
      gm.onBallLost();
    }
    this.savePosition();
  }

  private void drawEffect(GraphicsContext gc) {
    double i = 0;
    gc.setFill(Color.WHITE);
    for (Position ball : previousPosition) {
      gc.setFill(Color.PINK);
      gc.fillOval(ball.getX(), ball.getY(), this.getWidth() - i, this.getHeight() - i);
      i = i + 0.4;
      if(this.getWidth() < 0){
        this.setWidth(0);
      }
      if(this.getHeight() < 0){
        this.setHeight(0);
      }
    }
  }

  @Override
  public void render(GraphicsContext gc) {
    //this.drawEffect(gc);
    gc.setFill(Color.RED);
    gc.fillOval(this.getX(), this.getY(), this.getHeight(), this.getWidth());
  }

  public enum BallCollision{
    NONE, HORIZONTAL , VERTICAL , CORNER
  };
}