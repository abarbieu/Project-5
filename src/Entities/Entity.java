import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    private String id;
    private Point position;
    private List<PImage> images;

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public List<PImage> getImages() {
        return images;
    }
    public PImage getCurrentImage(){
        return images.get(0);
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}