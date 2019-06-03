

interface Action
{
   /*public Action.Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public void executeAnimationAction(EventScheduler scheduler)
    {
       this.entity.nextImage();

       if (this.repeatCount != 1)
       {
          scheduler.scheduleEvent(this.entity,
             this.entity.createAnimationAction(
                     Math.max(this.repeatCount - 1, 0)),
             this.entity.getAnimationPeriod());
       }
    }
*/
    public void executeAction(EventScheduler scheduler);
   /* {
       switch (this.kind)
       {
       case ACTIVITY:
          executeActivityAction(scheduler, this.entity.getKind(), this.entity);
          break;

       case ANIMATION:
          this.executeAnimationAction(scheduler);
          break;
       }*/


   /*public void executeActivityAction(EventScheduler scheduler, EntityKind entityKind, Entity entity)
   {
      switch (entityKind)
      {
      case MINER_FULL:
         entity.executeMinerFullActivity(world,
            imageStore, scheduler);
         break;

      case MINER_NOT_FULL:
         entity.executeMinerNotFullActivity(world,
            imageStore, scheduler);
         break;

      case ORE:
         entity.executeOreActivity(world, imageStore,
            scheduler);
         break;

      case ORE_BLOB:
         entity.executeOreBlobActivity(world,
            imageStore, scheduler);
         break;

      case QUAKE:
         entity.executeQuakeActivity(world, imageStore,
            scheduler);
         break;

      case VEIN:
         entity.executeVeinActivity(world, imageStore,
            scheduler);
         break;

      default:
         throw new UnsupportedOperationException(
            String.format("executeActivityAction not supported for %s",
                    entityKind));
      }
   }*/
}
