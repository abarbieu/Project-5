import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView {
    private PApplet screen;
    private WorldModel world;
    private float tileWidth;
    private float tileHeight;
    private float saveTH, saveTW;
    private Viewport viewport;
    private float scale = 1;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
                     int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.saveTH = tileHeight;
        this.saveTW = tileWidth;
        this.tileWidth = tileWidth * scale;
        this.tileHeight = tileHeight * scale;
        this.viewport = new Viewport(numRows, numCols);
    }

    public void drawViewport() {
        drawBackground();
        drawEntities();
    }

    public void drawEntities() {
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if (this.viewport.contains(pos)) {
                Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
                this.screen.image(entity.getCurrentImage(),
                        viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight, tileWidth, tileHeight);
            }
        }
    }

    public void drawBackground() {
        for (int row = 0; row < this.viewport.getNumRows(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = this.viewport.viewportToWorld(col, row);
                Optional<PImage> image = this.world.getBackgroundImage(
                        worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight, this.tileWidth, this.tileHeight);
                }
            }
        }
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = /*clamp(*/this.viewport.getCol() + colDelta/*, 0,
                this.world.getNumCols() - this.viewport.getNumCols())*/;
        int newRow = /*clamp(*/this.viewport.getRow() + rowDelta/*, 0,
                this.world.getNumRows() - this.viewport.getNumRows())*/;
        this.viewport.shift(newCol, newRow);
    }

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void zoom(float percent, Point p) {


        if (percent > 1 || tileWidth * percent >= saveTW) {
            this.tileHeight *= percent;
            this.tileWidth *= percent;
            //this.viewport.shift(p.x, p.y);
        }
        if (Math.abs(tileWidth - saveTW) < 3) {
            this.tileWidth = saveTW;
            this.tileHeight = saveTH;
        }
    }

    public void reset() {
        this.tileHeight = saveTH;
        this.tileWidth = saveTW;
        this.viewport.reset();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public float getTileHeight() {
        return tileHeight;
    }
}
