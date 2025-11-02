package gameobject.powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class LongerPaddlePowerUp extends PowerUp {


    public LongerPaddlePowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.getPaddle().extend();
        setIsEnd();
    }

    @Override
    public void update(PlayingProcess pp) {
        if(!isEnd()) {
            super.update(pp);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(isFalling()) {
            gc.setFill(Color.ORANGE);
            gc.fillRect(getX(), getY() + SIZE / 3, getWidth(), getHeight() / 2);
            gc.setFill(Color.BLACK);
            gc.fillText("<-->", getX() + 3, getY() + 17);
        }
    }
}
