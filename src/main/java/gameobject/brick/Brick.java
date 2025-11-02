package gameobject.brick;


import gameobject.GameObject;
import javafx.scene.canvas.GraphicsContext;
import process.PlayingProcess;

public abstract class Brick extends GameObject {

    private int hitPoints;
        Brick(double x, double y, double w, double h, int hp) {
        super(x, y, w, h);
        this.hitPoints = hp;
    }

    public void takeHit() {
        hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }

    public abstract void render(GraphicsContext gc);

    public abstract void update(PlayingProcess gameManager);
}