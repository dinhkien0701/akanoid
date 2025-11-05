package UI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PauseMenu extends StackPane {

    private boolean isVisible = false;
    private VBox menuBox;

    public PauseMenu(Runnable onResume, Runnable onRestart, Runnable onQuit) {
        this.setStyle("-fx-background-color: rgba(0,0,0,0.5);"); // mờ nhẹ
        this.setVisible(false); // ẩn lúc đầu

        menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);

        Button resume = new Button("Resume");
        Button restart = new Button("Restart");
        Button quit = new Button("Quit");

        resume.setOnAction(e -> { toggle(); if(onResume!=null) onResume.run(); });
        restart.setOnAction(e -> { toggle(); if(onRestart!=null) onRestart.run(); });
        quit.setOnAction(e -> { if(onQuit!=null) onQuit.run(); });

        menuBox.getChildren().addAll(resume, restart, quit);
        this.getChildren().add(menuBox);

        this.setFocusTraversable(true);
        this.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.P) {
                toggle();
                if(!isVisible && onResume!=null) onResume.run();
            }
        });
    }

    public void toggle() {
        isVisible = !isVisible;
        this.setVisible(isVisible);
        if(isVisible) this.requestFocus(); // nhận phím
    }

    public boolean isShowing() { return isVisible; }

    public void show() { isVisible = true; this.setVisible(true); this.requestFocus(); }
    public void hide() { isVisible = false; this.setVisible(false); }
}
