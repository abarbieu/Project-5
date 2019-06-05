import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;

import processing.core.*;
import processing.event.MouseEvent;

public final class VirtualWorld
        extends PApplet {
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_WIDTH = 640 * WORLD_WIDTH_SCALE;
    private static final int VIEW_HEIGHT = 480 * WORLD_HEIGHT_SCALE;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;


    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "gaia.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;


    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long next_time;

    private static final int MINER_ANIMATION_PERIOD = 6;
    private static final int MINER_ACTION_PERIOD = 5;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_ROW = 3;
    private static final int MINER_COL = 2;
    private static final int MINER_ID = 1;
    private static final String MINER_KEY = "miner";

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
                TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= next_time) {
            this.scheduler.updateOnTime(time);
            next_time = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void mousePressed() {
        worldEvent(mouseToWorld());
    }

    public Point mouseToWorld() {
        return new Point(this.view.getViewport().getCol() + mouseX / (int) view.getTileWidth(),
                this.view.getViewport().getRow() + mouseY / (int) view.getTileHeight());
    }

    public void worldEvent(Point pos) {
        println(pos);
        if (pos.withinBounds(this.world)) {

            Predicate<Point> canPassThrough = (pt) -> !pos.isOccupied(world);
            List<Point> neighbors = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS.apply(pos)
                    .filter(canPassThrough).collect(Collectors.toList());


            if (neighbors.size() < 8)
                return;

            for (Point neighbor : neighbors) {
                Background tree = new Background("tree", imageStore.getImageList("rocks"));
                if (neighbor.withinBounds(this.world)) {
                    world.setBackgroundCell(neighbor, tree);
                }

            }
            Background tree = new Background("tree", imageStore.getImageList("rocks"));
            world.setBackgroundCell(pos, tree);

//        Entity entity = new BadGuy("badGuy", pos, imageStore.getImageList("badGuy"), 1, 0, new AStarPathingStrategy());
//        world.addEntity(entity);

            Entity morty = new Morty("morty", pos, imageStore.getImageList("morty"), 1, 0, new AStarPathingStrategy());
            world.addEntity(morty);


            scheduleActions(world, scheduler, imageStore);
        }
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        } else {
            switch (key) {
                case ('r'):
                    view.reset();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void mouseWheel(MouseEvent event) {
        if (event.getCount() != 0) {
            view.zoom((float) (1.0 + (event.getCount() * 0.1)),
                    this.view.getViewport().worldToViewport(mouseX / (int) view.getTileWidth(),
                            mouseY / (int) view.getTileHeight()));
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(String filename, ImageStore imageStore,
                                   PApplet screen) {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(WorldModel world, String filename,
                                 ImageStore imageStore) {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(WorldModel world,
                                       EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Active)
                ((Active) entity).scheduleAction(scheduler, world, imageStore);
        }
    }

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
