import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Paddle extends MovableObject {
  double speed = 6.0;

  Paddle(double x, double y, double w, double h) {
    super(x, y, w, h);
  }

  void moveLeft() {
    dx = -speed;
  }

  void moveRight() {
    dx = speed;
  }

  void stop() {
    dx = 0;
  }


  void update(GameManager gm) {
    move();
    if (x < 0) {
      x = 0;
    }
    if (x + width > gm.width) {
      x = gm.width - width;
    }
  }


  void render(GraphicsContext gc) {
    gc.setFill(Color.YELLOW);
    gc.fillRect(x, y, width, height);
  }
}