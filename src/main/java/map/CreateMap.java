package map;
import java.util.Random;

public class CreateMap {
    int rows;
    int cols;
    int level;
    public CreateMap(int rowls , int cols, int level) {
        this.rows = rowls;
        this.cols = cols;
        this.level = level;
    }

    private int getCountBrickforLevel() {

        //đây chỉ tạo gạch đỏ thôi ( =gạch thường normal )
        int count = 0;
        if (level >= 1) {
            count += 2 * level;
            // mỗi level tăng ít nhất hai viên gạch
        }
        if (level >= 3) {
            count += level;
            // tu level 3 tang gap 3
        }
        if (level >= 5) {
            count += level;
            // tu level thu 5 co 20 vien gach
        }



        if (level >= 7) {
            count += level;
            // tu level thu 7 co 28 vien gach ( gap 4)
        }

        if (level >= 10) {
            count += level;
            // tu level thu 10 co 50 vien gach
        }

        return Math.min(count, 100);
        // toi da 100 vien gach
    }

    public int[][] creatMap( int[][] map) {
        int n = getCountBrickforLevel(); // lấy số lượng

        int m = 0;
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < cols ; j++) {
                if (map[i][j] == 0) {
                    m++;
                }
            }
        }

        n = Math.min(n,m);
        // bản dô sẽ dạng  w = 13  , h = 8


        DFS dfs = new DFS(rows, cols);
        for (int i = 1; i < 50; i++) {
            Random rand  = new Random();
            int x = rand.nextInt(rows) ;
            int y = rand.nextInt(cols) ;
            if(map[x][y] == 0 && n > 0) {
                n = dfs.createMap(map, x, y, 1 , n);
                // Lấy lại n sau khi đã trừ số ô đã tạo
            }
        }

        return map;
    }
}
