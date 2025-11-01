package gameobject.paddle;

import gameobject.MovableObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import process.PlayingProcess;

public class Paddle extends MovableObject {
    private static final double PADDLE_SPEED = 10.0;
    private int lives;
    private boolean canShoot = false;

    private double speed = 10.0;
    private static double normalWidth = 100;
    private static double smallWidth = normalWidth * 0.5;
    private static double extendedWidth = normalWidth * 2.5;
    private static double paddleHeight = 16;

    private enum SizeState {
        SHORT, NORMAL, EXTENDED
    }

    private SizeState sizeState;

    public Paddle(double x, double y) {
        super(x, y, normalWidth, paddleHeight, 0, 0);
        lives = 3;
        sizeState = SizeState.NORMAL;
    }

    @Override
    public void resetSpeed() {
        super.setDx(0);
        super.setDy(0);
    }

    public void takeHit() {
        lives--;
    }

    public int getLives() {
        return this.lives;
    }

    public void setPaddleWidth(double w) {
        this.setWidth(w);
    }

    public void setPaddleHeight(double h) {
        this.setHeight(h);
    }

    public void setSpeed(double s) {
        this.speed = s;
    }

    public void moveLeft() {
        // System.out.println("Left");
        dx = -speed;
    }

    public void moveRight() {
        // System.out.println("Right");
        dx = speed;
    }

    public void reborn() {
        this.lives = 3;
        setSize(SizeState.NORMAL);
    }

    public void addLife() {
        this.lives++;
    }

    public void stop() {
        dx = 0;
    }

    public void extend() {
        if (sizeState == SizeState.SHORT) {
            setSize(SizeState.NORMAL);
        } else if (sizeState == SizeState.NORMAL) {
            setSize(SizeState.EXTENDED);
        }
    }

    public void shrink() {
        if (sizeState == SizeState.EXTENDED) {
            this.setSize(SizeState.NORMAL);
        } else if (sizeState == SizeState.NORMAL) {
            this.setSize(SizeState.SHORT);
        }
    }

    public void resetSize() {
        if (this.getWidth() == extendedWidth) {
            double currentCenterX = this.getX() + this.getWidth() / 2;
            this.setWidth(normalWidth);
            this.setX(currentCenterX - this.getWidth() / 2);
        }
    }

    private void setSize(SizeState newState) {
        double currentCenterX = this.getX() + this.getWidth() / 2;
        this.sizeState = newState;
        switch (newState) {
            case SHORT:
                this.setPaddleWidth(smallWidth);
                break;
            case NORMAL:
                this.setPaddleWidth(normalWidth);
                break;
            case EXTENDED:
                this.setPaddleWidth(extendedWidth);
                break;
        }
        this.setX(currentCenterX - this.getWidth() / 2);
    }

    public void enableShooting() {
        this.canShoot = true;
    }

    public void disableShooting() {
        this.canShoot = false;
    }

    public boolean canShoot() {
        return this.canShoot;
    }

    @Override
    public void update(PlayingProcess gm) {
        move();
        if (getX() < gm.getMap().getX()) {
            this.setX(gm.getMap().getX());
        }
        if (getX() + getWidth() > gm.getMap().getWidth() + gm.getMap().getX()) {
            this.setX(gm.getMap().getWidth() + gm.getMap().getX() - this.getWidth());
        }
        if (lives <= 0) {
            gm.deadPaddle();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}