package gameobject.brick;

import gameobject.powerup.FallBoomPowerUp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class FallBombBrick extends Brick {

    public FallBombBrick (double x, double y, int locateX, int locateY) {
        super(x , y , locateX,locateY, 1);
    }


    private final Image brickImage = LoadImage.getImage("/image/fallBomb.png");
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
    public void update(PlayingProcess pp) {
        if (isDestroyed()){
            pp.addPowerUp(new FallBoomPowerUp(this.getX(),this.getY()));
        }
    }
}
