package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class LifeUpBrick extends Brick{
    public LifeUpBrick(double x, double y, double width, double height , int locateX, int locateY) {
        super(x , y , width, height , locateX, locateY, 1);
    }


    private Image brickImage = LoadResource.LoadImage.getImage("/image/lifeup.png");
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
    public void takeHit() {
        hitPoints --;
    }

    @Override
    public void update(PlayingProcess gameManager) {}
}
