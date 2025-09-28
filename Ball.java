import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Ball extends MovableObject {
  double speed = 5.0;

  Ball(double x, double y, double r) {
    super(x, y, r * 2, r * 2);
    dx = speed;
    dy = -speed;
  }

  double radius() {
    return width / 2;
  }

  void bounceOff(GameObject other) {
    dy = -dy;
  }

  double clamp(double v, double a, double b) {
    return Math.max(a, Math.min(b, v));
  }

  boolean checkCollision(GameObject other) {
    double cx = x + radius();
    double cy = y + radius();
    double closestX = clamp(cx, other.x, other.x + other.width);
    double closestY = clamp(cy, other.y, other.y + other.height);
    double distX = cx - closestX;
    double distY = cy - closestY;
    return (distX * distX + distY * distY) < (radius() * radius());
  }

  void update(GameManager gm) {
    move();
    if (x < 0) {
      x = 0;
      dx = -dx;
    }
    if (x + width > gm.width) {
      x = gm.width - width;
      dx = -dx;
    }
    if (y < 0) {
      y = 0;
      dy = -dy;
    }
    if (y > gm.height) {
      gm.onBallLost();
    }
  }


  void render(GraphicsContext gc) {
    gc.setFill(Color.WHITE);
    gc.fillOval(x, y, width, height);
  }
}
