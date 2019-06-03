

public class Activity implements Action {

    private Active entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Active entity, WorldModel world,
                    ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        entity.executeActivity(world,imageStore,scheduler);
    }
}
