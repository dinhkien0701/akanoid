package process;

import core.GameManager;
import java.io.InputStream; // Import thêm InputStream
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class MenuProcess {
    private Image startScreen; // Không khởi tạo final nữa để có thể xử lý lỗi
    private final int height, width;
    private int frame;
    private final GraphicsContext gc;

    public MenuProcess(int width, int height, GraphicsContext gc) {
        this.width = width;
        this.height = height;
        this.gc = gc;
        this.frame = height;

        // Sử dụng try - cat để bắt lỗi nhận file ảnh
        try {
            // Lấy tài nguyên từ classpath dưới dạng một luồng dữ liệu (InputStream)
            InputStream imageStream = getClass().getResourceAsStream("/image/startGame.png");

            // Nếu imageStream là null, nghĩa là không tìm thấy file
            if (imageStream == null) {
                // Ném ra một ngoại lệ để báo lỗi rõ ràng
                throw new IllegalArgumentException("Lỗi: Không tìm thấy tài nguyên ảnh tại '/image/startGame.png'");
            }

            // Nếu tìm thấy, tạo đối tượng Image từ luồng dữ liệu đó
            startScreen = new Image(imageStream);

        } catch (Exception e) {
            // Bắt tất cả các lỗi có thể xảy ra (bao gồm cả IllegalArgumentException ở trên)
            System.err.println("Không thể tải ảnh menu. Game có thể sẽ không hiển thị đúng.");
            // In ra chi tiết lỗi để gỡ rối
            e.printStackTrace();
            // Gán startScreen là null để tránh lỗi NullPointerException ở hàm render
            // Hoặc bạn có thể tạo một ảnh trống thay thế
            startScreen = null;
        }
        // ✅ Kết thúc phần xử lý lỗi
    }

    public void update(Scene scene, GameManager mn) {
        frame -= 10;
        frame = Math.max(frame, 0);
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.ENTER) {
                mn.finishMenu();
            } else if(code == KeyCode.ESCAPE) {
              System.exit(0);
            }
        });
    }

    public void render() {
        // Rất quan trọng: Kiểm tra xem ảnh đã được tải thành công chưa trước khi vẽ
        if (startScreen != null) {
            gc.drawImage(startScreen, 0, frame, this.width, this.height);
        } else {
            // (Tùy chọn) Bạn có thể vẽ một hình chữ nhật màu đen để báo hiệu ảnh bị lỗi
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.fillRect(0, 0, this.width, this.height);
        }
    }
}