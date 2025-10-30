package map;
import java.util.Random;

/**
 * CreateMap: sinh bản đồ nền tảng (gạch thường) theo level.
 *
 * Cách làm: tính số lượng gạch đỏ (normal = id 1) cần có theo level,
 * sau đó gọi DFS nhiều lần tại các vị trí ngẫu nhiên để tạo thành các cụm.
 * Chỉ điền vào những ô đang trống (map[i][j] == 0).
 */
public class CreateMap {
    int rows;   // số hàng của lưới
    int cols;   // số cột của lưới
    int level;  // cấp độ hiện tại

    public CreateMap(int rowls , int cols, int level) {
        this.rows = rowls;
        this.cols = cols;
        this.level = level;
    }

    /**
     * Quy đổi level -> số viên gạch thường (id=1) mong muốn.
     * Tăng dần theo các ngưỡng level, có trần 100 viên để giữ cân bằng.
     */
    private int getCountBrickforLevel() {

        // chỉ tính cho gạch đỏ (normal)
        int count = 0;
        if (level >= 1) {
            count += 2 * level; // mỗi level +2 viên cơ bản
        }
        if (level >= 3) {
            count += level;     // từ level 3: cộng thêm
        }
        if (level >= 5) {
            count += level;     // từ level 5: cộng thêm lần nữa
        }
        if (level >= 7) {
            count += level;     // từ level 7: cộng thêm (tăng nhanh hơn)
        }
        if (level >= 10) {
            count += level;     // từ level 10: cộng thêm
        }

        return Math.min(count, 100); // trần 100 viên
    }

    /**
     * Điền map bằng gạch thường (id=1) theo quota n, dùng DFS để tạo cụm.
     * @param map lưới 0/1 (0 trống, 1 là gạch normal)
     * @return map đã được điền thêm
     */
    public int[][] creatMap( int[][] map) {
        int n = getCountBrickforLevel(); // quota gạch cần thêm

        // m = số ô trống hiện có trong map
        int m = 0;
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < cols ; j++) {
                if (map[i][j] == 0) {
                    m++;
                }
            }
        }

        n = Math.min(n, m); // không vượt quá số ô trống
        // lưới mặc định: w = 13, h = 8

        DFS dfs = new DFS(rows, cols);
        // thử tối đa 50 điểm ngẫu nhiên để gieo cụm
        for (int i = 1; i < 50; i++) {
            Random rand  = new Random();
            int x = rand.nextInt(rows);
            int y = rand.nextInt(cols);
            if (map[x][y] == 0 && n > 0) {
                n = dfs.createMap(map, x, y, 1 , n); // id=1 (normal)
                // nhận lại n sau khi đã trừ số ô đã tô
            }
        }

        return map;
    }
}
