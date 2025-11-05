package UI;

import gamemanager.GameManager;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class BackButton extends UIButton {
    private static final Image image = new Image(
            Objects.requireNonNull(BackButton.class.getResourceAsStream("/image/BackButton.png")));

    private final int frameWidth;
    private final int frameHeight;
    private final ImageView imageView;
    private AudioClip clickSound;

    public BackButton(String text, Pos pos, Insets inset, int width, int height, GameManager gm,
                      Stage stage) {
        this(text, pos, inset, width, height, () -> gm.LeadToMenu(stage));
    }

    public BackButton(String text, Pos pos, Insets inset, int width, int height,
                      Runnable backAction) {
        super(text, pos, inset, width, height);
        this.frameWidth = width;
        this.frameHeight = height;

        imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        super.setGraphic(imageView);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        super.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                st.stop();
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, height, width, height)); // đổi hình hover
            } else {
                st.stop();
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, 0, width, height)); // hình bình thường
            }
        });

        // Tải âm thanh click
        try {
            clickSound = new AudioClip(Objects
                    .requireNonNull(getClass().getResource("/sound/click.wav")).toExternalForm());
        } catch (Exception e) {
            System.out.println("Không tìm thấy file click.wav");
        }

        // Click chuột hoặc nhấn Enter
        super.setOnMouseClicked(e -> {
            GlobalSound.play(clickSound);
            backAction.run();
        });
        super.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                GlobalSound.play(clickSound);
                backAction.run();
            }
        });
    }

}