import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity implements Active{

    private int actionPeriod;

    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }
    public void scheduleAction(EventScheduler scheduler,
                               WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
    }
}
