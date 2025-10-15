package object;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import core.*;
import process.PlayingProcess;

public class EternalBrick extends Brick {

  public EternalBrick(double x, double y, double width, double height) {
    super(x , y , width, height , 1000000000);
  }


  @Override
  public void render(GraphicsContext gc) {
    gc.setFill(Color.NAVY);
    gc.fillRect(getX(), getY(), getWidth(), getHeight());
  }

  @Override
  public void update(PlayingProcess gameManager) {}
}