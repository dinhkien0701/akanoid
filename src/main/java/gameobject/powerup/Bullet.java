package gameobject.powerup;


import gameobject.GameObject;
import gameobject.MovableObject;
import gameobject.brick.Brick;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import process.PlayingProcess;

public class Bullet extends MovableObject {

    public static final double BULLET_WIDTH = 5.0;
    public static final double BULLET_HEIGHT = 15.0;
    public static final double BULLET_SPEED = -10.0;

    public Bullet(double x, double y) {
        super(x, y, BULLET_WIDTH, BULLET_HEIGHT, 0, BULLET_SPEED);
    }

    @Override
    public void update(PlayingProcess pp) {
        move();
    }

    public boolean checkCollision(GameObject brick) {
        if (!(brick instanceof Brick)) {
            return false;
        }
        return (this.getX() < brick.getX() + brick.getWidth()
                && this.getX() + this.getWidth() > brick.getX()
                && this.getY() < brick.getY() + brick.getHeight()
                && this.getY() + this.getHeight() > brick.getY());
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.LIGHTYELLOW);
        gc.fillRoundRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 5, 5);
        gc.setStroke(Color.RED);
        gc.strokeRoundRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 5, 5);
    }


    @Override
    public void resetSpeed() {
        this.setDx(0);
        this.setDy(-BULLET_SPEED);
    }
}
