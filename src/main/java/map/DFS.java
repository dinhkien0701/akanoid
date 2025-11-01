package map;
import java.util.*;


class Pair<U, V> {
  // Tạo pair java
  public final U first;
  public final V second;

  public Pair(U first, V second) {
    this.first = first;
    this.second = second;
  }
}

public class DFS {
  // Sử dung thuật toán DFS để tạo bản đồ
  protected int rows, cols;

  public DFS(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
  }

  private boolean isValid(int x, int y) {
    // Kiểm tra vị trí (x, y) có hợp lệ không
    return x >= 0 && x < rows && y >= 0 && y < cols;
  }

  public int createMap(int[][] map, int x, int y, int id , int n_max) {
    // Bắt đầu tạo bản đồ từ (x, y)
    int[] dx = {0, 0, 1, -1};
    int[] dy = {1, -1, 0, 0};

    //Nhập id cho ô ban đầu
    map[x][y] = id;

    // Tạo đối tượng để random
    Random rand = new Random();

    // Lưu các hướng di chuyển
    List<Integer> directions = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      directions.add(i);
    }
    int n = rand.nextInt(8) + 1; // Lấy số ngẫu nhiên từ 1 đến 8
    if (n >= 5) {
      // Nếu số lớn hơn hoặc bằng 5
      n = rand.nextInt(8) + 1; // Lấy lại ngẫu nhiên lần nữa
    }

    if (n >= 5) {
      // Nếu vẫn lớn hơn hoặc bằng 5 lấy lại thêm lần cuối
      n = rand.nextInt(8) + 1; // Lấy lại ngẫu nhiên lần nữa
    }

    // -> Tất tuất là có 87.5 % ra kết quả n <5

    int count = 1; // Đếm số ô đã nhận
      n_max --;

    // Tạo một khay để lưu trữ các vị trí đang duyệt
    Deque<Pair<Integer, Integer>> stack = new ArrayDeque<>();
    stack.push(new Pair<>(x, y)); // Đẩy vị trí ban đầu vào khay = addFirst
    while (count < n && n_max > 0) {
      if (stack.isEmpty()) break; // Nếu không còn vị trí nào để duyệt
      Pair<Integer, Integer> current = stack.removeLast(); // Lấy vị trí cuối cùng ra = removeLast
      x = current.first;
      y = current.second;
      java.util.Collections.shuffle(directions); // Xáo trộn các hướng di chuyển

      int k = rand.nextInt(2) + 1; // Lấy số ngẫu nhiên từ 1 đến 2
      int madeMove = 0; // Đếm số lần di chuyển thành công
      for (int dir : directions) {
        if (madeMove >= k || count >= n) break; // Nếu đã di chuyển đủ k lần thì dừng
        // Nhận hướng di chuyển mới
        int nx = x + dx[dir];
        int ny = y + dy[dir];
        if (isValid(nx, ny) && map[nx][ny] == 0) {
          stack.addFirst(new Pair<>(nx, ny)); // Đẩy vị trí mới vào khay
          map[nx][ny] = id;
          count ++;
          madeMove++;
          n_max -- ;
        }
      }
    }

    return n_max ; // trả lại giá trị , do biến nguyên thủy chỉ truyền tham trị ( bản sao ) thôi
  }

}