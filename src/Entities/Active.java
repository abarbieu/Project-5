import processing.core.PImage;

import java.util.List;

public interface Active {
/*
    public Active(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }
*/
    int getActionPeriod();
    void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}
