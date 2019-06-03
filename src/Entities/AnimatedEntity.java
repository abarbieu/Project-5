import processing.core.PImage;

import java.util.List;
public abstract class AnimatedEntity extends Entity{
    private int animationPeriod;
    private int imageIndex;

    public AnimatedEntity(String id, Point position, int animationPeriod, List<PImage> images, int imageIndex) {
        super(id, position,images);
        this.animationPeriod = animationPeriod;
        this.imageIndex = imageIndex;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }
    public PImage getCurrentImage()
    {
        return super.getImages().get(imageIndex);
    }
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % super.getImages().size();
    }
    public void scheduleAction(EventScheduler scheduler,
                               WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Animation(this,world,imageStore,0), getAnimationPeriod());
    }
}
