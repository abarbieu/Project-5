import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private float tileWidth;
   private float tileHeight;
   private Viewport viewport;
   private float scale=1;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth*scale;
      this.tileHeight = tileHeight*scale;
      this.viewport = new Viewport(numRows, numCols);
   }

   public void drawViewport()
   {
      drawBackground();
      drawEntities();
   }

   public void drawEntities()
   {
      for (Entity entity : this.world.getEntities())
      {
         Point pos = entity.getPosition();

         if (this.viewport.contains(pos))
         {
            Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y );
            this.screen.image(entity.getCurrentImage(),
               viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight,tileWidth,tileHeight);
         }
      }
   }

   public void drawBackground()
   {
      for (int row = 0; row < this.viewport.getNumRows(); row++)
      {
         for (int col = 0; col < this.viewport.getNumCols(); col++)
         {
            Point worldPoint = this.viewport.viewportToWorld(col, row);
            Optional<PImage> image = this.world.getBackgroundImage(
                    worldPoint);
            if (image.isPresent())
            {
               this.screen.image(image.get(), col * this.tileWidth,
                  row * this.tileHeight,this.tileWidth,this.tileHeight);
            }
         }
      }
   }

   public void shiftView(int colDelta, int rowDelta)
   {
      int newCol = Functions.clamp(this.viewport.getCol() + colDelta, 0,
              this.world.getNumCols() - this.viewport.getNumCols());
      int newRow = Functions.clamp(this.viewport.getRow() + rowDelta, 0,
              this.world.getNumRows() - this.viewport.getNumRows());

      this.viewport.shift(newCol, newRow);
   }
//    public void zoom(float percent){
//      this.tileHeight*=percent;
//      this.tileWidth*=percent;
//    }
}
