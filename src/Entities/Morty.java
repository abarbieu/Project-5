import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Morty extends ActimatedEntity {

    public Morty(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, 5, p);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> pickleDeath = getPosition().findNearest(world,PickleRick.class);
        if(pickleDeath.isPresent() && getPosition().adjacent(pickleDeath.get().getPosition())){
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            Point newPoint = new Point((int)( Math.random() * world.getNumCols()), (int)( Math.random() * world.getNumRows()));
            while (!checkIfValid(newPoint, world)) {
                newPoint = new Point((int)( Math.random() * world.getNumCols()), (int)( Math.random() * world.getNumRows()));
            }
            Morty teleMorty = new Morty("newMorty", newPoint,imageStore.getImageList("morty"),
                    1,0,new AStarPathingStrategy());
            world.addEntity(teleMorty);
            teleMorty.scheduleAction(scheduler,world,imageStore);


            return;
        }

        Optional<Entity> target = super.getPosition().findNearest(world, OreBlob.class);

        if (target.isPresent()) {
            this.moveToMorty(world, target.get(), scheduler);
        }

        long nextPeriod = super.getActionPeriod();
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }
    private boolean checkIfValid(Point p, WorldModel world) {
        return p.withinBounds(world) && !p.isOccupied(world) && !(world.getOccupancyCell(p) instanceof Tree);
    }

    private boolean moveToMorty(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            return super.moveNextPos(world, target, scheduler);
        }
    }
}
