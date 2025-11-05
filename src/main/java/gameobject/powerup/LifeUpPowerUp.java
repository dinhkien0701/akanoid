package gameobject.powerup;

import gameobject.ball.Ball;
import gameobject.brick.LoadImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class LifeUpPowerUp extends PowerUp {
    private final Image powerUpImage;

    public LifeUpPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
        this.powerUpImage = LoadImage.getImage("/image/LifeUpPowerUp.png");
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.getPaddle().addLife();
        setIsEnd();
    }

    @Override
    public void update(PlayingProcess pp) {
        if (!isEnd()) {
            super.update(pp);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isFalling()) {
            if (powerUpImage != null) {
                gc.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}
