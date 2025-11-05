package UI;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

public class QuitButton extends UIButton {
    public static Image image = new Image(Objects
            .requireNonNull(OptionButton.class.getResourceAsStream("/image/QuitButton.png")));
    public static ImageView imageView = new ImageView(image);

    public QuitButton(String s, Pos pos, Insets inset, int width, int height) {
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

        super.setOnMouseClicked(e -> {
            System.exit(0);
        });
        super.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.exit(0);
            }
        });
    }
}
