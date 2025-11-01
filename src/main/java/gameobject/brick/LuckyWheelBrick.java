package gameobject.brick;

import gameobject.powerup.PowerUp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import gameobject.powerup.*;
import process.PlayingProcess;

import java.util.Random;

public class LuckyWheelBrick extends Brick {
    public LuckyWheelBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
    }

    private final Image brickImage = LoadImage.getImage("/image/luckywheel.png");

    private PowerUp spawnRandomPowerUp(double x, double y) {
        Random rand = new Random();
        int powerUpType = rand.nextInt(7);
        // int powerUpType = 6;
        switch (powerUpType) {
            case 0:
                return (new DuplicateBallPowerUp(x, y));
            case 1:
                return (new BiggerBallPowerUp(x, y));
            case 2:
                return (new LongerPaddlePowerUp(x, y));
            case 3:
                return (new ShortenPaddlePowerUp(x, y));
            case 4:
                return (new FallBoomPowerUp(x, y));
            case 5:
                return (new LifeUpPowerUp(x, y));
            case 6:
                return (new ShotPowerUp(x, y));
            default:
                return null;
        }
    }

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
    public void update(PlayingProcess pp) {
        if (pp.isEnoughPowerUpsSpawnedThisLevel()) {
            return;
        }
        pp.addPowerUp(spawnRandomPowerUp(this.getX(), this.getY()));
    }
}
