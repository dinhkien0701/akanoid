package gameobject.powerup;

import gameobject.brick.Brick;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShotPowerUp extends PowerUp {
    private static final long SHOT_COOLDOWN = 300;

    private final List<Bullet> bullets;
    private long currentTime;
    private long lastShotTime;

    public ShotPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
        bullets = new ArrayList<>();
        lastShotTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();
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
                bullets.clear();
                setIsEnd();
            } else {
                currentTime = System.currentTimeMillis();
                if(currentTime - lastShotTime > SHOT_COOLDOWN) {
                    bullets.add(new Bullet(pp.getPaddle().getX(), pp.getPaddle().getY()));
                    bullets.add(new Bullet(pp.getPaddle().getX() + pp.getPaddle().getWidth() - Bullet.BULLET_WIDTH,
                            pp.getPaddle().getY()));
                    lastShotTime = System.currentTimeMillis();
                }
                Iterator<Bullet> bullet_it = bullets.iterator();
                while (bullet_it.hasNext()) {
                    Bullet bullet = bullet_it.next();
                    boolean bulletHit = false;
                    Iterator<Brick> brick_it = pp.getBricks().iterator();
                    while (brick_it.hasNext()) {
                        Brick brick = brick_it.next();
                        if (bullet.checkCollision(brick)) {
                            brick.takeHit();
                            if (brick.isDestroyed()) {
                                brick.update(pp);
                                brick_it.remove();
                            }
                            bulletHit = true;
                            break;
                        }
                    }
                    if (bulletHit || bullet.getY() < 0) {
                        bullet_it.remove();
                    } else {
                        bullet.update(pp);
                    }
                }

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
        } else {
            for(Bullet bullet : bullets) {
                bullet.render(gc);
            }
        }
    }
}
