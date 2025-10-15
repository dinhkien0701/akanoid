package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import process.PlayingProcess;

import java.io.InputStream;

public class DuplicateBallPowerUp extends PowerUp {

    private static final double SIZE = 25;
    private static Image powerUpImage;
    static {
        try {
            InputStream imageStream = DuplicateBallPowerUp.class.getResourceAsStream("/images/powerup_2x.png");
            if (imageStream == null) {
                throw new IllegalArgumentException("Không tìm thấy file ảnh: /images/powerup_2x.png");
            }
            powerUpImage = new Image(imageStream);
        } catch (Exception e) {
            System.err.println("Lỗi tải ảnh cho power-up nhân đôi bóng.");
            powerUpImage = null;
        }
    }

    public DuplicateBallPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void applyEffect(PlayingProcess pp) {
        pp.duplicateBalls();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (powerUpImage != null) {
            gc.drawImage(powerUpImage, getX(), getY(), getWidth(), getHeight());
        } else {
            gc.setFill(javafx.scene.paint.Color.GREENYELLOW);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        }
    }
}