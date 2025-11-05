package process;

import gamemanager.GameManager;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class Process {
    protected int width, height;
    protected StackPane pane;
    protected GraphicsContext gc;
    protected Scene scene;
    protected boolean isEnd;
    private boolean hasButton;

    public Process(int width, int height) {
        this.width = width;
        this.height = height;
        pane = new StackPane();
        scene = new Scene(pane, width, height, Color.BLACK);
        this.gc = null;
        isEnd = false;
        hasButton = false;
    }


    public boolean isAddButton() {
        return this.hasButton;
    }

    public void addButtonDone() {
        this.hasButton = true;
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setScene(Stage stage) {
        stage.setScene(this.scene);
    }

    public abstract void update(Stage stage, GameManager gameManager);

    public abstract void render();
}
