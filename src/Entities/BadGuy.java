import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class BadGuy extends ActimatedEntity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private PathingStrategy pathStrat;
    private static final String QUAKE_KEY = "quake";

    public BadGuy(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, actionPeriod, p);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {


        Optional<Entity> rick = super.getPosition().findNearest(world, Rick.class);
        if (rick.isPresent())
            super.moveNextPos(world, rick.get(), scheduler);
        /*if (rick.isPresent()) {
            if(moveToBadGuy(world,rick.get(),scheduler)){
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                ActimatedEntity quake = new Quake(getPosition(),imageStore.getImageList("quake"));
                world.addEntity(quake);
                quake.scheduleAction(scheduler,world,imageStore);
                return;
            }
        }*/

        long nextPeriod = super.getActionPeriod();
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }
}
