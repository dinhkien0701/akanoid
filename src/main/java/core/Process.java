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

    public Process(int width, int height) {
        this.width = width;
        this.height = height;
        Canvas canvas = new Canvas(width, height);
        pane = new StackPane(canvas);
        scene = new Scene(pane, width, height, Color.BLACK);
        this.gc = canvas.getGraphicsContext2D();
    }

    public abstract void setScene(Stage stage);
    public abstract void update(Stage stage, GameManager gameManager);
    public abstract void render();
}
