package gameobject.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import map.Map;
import process.PlayingProcess;
import java.util.List;

public class PushBrick extends Brick {

    private final Image brickImage = LoadImage.getImage("/image/pushBrick.png");
    private boolean pushed = false; // chỉ đẩy 1 lần sau khi bị phá

    public PushBrick(double x, double y, int locateX, int locateY) {
        super(x, y, locateX, locateY, 1);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (brickImage != null) {
            gc.drawImage(brickImage, getX(), getY(), getWidth(), getHeight());
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void takeHit() {
        if (isDestroyed())
            return;
        hitPoints--;
    }

    @Override
    public void update(PlayingProcess game) {
        if (isDestroyed() && !pushed) {
            pushed = true;
            doPushBrick(game);
        }
    }

    /**
     * Đẩy toàn bộ gạch còn lại lên 1 hàng
     */
    private void doPushBrick(PlayingProcess game) {
        List<Brick> bricks = game.getBricks();
        if (bricks == null || bricks.isEmpty())
            return;

        int maxLocateY = 0;
        for (Brick b : bricks) {
            maxLocateY = Math.max(maxLocateY, (int) b.getY());
        }

        if (maxLocateY < Map.MAP_ZONE.getY() + 50 + Brick.BRICK_HEIGHT + 6) {
            return; // không thể đẩy hết gạch ra khỏi màn chơi
        }

        // thực hiện đẩy nếu thỏa
        for (Brick b : bricks) {
            b.setY(b.getY() - Brick.BRICK_HEIGHT + 6);
        }
        System.out.println("[PushBrick] Tất cả gạch đã được đẩy lên 1 hàng!");
    }
}
