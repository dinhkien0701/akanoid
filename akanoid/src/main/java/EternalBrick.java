import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class EternalBrick extends Brick {

  EternalBrick(double x, double y, double width, double height) {
    super(x, y, width, height, 1000000000);
  }

  @Override
  void render(GraphicsContext gc) {
    gc.setFill(Color.NAVY);
    gc.fillRect(getX(), getY(), getWidth(), getHeight());
  }

  @Override
  void update(GameManager gameManager) {
  }
}
