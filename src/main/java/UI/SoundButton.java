package UI;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import java.util.Objects;

public class SoundButton extends UIButton {
    private static final Image image = new Image(Objects
            .requireNonNull(SoundButton.class.getResourceAsStream("/image/SoundButton.png")));
    private final ImageView imageView = new ImageView(image);

    private final int frameWidth;
    private final int frameHeight;

    public SoundButton(String s, Pos pos, Insets inset, int width, int height) {
        super(s, pos, inset, width, height);
        this.frameWidth = width;
        this.frameHeight = height;

        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        super.setGraphic(imageView);

        // Hiệu ứng phóng to khi focus
        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        super.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                st.stop();
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
            } else {
                st.stop();
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
            }
        });

        // Click chuột / Enter
        super.setOnMouseClicked(e -> toggleSound());
        super.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                toggleSound();
        });
    }

    private void toggleSound() {
        GlobalSound.toggle();
        if (GlobalSound.isEnabled()) {
            imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight)); // hình bật
        } else {
            imageView.setViewport(new Rectangle2D(0, frameHeight, frameWidth, frameHeight)); // hình
                                                                                             // tắt
        }
    }
}
