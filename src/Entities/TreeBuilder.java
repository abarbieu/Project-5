import processing.core.PImage;

import java.util.List;

public class TreeBuilder extends Entity implements Active {
    private int actionPeriod = 10000;

    public TreeBuilder(String id, int actionPeriod) {
        super(id, new Point(-1, -1), null);
        this.actionPeriod = actionPeriod;
    }

    @Override
    public int getActionPeriod() {
        return actionPeriod;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point newPoint = new Point((int)( Math.random() * world.getNumRows()), (int)( Math.random() * world.getNumCols()));
        while (!checkIfValid(newPoint, world)) {
            newPoint = new Point((int)( Math.random() * world.getNumRows()), (int)( Math.random() * world.getNumCols()));
        }
        Tree tree = new Tree("newTree", imageStore.getImageList("tree"),
                newPoint);
        world.addEntity(tree);
        this.scheduleAction(scheduler, world, imageStore);
    }

    private boolean checkIfValid(Point p, WorldModel world) {
        return p.withinBounds(world) && !p.isOccupied(world) && !(world.getOccupancyCell(p) instanceof Tree);
    }

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
    }


}