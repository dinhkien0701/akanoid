package main;

import core.GameManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Tạo đối tượng GameManager ngay từ đầu, nhưng chưa chạy nó
        GameManager gameManager = new GameManager();

        // Cố gắng tìm và phát video giới thiệu
        try {
            URL resource = getClass().getResource("/video/intro.mp4"); // Đảm bảo bạn đã tạo thư mục video
            if (resource == null) {
                throw new Exception("Không tìm thấy file video intro.mp4");
            }

            Media media = new Media(resource.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Cài đặt để video vừa với cửa sổ
            mediaView.fitWidthProperty().bind(primaryStage.widthProperty());
            mediaView.fitHeightProperty().bind(primaryStage.heightProperty());

            // Tạo Scene cho video
            StackPane videoRoot = new StackPane(mediaView);
            Scene videoScene = new Scene(videoRoot, 800, 600); // Kích thước ban đầu

            // QUAN TRỌNG: Lắng nghe khi video kết thúc
            mediaPlayer.setOnEndOfMedia(() -> {
                // Chuyển sang luồng UI chính để thay đổi Scene và bắt đầu game
                Platform.runLater(() -> gameManager.process(primaryStage));
            });

            // Thiết lập cửa sổ và bắt đầu phát video
            primaryStage.setTitle("My Game");
            primaryStage.setScene(videoScene);
            primaryStage.show();
            mediaPlayer.play();

        } catch (Exception e) {
            // Nếu có bất kỳ lỗi nào với video (không tìm thấy, định dạng sai, v.v.)
            // thì sẽ chạy game ngay lập tức
            System.err.println("Lỗi khi tải video: " + e.getMessage());
            System.err.println("Bắt đầu game ngay lập tức...");
            gameManager.process(primaryStage);
        }
    }
}