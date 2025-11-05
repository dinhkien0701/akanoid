package gameobject.brick;

import gameobject.powerup.GoldPowerUp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class GoldBrick extends Brick {
    public GoldBrick(double x, double y, int locateX, int locateY) {
        super(x , y , locateX, locateY , 1);
    }


    private Image brickImage = LoadImage.getImage("/image/gold.png");
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
        if(isDestroyed()) {
            pp.addPowerUp(new GoldPowerUp(this.getX(), this.getY()));
        }
    }
}
