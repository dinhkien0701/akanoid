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
    public int[][] creatMap() {
        int[][] map = new int[rows][cols];

        // Khởi tạo bản đồ với giá trị 0 (chưa được thăm)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = 0;
            }
        }

        DFS dfs = new DFS(rows, cols);
        int n = (int)(1.5 * level);
        if (level >= 3) n = n + level;
        if (level >= 5) n = n + level;
        if (level >= 7) n = n + (int)Math.pow(2,level - 6);
        for (int i = 1; i < 50; i++) {
            Random rand  = new Random();
            int x = rand.nextInt(rows) ;
            int y = rand.nextInt(cols) ;
            if(map[x][y] == 0 && n > 0) {
                dfs.createMap(map, x, y, i , n);
            }
        }

        // Duyệt qua tất cả các ô trong bản đồ
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        return map;
    }
}
