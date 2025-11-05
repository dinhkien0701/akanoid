package map;

public class Pair<U, V> {
    // Cặp giá trị bất biến (first, second)
    public final U first;
    public final V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }
}
