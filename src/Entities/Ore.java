import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore extends ActiveEntity {
/*    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit=0;
    private int resourceCount=0;
    private int actionPeriod;
    private int animationPeriod=0;*/

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    private static final Random rand = new Random();

    public Ore(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }
    public void executeActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = super.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        ActimatedEntity blob = new Morty(super.getId() + BLOB_ID_SUFFIX,
                pos,imageStore.getImageList(BLOB_KEY), super.getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN)
        ,new AStarPathingStrategy()
        );

        world.addEntity(blob);
        blob.scheduleAction(scheduler, world, imageStore);
    }
}
