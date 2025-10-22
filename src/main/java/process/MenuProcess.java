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

public class MenuProcess {
    private Image startScreen;
    private final int height, width;
    private final GraphicsContext gc;
    private final StackPane menuPane;
    private final Scene menuScene;

    public MenuProcess(int width, int height, GraphicsContext gc) {
        this.width = width;
        this.height = height;
        this.gc = gc;
        try {
            InputStream imageStream = getClass().getResourceAsStream("/image/startGame.png");

            if (imageStream == null) {
                throw new IllegalArgumentException("Lỗi: Không tìm thấy tài nguyên ảnh tại '/image/startGame.png'");
            }

            startScreen = new Image(imageStream);

        } catch (Exception e) {
            System.err.println("Không thể tải ảnh menu. Game có thể sẽ không hiển thị đúng.");
            e.printStackTrace();
            startScreen = null;
        }
        menuPane = new StackPane();
        menuScene = new Scene(menuPane, width, height, Color.BLACK);
    }

    public void setScene(Stage stage) {
        stage.setScene(menuScene);
    }

    public void update(Scene scene, GameManager mn) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.ENTER) {
                mn.finishMenu();
            } else if(code == KeyCode.ESCAPE) {
              System.exit(0);
            }
        });
    }

    public void CRTTvShow(Rectangle flash, StackPane root, Stage stage){

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2), flash);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

 
        ScaleTransition expandY = new ScaleTransition(Duration.seconds(0.5), flash);
        expandY.setFromY(1);
        expandY.setToY(150); // giãn mạnh lên để phủ màn hình

//        // 3️⃣ Làm sáng dần toàn bộ
//        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), flash);
//        fadeOut.setFromValue(1);
//        fadeOut.setToValue(0);

        SequentialTransition tvOn = new SequentialTransition(fadeIn, expandY);
        tvOn.setOnFinished(e -> {
           ImageView imageView = new ImageView();
            imageView.setImage(startScreen);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            menuPane.getChildren().add(imageView);
        });

        tvOn.play();
    }


    public void render(Stage stage) {

        Rectangle flash = new Rectangle(width, 5, Color.WHITE);
        flash.setArcWidth(20);
        flash.setArcHeight(20);
        menuPane.getChildren().add(flash);

        CRTTvShow(flash, menuPane, stage);
    }
}
