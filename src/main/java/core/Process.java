package core;

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

    public Process(int width, int height) {
        this.width = width;
        this.height = height;
        Canvas canvas = new Canvas(width, height);
        pane = new StackPane(canvas);
        scene = new Scene(pane, width, height, Color.BLACK);
        this.gc = canvas.getGraphicsContext2D();
        isEnd = false;
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
