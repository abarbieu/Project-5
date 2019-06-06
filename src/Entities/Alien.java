import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Alien extends ActimatedEntity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private PathingStrategy pathStrat;
    private static final String QUAKE_KEY = "quake";

    public Alien(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, actionPeriod ,p);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {


        Optional<Entity> blobTarget = super.getPosition().findNearest(world,
                Morty.class);

        long nextPeriod = super.getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveToMorty(world, blobTarget.get(), scheduler,imageStore)) {
                ActimatedEntity quake = new Quake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += super.getActionPeriod();
                quake.scheduleAction(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }

    private boolean moveToMorty(WorldModel world,
                                  Entity target, EventScheduler scheduler,ImageStore imageStore) {
        if (super.getPosition().adjacent(target.getPosition())) {
            Point tmp = target.getPosition();
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            world.loose=true;
            Vein newV = new Vein("end",tmp,1,imageStore.getImageList("vein"));
            world.addEntity(newV);
            newV.scheduleAction(scheduler,world,imageStore);
            return true;
        } else {

            return super.moveNextPos(world, target, scheduler);
        }
    }
}
