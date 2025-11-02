package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public abstract class UIButton extends Button {

    public UIButton(String s, Pos pos, Insets inset, int width, int height) {
        super(s);
        super.setPrefSize(width, height);
        super.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 200;");
        StackPane.setAlignment(this, pos);
        StackPane.setMargin(this, inset);

    }
}
