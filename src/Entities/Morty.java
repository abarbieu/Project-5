import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Morty extends ActimatedEntity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private PathingStrategy pathStrat;
    private static final String QUAKE_KEY = "quake";

    public Morty(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, actionPeriod, p);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {


        Optional<Entity> target = super.getPosition().findNearest(world, Rick.class);

        long nextPeriod = super.getActionPeriod();

        if (target.isPresent()) {
            this.moveToRick(world, target.get(), scheduler);
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), nextPeriod);
    }

    private void moveToRick(WorldModel world, Entity target, EventScheduler scheduler)
    {
        super.moveNextPos(world, target, scheduler);
    }
}

