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

public class OptionButton extends UIButton {
    public static Image image = new Image(Objects.requireNonNull(OptionButton.class.getResourceAsStream("/image/OptionButton.png")));
    public static ImageView imageView = new ImageView(image);

    public OptionButton(String s, Pos pos, Insets inset, int width, int height, GameManager gameManager, Stage stage) {
        super(s, pos, inset,width,height);
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

        super.setOnMouseClicked(e -> {
            gameManager.LeadToOption(stage);
        });
        super.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                gameManager.LeadToOption(stage);
            }
        });
    }
}
