class GameObject {
  double x, y, width, height;

  protected GameObject (double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

   void update(GameManager gm){}
   void render(javafx.scene.canvas.GraphicsContext gc){}
}

