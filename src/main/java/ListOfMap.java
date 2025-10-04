import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListOfMap {
  private class Map {
    private int[][] map = new int[8][8];
    private boolean MapBeated = false;

    public Map(){}

    public Map(int[][] map) {
      this.map = map;
    }

    public boolean getMapBeated() {
      return MapBeated;
    }

    public void setMapBeated(boolean MapBeated) {
      this.MapBeated = MapBeated;
    }

    public int[][] getMap() {
      return this.map;
    }
  }

  private List<Map> listOfMaps = new ArrayList<>();

  private int Size = 0;

  public ListOfMap() {}
  public ListOfMap(String filePath) {
    try (BufferedReader br  = new BufferedReader(new FileReader(filePath))) {
      String line = null;
      int[][] map = new int[8][8];
      int row = 0;
      while ((line = br.readLine()) != null) {
        String[] path = line.split(" ");
        if (line.charAt(0) == '#') {
          if (row > 0) {
            Size++;
            Map e = new Map(map);
            listOfMaps.add(e);
            row = 0;
            map = new int[8][8];
          }
        } else {
          for (int i = 0; i < path.length; i++) {
            map[row][i] = Integer.parseInt(path[i]);
          }
          row++;
        }
      }
    } catch ( IOException e){
      e.printStackTrace();
    }
  }

  public int[][] getMapByCode(int Code) {
    if(Code > Size) {
      return null;
    }
    Code = Math.max(Code - 1, 0);
    return listOfMaps.get(Code).getMap();
  }

}
