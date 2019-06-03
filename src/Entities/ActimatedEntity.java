import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class ActimatedEntity extends AnimatedEntity implements Active {
    private int actionPeriod;
    private PathingStrategy strategy;

    public ActimatedEntity(String id, Point position, int animationPeriod, List<PImage> images, int imageIndex, int actionPeriod,PathingStrategy p) {
        super(id, position, animationPeriod, images, imageIndex);
        this.actionPeriod = actionPeriod;
        this.strategy=p;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    protected boolean moveNextPos(WorldModel world, Entity target, EventScheduler scheduler) {
        Point nextPos;
        List<Point> tst = strategy.computePath(this.getPosition(),target.getPosition(),
                p -> p.withinBounds(world) && !p.isOccupied(world),
                this::neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if(tst.size()>=1){
            nextPos= tst.get(0);
        }else {
            nextPos = this.getPosition();
        }
        if (!super.getPosition().equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
        }
        return false;
    }

    public void scheduleAction(EventScheduler scheduler,
                               WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this,
                new Animation(this, world, imageStore, 0), getAnimationPeriod());
    }
    public PathingStrategy getStrat(){
        return strategy;
    }
    private boolean neighbors(Point p1, Point p2) {
        return p1.x + 1 == p2.x && p1.y == p2.y ||
                p1.x - 1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y + 1 == p2.y ||
                p1.x == p2.x && p1.y - 1 == p2.y;
    }
}
