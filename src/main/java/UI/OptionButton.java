package UI;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

public class OptionButton extends Button {

    public OptionButton(String s, Insets inset, int width, int height, String filepath) {
        super(s);
        super.setPrefSize(width, height);
        super.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 200;");
        StackPane.setAlignment(this, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(this, inset);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(filepath)));
        ImageView imageView2 = new ImageView(image);
        imageView2.setViewport(new Rectangle2D(0, 0, width, height));
        super.setGraphic(imageView2);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        super.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                st.stop();
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
                imageView2.setViewport(new Rectangle2D(0, height, width, height));
            } else {
                st.stop();
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
                imageView2.setViewport(new Rectangle2D(0, 0, width, height));
            }
        });

        super.setOnMouseClicked(e -> {
            System.out.println("This mode is not completed");
            System.exit(0);
        });
        super.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println("This mode is not completed");
                System.exit(0);
            }
        });
    }
}
