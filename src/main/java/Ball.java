import java.util.Deque;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Ball extends MovableObject {
  private double speed = 5.0;
  enum BallCollision{
    NONE, HORIZONTAL , VERTICAL , CORNER
  };
  static double r = 8.0;
  private static class ob{
    double _x;
    double _y;
    ob(double x, double y) {
      _x = x;
      _y = y;
    }
  };
  private Deque<ob> past = new LinkedList<ob>();

  public Ball(double x, double y) {
    super(x, y, r * 2, r * 2);
    dx = speed/2;
    dy = -speed;
    past.clear();
  }

  private double radius() {
    return width / 2;
  }

  public void setRadius(double r) {
    this.width = r*2;
    this.height = r*2;
  }

  public void bounceOff(GameObject other, BallCollision collision) {
    if(collision == BallCollision.CORNER) {
      dx = -dx;
      dy = -dy;
    } else if(collision == BallCollision.VERTICAL) {
      dx = -dx;
    } else  if(collision == BallCollision.HORIZONTAL) {
      dy = -dy;
    }
  }

  private double clamp(double v, double a, double b) {
    return Math.max(a, Math.min(b, v));
  }

  public BallCollision checkCollision(GameObject other) {
    double cx = this.x + radius();
    double cy = this.y + radius();
    double closestX = clamp(cx, other.x, other.x + other.width);
    double closestY = clamp(cy, other.y, other.y + other.height);
    double distX = cx - closestX;
    double distY = cy - closestY;
    if((distX * distX + distY * distY) > (radius() * radius())){
      return BallCollision.NONE;
    } else {
      distY = Math.abs(distY);
      distX = Math.abs(distX);
      if(distX == distY && distX == radius()) {
        return BallCollision.CORNER;
      }
      else {
        if (distX == radius()) {
          return BallCollision.VERTICAL;
        } else if (distY == radius()) {
          return BallCollision.HORIZONTAL;
        } else {
          return BallCollision.HORIZONTAL;
        }
      }
    }
  }

  void update(GameManager gm) {
    move();
    if (x < gm.map.x) {
      x = gm.map.x;
      dx = -dx;
    }
    if (x + width > gm.map.width + gm.map.x) {
      x = gm.map.width + gm.map.x - width;
      dx = -dx;
    }
    if (y < 0) {
      y = 0;
      dy = -dy;
    }
    past.addFirst(new ob(x,y));

    if(past.size() >= 50) {
      past.removeLast();
    }
    if (y > gm.height) {
      gm.onBallLost();
    }
  }

  void render(GraphicsContext gc) {
    double i = 0;
    for (ob ball : past) {
      gc.setFill(Color.WHITE);
      gc.fillOval(ball._x, ball._y, this.width - i, this.height - i);
      i = i + 0.4;
      if(this.width < 0){
        this.width = 0;
      }
      if(this.height < 0){
        this.height = 0;
      }
    }
    gc.setFill(Color.RED);
    gc.fillOval(this.x, this.y, this.width, this.height);
  }
}
