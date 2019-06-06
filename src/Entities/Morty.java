import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Morty extends ActimatedEntity {

    public Morty(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, actionPeriod, p);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {


        Optional<Entity> target = super.getPosition().findNearest(world,
                Rick.class);

        long nextPeriod = super.getActionPeriod();

        if (target.isPresent()) {
            this.moveToOreBlob(world, target.get(), scheduler);
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }

    private void moveToOreBlob(WorldModel world,
                                  Entity target, EventScheduler scheduler) {
        super.moveNextPos(world, target, scheduler);
    }
}
