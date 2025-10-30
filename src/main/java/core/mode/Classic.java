package core.mode;

import core.GameManager;
import core.GameMode;
import javafx.stage.Stage;

// Classic: chế độ cổ điển (chơi đơn)
// Hiện tại chỉ là khung rỗng để cắm sau này.
public class Classic implements GameMode {
    @Override
    public String id() {
        return "classic";
    }

    @Override
    public void onEnter(Stage stage, GameManager gameManager) {
        // Khởi tạo riêng cho classic (nếu cần)
    }
}
