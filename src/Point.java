import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

final class Point
{
   public final int x;
   public final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

    public Optional<Entity> findNearest(WorldModel world,
                                        Class<?> type) {
       List<Entity> ofType = new LinkedList<>();
       for (Entity entity : world.getEntities())
       {
          if (entity.getClass() == type)
              ofType.add(entity);
       }

       return nearestEntity(ofType);
    }

    public int distanceSquared(Point p2)
    {
       int deltaX = this.x - p2.x;
       int deltaY = this.y - p2.y;

       return deltaX * deltaX + deltaY * deltaY;
    }

    public Optional<Entity> nearestEntity(List<Entity> entities)
    {
       if (entities.isEmpty())
       {
          return Optional.empty();
       }
       else
       {
          Entity nearest = entities.get(0);
          int nearestDistance = nearest.getPosition().distanceSquared(this);

          for (Entity other : entities)
          {
             int otherDistance = other.getPosition().distanceSquared(this);

             if (otherDistance < nearestDistance)
             {
                nearest = other;
                nearestDistance = otherDistance;
             }
          }

          return Optional.of(nearest);
       }
    }

    public boolean isOccupied(WorldModel world)
    {
       return withinBounds(world) &&
          world.getOccupancyCell(this) != null;
    }

    public boolean withinBounds(WorldModel world)
    {
       return this.y >= 0 && this.y < world.getNumRows() &&
          this.x >= 0 && this.x < world.getNumCols();
    }


    public boolean adjacent(Point p2)

    {
       return (this.x == p2.x && Math.abs(this.y - p2.y) == 1) ||
          (this.y == p2.y && Math.abs(this.x - p2.x) == 1);
    }

    public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

    public Optional<Point> findOpenAround(WorldModel worldModel)
    {
       for (int dy = -WorldModel.getOreReach(); dy <= WorldModel.getOreReach(); dy++)
       {
          for (int dx = -WorldModel.getOreReach(); dx <= WorldModel.getOreReach(); dx++)
          {
             Point newPt = new Point(x + dx, y + dy);
             if (newPt.withinBounds(worldModel) &&
                     (!newPt.isOccupied(worldModel) && !(worldModel.getOccupancyCell(newPt) instanceof Tree)))
             {
                return Optional.of(newPt);
             }
          }
       }

       return Optional.empty();
    }

}
