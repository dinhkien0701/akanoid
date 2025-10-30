package object;

import LoadResource.LoadImage;
import process.PlayingProcess;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import LoadResource.LoadImage;
public class NormalBrick extends Brick {

  public NormalBrick(double x, double y, double w, double h) {
    super(x, y, w, h, 1);
  }

    private Image brickImage = LoadImage.getImage("/image/normal.png");
  @Override
  public void render(GraphicsContext gc) {
      if (brickImage != null) {
          gc.drawImage(brickImage, getX(), getY(), getWidth(), getHeight());
      } else {
          System.out.println("brickImage is null");
          gc.setFill(Color.RED);
          gc.fillRect(getX(), getY(), getWidth(), getHeight());
      }
  }

  @Override
  public void update(PlayingProcess gm){}
}
