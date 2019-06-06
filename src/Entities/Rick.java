import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class Rick extends ActimatedEntity {
    private int resourceLimit;
    private int resourceCount;
    private int dx,dy;
    private WorldView view;
    private boolean moving = false;


    public Rick(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, actionPeriod, p);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public boolean moveDir(int dx, int dy, WorldModel world,WorldView view) {
        Point newPoint = new Point(getPosition().x + dx, getPosition().y + dy);
        if (checkValid(newPoint, world)) {
            setPosition(newPoint);
            view.shiftView(dx, dy);
            return true;
        }
        return false;
    }
    public void setMove(int dx, int dy,WorldView view){
        this.moving = true;
        this.view=view;
        this.dx=dx;
        this.dy=dy;
    }

    public boolean checkValid(Point pos, WorldModel world) {
        return pos.withinBounds(world) && !pos.isOccupied(world) && !(world.getOccupancyCell(pos) instanceof Tree);
    }
    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {


        Optional<Entity> blobTarget = super.getPosition().findNearest(world,
                BadGuy.class);

        long nextPeriod = super.getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveToOreBlob(world, blobTarget.get(), scheduler)) {
                ActimatedEntity quake = new Quake(tgtPos,
                        imageStore.getImageList("quake"));

                world.addEntity(quake);
                nextPeriod += super.getActionPeriod();
                quake.scheduleAction(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }

    private boolean moveToOreBlob(WorldModel world,
                                  Entity target, EventScheduler scheduler) {
        if (super.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            if (moving) {
                moveDir(dx, dy, world, this.view);
                moving = false;
                return false;
            }
            return false;
        }
    }
    /*public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (moving) {
            moveDir(dx, dy, world, this.view);
            moving = false;

            Optional<Entity> baddie = getPosition().findNearest(world, BadGuy.class);
            if (baddie.isPresent() && baddie.get().getPosition().adjacent(getPosition())) {
                world.removeEntity(baddie.get());
                scheduler.unscheduleAllEvents(baddie.get());
                ActimatedEntity quake = new Quake(baddie.get().getPosition(),
                        imageStore.getImageList("quake"));

                world.addEntity(quake);
                quake.scheduleAction(scheduler, world, imageStore);
            }
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    getActionPeriod());
        }
    }*/
        /*if (resourceCount >= resourceLimit) {
            Optional<Entity> fullTarget = super.getPosition().findNearest(world,
                    Tree.class);
            Point tmpPos = null;
            if(fullTarget.isPresent()){
                tmpPos = fullTarget.get().getPosition();
            }
            if (fullTarget.isPresent() &&
                    this.moveToFull(world, fullTarget.get(), scheduler, imageStore)) {


                ActiveEntity newVein = new Vein("newVein",
                        tmpPos,
                        10000,
                        imageStore.getImageList("vein"));
                world.addEntity(newVein);
                newVein.scheduleAction(scheduler, world, imageStore);
                this.transformFull(world, scheduler, imageStore);

            } else {
                scheduler.scheduleEvent(this,
                        new Activity(this, world, imageStore),
                        super.getActionPeriod());
            }
        } else {
            Optional<Entity> notFullTarget = super.getPosition().findNearest(world,
                    Ore.class);

            if (!notFullTarget.isPresent() ||
                    !this.moveToNotFull(world, notFullTarget.get(), scheduler) ||
                    !this.transformNotFull(world, scheduler, imageStore)) {
                scheduler.scheduleEvent(this,
                        new Activity(this, world, imageStore),
                        super.getActionPeriod());
            }
        }*/
    //}

    /*private boolean moveToNotFull(WorldModel world,
                                  Entity target, EventScheduler scheduler) {
        if (super.getPosition().adjacent(target.getPosition())) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            return moveNextPos(world, target, scheduler);
        }
    }

    private boolean moveToFull(WorldModel world,
                               Entity target, EventScheduler scheduler, ImageStore imageStore) {
        if (super.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);


            Point tmpPos = target.getPosition();
            ActimatedEntity quake = new Quake(tmpPos,
                    imageStore.getImageList("quake"));
            world.addEntity(quake);
            quake.scheduleAction(scheduler, world, imageStore);


            return true;
        } else {
            return moveNextPos(world, target, scheduler);
        }
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            ActimatedEntity miner = new Rick(super.getId(), super.getPosition(),
                    super.getImages(), this.resourceLimit, this.resourceLimit,
                    super.getActionPeriod(), super.getAnimationPeriod(), super.getStrat());

            replaceMe(world, scheduler, imageStore, miner);

            return true;
        }

        return false;
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore) {
        ActimatedEntity miner = new Rick(super.getId(), super.getPosition(), super.getImages(), this.resourceLimit, 0,
                super.getActionPeriod(), super.getAnimationPeriod(), super.getStrat());

        replaceMe(world, scheduler, imageStore, miner);
    }

    private void replaceMe(WorldModel world, EventScheduler scheduler, ImageStore imageStore, ActimatedEntity miner) {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleAction(scheduler, world, imageStore);
    }*/




}
