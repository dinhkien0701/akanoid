package UI;

import gamemanager.GameManager; // 1. Thêm import GameManager
import javafx.stage.Stage; // 2. Thêm import Stage
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.Objects;

public class TutorialButton extends UIButton {
    private static final Image image = new Image(Objects
            .requireNonNull(TutorialButton.class.getResourceAsStream("/image/Tutorial.png")));
    private final ImageView imageView = new ImageView(image);

    public TutorialButton(String s, Pos pos, Insets inset, int width, int height,
            GameManager gameManager, Stage stage) {
        super(s, pos, inset, width, height);

        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        super.setGraphic(imageView);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        super.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                st.stop();
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, height, width, height));
            } else {
                st.stop();
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, 0, width, height));
            }
        });

        super.setOnMouseClicked(e -> gameManager.LeadToTutorial(stage));
        super.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                gameManager.LeadToTutorial(stage);
        });
    }

}
