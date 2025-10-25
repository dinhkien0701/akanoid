package process;

import core.GameManager;
import java.io.InputStream; // Import thêm InputStream

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import core.Process;

public class MenuProcess extends Process {
    private Image startScreen;
    private boolean checkEndCRTEffect;

    public MenuProcess(int width, int height) {
        super(width, height);
        try {
            InputStream imageStream = getClass().getResourceAsStream("/image/startGame.png");

            if (imageStream == null) {
                throw new IllegalArgumentException(
                        "Lỗi: Không tìm thấy tài nguyên ảnh tại '/image/startGame.png'");
            }

            startScreen = new Image(imageStream);

        } catch (Exception e) {
            System.err.println("Không thể tải ảnh menu. Game có thể sẽ không hiển thị đúng.");
            e.printStackTrace();
            startScreen = null;
        }
        checkEndCRTEffect = false;
    }

    public void setScene(Stage stage) {
        stage.setScene(this.scene);
    }


    public void update(Stage stage, GameManager gameManager) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.ENTER) {
                gameManager.finishMenu(stage);
            } else if (code == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
    }

    public void CRTTvShow(Rectangle flash) {

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2), flash);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);


        ScaleTransition expandY = new ScaleTransition(Duration.seconds(1.0), flash);
        expandY.setFromY(1);
        expandY.setToY(150);


        SequentialTransition tvOn = new SequentialTransition(fadeIn, expandY);
        tvOn.setOnFinished(e -> {
            ImageView imageView = new ImageView();
            imageView.setImage(startScreen);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            this.pane.getChildren().add(imageView);
        });
        checkEndCRTEffect = true;
        tvOn.play();
    }

    @Override
    public void render() {
        if (!checkEndCRTEffect) {
            Rectangle flash = new Rectangle(width, 5, Color.WHITE);
            flash.setArcWidth(20);
            flash.setArcHeight(20);
            this.pane.getChildren().add(flash);
            CRTTvShow(flash);
        }
    }
}
