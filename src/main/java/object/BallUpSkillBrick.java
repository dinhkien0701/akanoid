package object;

import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import process.PlayingProcess;
import powerup.*;

public class BallUpSkillBrick extends Brick {


    public BallUpSkillBrick(double x, double y, double width, double height) {
        super(x , y , width, height , 1);
    }


    private final Image brickImage = LoadResource.LoadImage.getImage("/image/ballUpSkill.png");
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
//        Random rand = new Random();
//        int powerUpType = rand.nextInt(1);
//        switch (powerUpType) {
//            case 0:
                return(new DuplicateBallPowerUp(x, y));
//            case 1:
//                return(new BiggerBallPowerUp(x, y));
//            default:
//                return(new LongerPaddlePowerUp(x, y));
//        }
    }

    @Override
    public void update(PlayingProcess pp) {
        if(pp.isEnoughPowerUpsSpawnedThisLevel()){
            return;
        }
        pp.addPowerUp(spawnRandomPowerUp(this.getX(), this.getY()));
    }
}
