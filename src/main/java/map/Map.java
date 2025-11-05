package map;

public class Map {
  private int[][] map;
  private boolean mapBeaten;

  public Map(){
    map = new int[8][8];
    mapBeaten = false;
  }

  public Map(int[][] map) {
    this();
    this.map = map;
  }

  public int[][] getMap() {
    return this.map;
  }

  public boolean isMapBeaten() {
    return mapBeaten;
  }

  public void setMapBeaten(boolean mapBeaten) {
    this.mapBeaten = mapBeaten;
  }
}