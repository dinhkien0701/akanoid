package UI;

import gamemanager.GameManager;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class ReplayButton extends UIButton {
    private static final Image image = new Image(Objects
            .requireNonNull(ReplayButton.class.getResourceAsStream("/image/ReplayButton.png")));
    private final ImageView imageView = new ImageView(image);

    public ReplayButton(String s, Pos pos, Insets inset, int width, int height,
            GameManager gameManager, Stage stage) {
        super(s, pos, inset, width, height);
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        super.setGraphic(imageView);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        this.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, height, width, height));
            } else {
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, 0, width, height));
            }
        });

        this.setOnMouseClicked(e -> gameManager.rePlay(stage));
        this.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                gameManager.rePlay(stage);
            }
        });
    }
}
