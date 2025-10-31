package gameobject.powerup;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class ShotPowerUp extends PowerUp {
    private static final double SIZE = 25;
    private static final int DURATION_SECONDS = 6000;

    public ShotPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.getPaddle().enableShooting();
    }

    @Override
    public void update(PlayingProcess pp) {
        super.update(pp);
        if (isApplying()) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - this.getTimer() >= DURATION_SECONDS) {
                pp.getPaddle().disableShooting();
                setIsEnd();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(isFalling()) {
            gc.setFill(Color.CRIMSON);
            gc.fillRect(getX() + SIZE / 3, getY(), getWidth() / 3, getHeight());
            gc.setFill(Color.YELLOW);
            gc.fillOval(getX() + SIZE / 4, getY(), getWidth() / 2, getHeight() / 3);
        }
    }
}
