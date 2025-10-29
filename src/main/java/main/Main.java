package main;

import core.GameManager;
import javafx.application.Application;
import javafx.stage.Stage;
import process.LoadingProcess;

public class Main extends Application {

  public static final int SCREEN_WIDTH = 1200;
  public static final int SCREEN_HEIGHT = 750;

  public static void main(String[] args) {
        launch(args);
    }

  @Override
  public void start(Stage primaryStage) {
//    LoadingProcess lp = new LoadingProcess(SCREEN_WIDTH, SCREEN_HEIGHT);
//    lp.render(primaryStage, () -> {
      GameManager gameManager = new GameManager(SCREEN_WIDTH,SCREEN_HEIGHT);
      gameManager.process(primaryStage);
//    });
  }
}