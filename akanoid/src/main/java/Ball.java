import java.util.Deque;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {

  private static final double BALL_RADIUS = 10.0;
  private static final double BALL_SPEED = 5.0;

  private double speed;
  private double r;

  private Deque<Position> previousPosition = new LinkedList<Position>();

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

  public void bounceOff(GameObject other, BallCollision collision) {
    if (collision == BallCollision.CORNER) {
      dx = -dx;
      dy = -dy;
    } else if (collision == BallCollision.VERTICAL) {
      dx = -dx;
    } else if (collision == BallCollision.HORIZONTAL) {
      dy = -dy;
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
    if ((distX * distX + distY * distY) > (this.r * this.r)) {
      return BallCollision.NONE;
    } else {
      distY = Math.abs(distY);
      distX = Math.abs(distX);
      if (distX == distY && distX == this.r) {
        return BallCollision.CORNER;
      } else {
        if (distX == this.r) {
          return BallCollision.VERTICAL;
        } else if (distY == this.r) {
          return BallCollision.HORIZONTAL;
        } else {
          return BallCollision.HORIZONTAL;
        }
      }
    }
  }

  @Override
  void update(GameManager gm) {
    move();
    if (this.getX() < gm.map.x) {
      this.setX(gm.map.x);
      dx = -dx;
    }
    if (this.getX() + this.getWidth() > gm.map.width + gm.map.x) {
      this.setX(gm.map.width + gm.map.x - this.getWidth());
      dx = -dx;
    }
    if (this.getY() < 0) {
      this.setY(0);
      dy = -dy;
    }
    if (this.getY() > gm.height) {
      gm.onBallLost(this);
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
    gc.setFill(Color.WHITE);
    for (Position ball : previousPosition) {
      gc.setFill(Color.WHITE);
      gc.fillOval(ball._x, ball._y, this.getWidth() - i, this.getHeight() - i);
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
  void render(GraphicsContext gc) {
    this.drawEffect(gc);
    gc.setFill(Color.RED);
    gc.fillOval(this.getX(), this.getY(), this.getHeight(), this.getWidth());
  }

  enum BallCollision {
    NONE, HORIZONTAL, VERTICAL, CORNER
  };
}
