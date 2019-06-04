import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class BadGuy extends ActimatedEntity{


    public BadGuy(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, actionPeriod, p);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {


        Optional<Entity> target = super.getPosition().findNearest(world,
                Rick.class);

        super.moveNextPos(world, target.get(), scheduler);

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                super.getActionPeriod());

    }
}

