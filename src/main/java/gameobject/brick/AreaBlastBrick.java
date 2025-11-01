package gameobject.brick;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class AreaBlastBrick extends Brick {

    public AreaBlastBrick(double x, double y, double width, double height) {
        super(x , y , width, height , 1);
    }


    private final Image brickImage = LoadImage.getImage("/image/areaBlast.png");
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
    public void update(PlayingProcess gameManager) {}
}
