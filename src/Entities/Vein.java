import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActiveEntity {
/*    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int animationPeriod=0;*/

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final Random rand = new Random();
    private static final String ORE_KEY = "ore";

    public Vein(String id, Point position, int actionPeriod,List<PImage> images) {
        super(id,position,images, actionPeriod*4);
    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = super.getPosition().findOpenAround(world);

        if (openPt.isPresent())
        {
            ActiveEntity ore = new Ore(ORE_ID_PREFIX + super.getId(),
                    openPt.get(),
                    imageStore.getImageList(ORE_KEY),ORE_CORRUPT_MIN +
                    rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN));
            world.addEntity(ore);
            ore.scheduleAction(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new Activity(this,world, imageStore),
                super.getActionPeriod());
    }

}
