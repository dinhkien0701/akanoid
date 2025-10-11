import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class NormalBrick extends Brick {

  NormalBrick(double x, double y, double w, double h) {
    super(x, y, w, h, 1);
  }

  @Override
  void render(GraphicsContext gc) {
    gc.setFill(Color.RED);
    gc.fillRect(getX(), getY(), getWidth(), getHeight());
  }

  @Override
  void update(GameManager gm) {
  }
}
