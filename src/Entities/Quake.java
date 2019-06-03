import processing.core.PImage;

import java.util.List;

public class Quake extends ActimatedEntity {
/*    private String id = "quake";
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod = 1100;
    private int animationPeriod = 100;*/

//    private static final String QUAKE_KEY = "quake";
//    private static final String QUAKE_ID = "quake";
//    private static final int QUAKE_ACTION_PERIOD = 1100;
//    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position, List<PImage> images) {
        super("quake", position, 100, images, 0, 1100,null);
    }
    /*public Quake(Point position, List<PImage> images) {
        this.position = position;
        this.images = images;
    }*/

    public void executeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
    public void scheduleAction(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this,world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent(this,
                new Animation(this,world,imageStore,QUAKE_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }


}
