import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class NormalBrick extends Brick {
  NormalBrick(double x, double y, double w, double h) {
    super(x, y, w, h, 1);
  }


  void render(GraphicsContext gc) {
    gc.setFill(Color.RED);
    gc.fillRect(x, y, width, height);
  }
}

