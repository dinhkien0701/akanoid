package core.mode;

import core.GameManager;
import core.GameMode;
import javafx.stage.Stage;

// ClassicPvp: chế độ cổ điển 2 người (PvP)
// Tạm thời là khung rỗng, sẽ nối mạng/đồng bộ sau.
public class ClassicPvp implements GameMode {
    @Override
    public String id() {
        return "classicpvp";
    }

    @Override
    public void onEnter(Stage stage, GameManager gameManager) {
        // Khởi tạo riêng cho PvP cổ điển (nếu cần)
    }
}
