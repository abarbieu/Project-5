import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class PickleRick extends ActimatedEntity {
    private boolean activated = false;
    private int moveCnt = 0, maxMoves = 10;

    public PickleRick(String id, Point position, List<PImage> images) {
        super(id, position, 0, images, 0, (int) (1000 + Math.random() * 1000), new SingleStepPath());
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (activated) {
            Optional<Entity> target = getPosition().findNearest(world, Morty.class);
            if (target.isPresent()) {
                moveNextPos(world, target.get(), scheduler);
            }
            moveCnt++;
            if (moveCnt >= maxMoves) {
                activated=false;
                moveCnt = 0;
            }
            long nextPeriod = super.getActionPeriod();
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    nextPeriod);
        }
    }

    public void activate() {
        activated = true;
    }
}
