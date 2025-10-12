import java.util.Random;

public class CreateMap {
    public static void main(String[] args) {
        int rows = 8;
        int cols = 8;
        int[][] map = new int[rows][cols];

        // Khởi tạo bản đồ với giá trị 0 (chưa được thăm)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = 0;
            }
        }

        DFS dfs = new DFS(rows, cols);

        for (int i = 1; i < 20; i++) {
            Random rand  = new Random();
            int x = rand.nextInt(rows) ;
            int y = rand.nextInt(cols) ;
            dfs.createMap(map, x, y, i);
        }

        // Duyệt qua tất cả các ô trong bản đồ
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
}
