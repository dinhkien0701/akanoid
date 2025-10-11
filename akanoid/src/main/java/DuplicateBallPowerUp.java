import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DuplicateBallPowerUp extends PowerUp {

    private static final double SIZE = 20;

    public DuplicateBallPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    void applyEffect(GameManager gm) {
        gm.duplicateBalls();
    }

    @Override
    void render(GraphicsContext gc) {
        gc.setFill(Color.GREENYELLOW);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
        gc.setFill(Color.BLACK);
        gc.fillText("2x", getX() + 5, getY() + 14);
    }
}