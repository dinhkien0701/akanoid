package object;

import process.PlayingProcess;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class NormalBrick extends Brick {

  public NormalBrick(double x, double y, double w, double h) {
    super(x, y, w, h, 1);
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.setFill(Color.RED);
    gc.fillRect(getX(), getY(), getWidth(), getHeight());
  }

  @Override
  public void update(PlayingProcess gm) {}
}
