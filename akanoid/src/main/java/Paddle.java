import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Paddle extends MovableObject {
  private static final double PADDLE_SPEED = 10.0;

  private double speed = 10.0;
  static double paddleWidth = 100;
  static double paddleHeight = 16;

  Paddle(double x, double y) {
    super(x, y, paddleWidth, paddleHeight, 0, 0);
  }

  public void setPaddleWidth(double w) {
    this.setWidth(w);
  }

  public void setPaddleHeight(double h) {
    this.setHeight(h);
  }

  public void setSpeed(double s) {
    this.speed = s;
  }

  void moveLeft() {
    // System.out.println("Left");
    dx = -speed;
  }

  void moveRight() {
    // System.out.println("Right");
    dx = speed;
  }

  void stop() {
    dx = 0;
  }

  void update(GameManager gm) {
    move();
    if (getX() < gm.map.x) {
      this.setX(gm.map.x);
    }
    if (getX() + getWidth() > gm.map.width + gm.map.x) {
      this.setX(gm.map.width + gm.map.x - this.getWidth());
    }
  }

  void render(GraphicsContext gc) {
    gc.setFill(Color.YELLOW);
    gc.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
  }
}