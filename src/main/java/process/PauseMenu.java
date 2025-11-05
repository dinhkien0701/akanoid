package process;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PauseMenu extends StackPane {

    private boolean isVisible = false;

    /**
     * @param onResume  Runnable khi nhấn Resume
     * @param onReplay  Runnable khi nhấn Replay
     * @param onQuit    Runnable khi nhấn Quit
     */
    public PauseMenu(Runnable onResume, Runnable onReplay, Runnable onQuit) {
        this.setStyle("-fx-background-color: rgba(0,0,0,0.5);"); // nền mờ
        this.setVisible(false); // ẩn mặc định

        VBox menuBox = new VBox(15); // khoảng cách giữa các nút
        menuBox.setAlignment(Pos.CENTER);

        // Nút Resume
        Button resume = new Button("Resume");
        resume.setOnAction(e -> {
            toggle();
            if(onResume != null) onResume.run();
        });

        // Nút Replay
        Button replay = new Button("Replay");
        replay.setOnAction(e -> {
            toggle();
            if(onReplay != null) onReplay.run();
        });

        // Nút Quit
        Button quit = new Button("Quit");
        quit.setOnAction(e -> {
            toggle();
            if(onQuit != null) onQuit.run();
        });

        menuBox.getChildren().addAll(resume, replay, quit);
        this.getChildren().add(menuBox);

        // cho phép bắt phím ESC / P để toggle menu
        this.setFocusTraversable(true);
        this.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.P) {
                toggle();
                if(!isVisible && onResume != null) onResume.run();
            }
        });
    }

    /** Hiển thị / ẩn menu */
    public void toggle() {
        isVisible = !isVisible;
        this.setVisible(isVisible);
        if(isVisible) this.requestFocus();
    }

    public boolean isShowing() { return isVisible; }
    public void show() { isVisible = true; this.setVisible(true); this.requestFocus(); }
    public void hide() { isVisible = false; this.setVisible(false); }
}
