package process;


import java.io.InputStream;
import java.util.Objects;

import gamemanager.GameManager;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MenuProcess extends Process {
    private Image startScreen;
    private boolean checkEndCRTEffect;
    private boolean check = true;
    private int buttonStage = 0;
    private Button startButton;
    private Button exitButton;
    private Button settingButton;

    public MenuProcess(int width, int height) {
        super(width, height);
        try {
            InputStream imageStream = getClass().getResourceAsStream("/image/BackgroundMenu.png");

            if (imageStream == null) {
                throw new IllegalArgumentException("Lỗi: Không tìm thấy tài nguyên ảnh tại '/image/BackgroundMenu.png'");
            }

            startScreen = new Image(imageStream);

        } catch (Exception e) {
            System.err.println("Không thể tải ảnh menu. Game có thể sẽ không hiển thị đúng.");
            e.printStackTrace();
            startScreen = null;
        }
        checkEndCRTEffect = false;
        buttonStage = 0;
    }

    @Override
    public void update(Stage stage, GameManager gameManager) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case UP:
                    buttonStage = (buttonStage + 1) % 2;
                    break;
                case DOWN:
                    buttonStage = (buttonStage - 1) % 2;
                    if(buttonStage < 0) {
                        buttonStage = 1;
                    }
                    break;
                case ENTER:
                    System.exit(0);
                    break;
            }
        });
        if (checkEndCRTEffect && !check) {
            addMenuBackground(stage, gameManager);
            addStartButton(stage, gameManager);
            addSettingButton(stage, gameManager);
            addExitButton(stage, gameManager);
            buttonStage = 0;
            check = true;
        }

    }


    private void CRTTvShow(){
        Rectangle flash = new Rectangle(width, 5, Color.WHITE);
        flash.setArcWidth(20);
        flash.setArcHeight(20);
        this.pane.getChildren().add(flash);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2), flash);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition expandY = new ScaleTransition(Duration.seconds(1.0), flash);
        expandY.setFromY(1);
        expandY.setToY(150);

        SequentialTransition tvOn = new SequentialTransition(fadeIn, expandY);
        tvOn.setOnFinished(e -> {
            this.pane.getChildren().remove(flash);
            check = false;
        });
        tvOn.play();
    }

    private void addMenuBackground(Stage stage, GameManager gameManager) {
        ImageView imageView = new ImageView();
        imageView.setImage(startScreen);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        this.pane.getChildren().add(imageView);
    }

    private void addSettingButton(Stage stage, GameManager gameManager) {
        settingButton = new Button("Setting");
        settingButton.setPrefWidth(250);
        settingButton.setPrefHeight(60);
        settingButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 200;");
        StackPane.setAlignment(settingButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(settingButton, new Insets(0, 360, 180, 0));

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/OptionButton.png")));
        ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, 250, 60));
        settingButton.setGraphic(imageView);

        // Hiệu ứng phóng to
        ScaleTransition st = new ScaleTransition(Duration.millis(200), settingButton);

        //  Lắng nghe focus
        settingButton.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) { // Khi được focus
                st.stop();
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, 60, 250, 60));
            } else { // Khi mất focus
                st.stop();
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
                imageView.setViewport(new Rectangle2D(0, 0, 250, 60));
            }
        });

        settingButton.setOnMouseClicked(e -> {
            System.out.println("This mode is not completed");
            System.exit(0);
        });
        settingButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.out.println("This mode is not completed");
                System.exit(0);
            }
        });

        pane.getChildren().add(settingButton);
    }

    private void addStartButton(Stage stage, GameManager gameManager) {
        startButton = new Button("Start");
        startButton.setPrefWidth(250);
        startButton.setPrefHeight(60);
        startButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 200;");
        StackPane.setAlignment(startButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(startButton, new Insets(0, 360, 260, 0));

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/Sprite-0001.png")));
        ImageView imageView2 = new ImageView(image);
        imageView2.setViewport(new Rectangle2D(0, 0, 250, 60));
        startButton.setGraphic(imageView2);

        // Hiệu ứng phóng to
        ScaleTransition st = new ScaleTransition(Duration.millis(200), startButton);

        startButton.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) { // Khi được focus
                st.stop();
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
                imageView2.setViewport(new Rectangle2D(0, 60, 250, 60));
            } else { // Khi mất focus
                st.stop();
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
                imageView2.setViewport(new Rectangle2D(0, 0, 250, 60));
            }
        });

        // Khi click chuột hoặc nhấn Enter
        startButton.setOnMouseClicked(e -> gameManager.finishMenu(stage));
        startButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                CRTTvShow();
                gameManager.finishMenu(stage);
            }
        });

        pane.getChildren().add(startButton);
    }

    private void addExitButton(Stage stage, GameManager gameManager) {
        exitButton = new Button("exit");
        exitButton.setPrefWidth(250);
        exitButton.setPrefHeight(60);
        exitButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 200;");
        StackPane.setAlignment(exitButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(exitButton, new Insets(0, 360, 100, 0));

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/QuitButton.png")));
        ImageView imageView2 = new ImageView(image);
        imageView2.setViewport(new Rectangle2D(0, 0, 250, 60));
        exitButton.setGraphic(imageView2);

        // Hiệu ứng phóng to
        ScaleTransition st = new ScaleTransition(Duration.millis(200), exitButton);

        exitButton.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) { // Khi được focus
                st.stop();
                st.setToX(1.1);
                st.setToY(1.1);
                st.playFromStart();
                imageView2.setViewport(new Rectangle2D(0, 60, 250, 60));
            } else { // Khi mất focus
                st.stop();
                st.setToX(1.0);
                st.setToY(1.0);
                st.playFromStart();
                imageView2.setViewport(new Rectangle2D(0, 0, 250, 60));
            }
        });

        // Khi click chuột hoặc nhấn Enter
        exitButton.setOnMouseClicked(e -> System.exit(0));
        exitButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                System.exit(0);
            }
        });
        pane.getChildren().add(exitButton);
    }


    @Override
    public void render() {
        if(!checkEndCRTEffect) {
            CRTTvShow();
            checkEndCRTEffect = true;
        }
    }

    public enum ButtonStage {
        STARTBUTTON,
        SETTINGBUTTON,
        EXITBUTTON
    }
}
