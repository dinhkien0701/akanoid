package core.mode;

import core.GameManager;
import core.GameMode;
import javafx.stage.Stage;

// Ultimate: chế độ chơi đơn kiểu "ultimate"
// Dùng cho map động (hạ xuống), gạch di chuyển, bomber... (sẽ cắm sau)
public class Ultimate implements GameMode {
    @Override
    public String id() {
        return "ultimate";
    }

    @Override
    public void onEnter(Stage stage, GameManager gameManager) {
        // Khởi tạo state riêng cho Ultimate (nếu cần)
    }
}
