import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Rick extends ActimatedEntity {
    private int resourceLimit;
    private int resourceCount;


    public Rick(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, animationPeriod, images, 0, actionPeriod,p);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (resourceCount >= resourceLimit) {
            Optional<Entity> fullTarget = super.getPosition().findNearest(world,
                    PickleRick.class);

            if (fullTarget.isPresent() &&
                    this.moveToFull(world, fullTarget.get(), scheduler)) {
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
        }
    }

    private boolean moveToNotFull(WorldModel world,
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
                               Entity target, EventScheduler scheduler) {
        if (super.getPosition().adjacent(target.getPosition())) {
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
                    super.getActionPeriod(), super.getAnimationPeriod(),super.getStrat());

            replaceMe(world, scheduler, imageStore, miner);

            return true;
        }

        return false;
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore) {
        ActimatedEntity miner = new Rick(super.getId(), super.getPosition(), super.getImages(), this.resourceLimit, 0,
                super.getActionPeriod(), super.getAnimationPeriod(),super.getStrat());

        replaceMe(world, scheduler, imageStore, miner);
    }

    private void replaceMe(WorldModel world, EventScheduler scheduler, ImageStore imageStore, ActimatedEntity miner) {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleAction(scheduler, world, imageStore);
    }


}
