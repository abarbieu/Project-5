import processing.core.PImage;

import java.util.List;

public class Quake extends ActimatedEntity {
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position, List<PImage> images) {
        super("quake", position, 100, images, 0, 500,null);
    }

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
