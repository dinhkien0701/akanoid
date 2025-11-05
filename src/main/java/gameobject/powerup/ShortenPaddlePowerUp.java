package gameobject.powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import gameobject.brick.LoadImage;
import process.PlayingProcess;

public class ShortenPaddlePowerUp extends PowerUp {

    private final Image powerUpImage;

    public ShortenPaddlePowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
        this.powerUpImage = LoadImage.getImage("/image/ShortenPaddlePowerUp.png");
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.getPaddle().shrink();
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
