package gameobject.brick;

import gameobject.powerup.DuplicateBallPowerUp;
import gameobject.powerup.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;

import java.util.Random;

public class LuckyWheelBrick extends Brick {
    public LuckyWheelBrick(double x, double y, int locateX, int locateY) {
        super(x, y, locateX, locateY, 1);
    }


    private final Image brickImage = LoadImage.getImage("/image/luckywheel.png");

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

    private PowerUp spawnRandomPowerUp(double x, double y) {
        Random rand = new Random();
        int powerUpType = rand.nextInt(6);
        // int powerUpType = 4;
        return switch (powerUpType) {
            case 0 -> (new DuplicateBallPowerUp(x, y));
            case 1 -> (new BiggerBallPowerUp(x, y));
            case 2 -> (new LongerPaddlePowerUp(x, y));
            case 3 -> (new ShortenPaddlePowerUp(x, y));
            case 4 -> (new FallBoomPowerUp(x, y));
            case 5 -> (new LifeUpPowerUp(x, y));
            case 6 -> (new ShotPowerUp(x, y));
            case 7 -> (new GoldPowerUp(x, y));
            default -> null;
        };
    }

    @Override
    public void takeHit() {
        hitPoints--;
    }

    @Override
    public void update(PlayingProcess pp) {
        if (pp.isEnoughPowerUpsSpawnedThisLevel()) {
            return;
        }
        pp.addPowerUp(spawnRandomPowerUp(this.getX(), this.getY()));
    }
}
