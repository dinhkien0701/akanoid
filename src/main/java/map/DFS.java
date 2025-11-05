package map;

import java.util.*;

/**
 * DFS (Depth-First Search) để mở rộng một cụm gạch bắt đầu từ một ô (x, y).
 *
 * Ý tưởng: chọn ngẫu nhiên kích thước cụm (n < 5 với xác suất cao), sau đó duyệt các láng giềng 4
 * hướng và tô id cho đến khi đủ n ô hoặc hết quota (n_max) cho phép. Cách này tạo ra các "đám" gạch
 * tự nhiên.
 */

class Pair<U, V> {
  // Cặp giá trị bất biến (first, second)
  public final U first;
  public final V second;

  public Pair(U first, V second) {
    this.first = first;
    this.second = second;
  }
}


public class DFS {
  // Kích thước lưới bản đồ
  protected int rows, cols;

  public DFS(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
  }

  private boolean isValid(int x, int y) {
    // Vị trí (x, y) nằm trong biên lưới?
    return x >= 0 && x < rows && y >= 0 && y < cols;
  }

  /**
   * Mở rộng cụm từ (x, y) bằng id cho đến khi đủ n ô hoặc hết n_max.
   * 
   * @param map lưới bản đồ (0 là trống, >0 là đã có gạch)
   * @param x hàng bắt đầu
   * @param y cột bắt đầu
   * @param id loại gạch sẽ gán (ví dụ 1 = normal)
   * @param n_max quota còn lại có thể tô
   * @return quota còn lại sau khi tô (n_max mới)
   */
  public int createMap(int map[][], int x, int y, int id, int n_max) {
    // 4 hướng cơ bản: phải, trái, xuống, lên
    int[] dx = {0, 0, 1, -1};
    int[] dy = {1, -1, 0, 0};

    // Tô ô đầu tiên
    map[x][y] = id;

    Random rand = new Random();

    // Danh sách thứ tự hướng sẽ được xáo trộn mỗi bước để tạo ngẫu nhiên
    List<Integer> directions = new ArrayList<>();
    for (int i = 0; i < 4; i++)
      directions.add(i);

    // Chọn kích thước cụm n (thiên về nhỏ < 5)
    int n = rand.nextInt(8) + 1; // 1..8
    if (n >= 5)
      n = rand.nextInt(8) + 1;
    if (n >= 5)
      n = rand.nextInt(8) + 1;
    // -> Xác suất ~87.5% để n < 5 (cụm nhỏ, dàn đều hơn)

    int count = 1; // đã tô 1 ô (ô gốc)
    n_max--; // trừ quota

    // Ngăn xếp (deque) lưu các ô biên để tiếp tục mở rộng
    Deque<Pair<Integer, Integer>> stack = new ArrayDeque<>();
    stack.push(new Pair<>(x, y)); // addFirst

    while (count < n && n_max > 0) {
      if (stack.isEmpty())
        break; // không còn biên để mở rộng

      // Lấy 1 ô ở cuối ra để trải (tạo cảm giác DFS nhưng có trộn)
      Pair<Integer, Integer> current = stack.removeLast(); // removeLast
      x = current.first;
      y = current.second;

      // Xáo hướng mỗi vòng để pattern đa dạng
      java.util.Collections.shuffle(directions);

      int k = rand.nextInt(2) + 1; // số bước mở rộng tối đa từ ô này (1..2)
      int madeMove = 0;
      for (int dir : directions) {
        if (madeMove >= k || count >= n)
          break; // đủ bước hoặc đủ cụm thì dừng

        int nx = x + dx[dir];
        int ny = y + dy[dir];
        if (isValid(nx, ny) && map[nx][ny] == 0) {
          // Mở rộng sang ô mới và đưa ô đó vào đầu deque
          stack.addFirst(new Pair<>(nx, ny));
          map[nx][ny] = id;
          count++;
          madeMove++;
          n_max--;
        }
      }
    }

    // Trả lại quota còn lại để caller tiếp tục tạo cụm khác
    return n_max; // (truyền tham trị nên phải trả về)
  }

}
