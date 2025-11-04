package UI;

import javafx.animation.ScaleTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;

public class StartButton extends Button {
    private ImageView imageView;
    private Image image;

    public StartButton(String s, int x, int y, int width, int height, String filepath) {
        super(s);
        super.setPrefWidth(x);
        super.setPrefHeight(y);
        super.setStyle(
                "-fx-background-color: #0000; " +
                        "-fx-background-radius: 200;");
        image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filepath)));
        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        super.setGraphic(imageView);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);

        this.setOnMouseEntered(e -> {
            st.setToX(1.1);
            st.setToY(1.1);
            st.playFromStart();
            this.setFocusTraversable(true);
            imageView.setViewport(new Rectangle2D(0,540,250,60));
        });
        this.setOnMouseExited(e -> {
            st.setToX(1.0);
            st.setToY(1.0);
            st.playFromStart();
            this.setFocusTraversable(false);
            imageView.setViewport(new Rectangle2D(0,0,250,60));
        });
    }

}
