import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Paddle extends MovableObject {
  private double speed = 6.0;
  static double paddleWidth = 100;
  static double paddleHeight = 16;

  Paddle(double x, double y) {
    super(x, y, paddleWidth, paddleHeight);
  }

  public void setPaddleWidth(double w) {
    this.width = w;
  }
  public void setPaddleHeight(double h) {
    this.height = h;
  }
  public void setSpeed(double s) {
    this.speed = s;
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
    if (x < gm.map.x) {
      x = gm.map.x;
    }
    if (x + width > gm.map.width + gm.map.x) {
      x = gm.map.width + gm.map.x - width;
    }
  }


  void render(GraphicsContext gc) {
    gc.setFill(Color.YELLOW);
    gc.fillRect(x, y, width, height);
  }
}