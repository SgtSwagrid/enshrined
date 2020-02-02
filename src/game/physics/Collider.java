package game.physics;

import game.entity.Component;
import game.entity.Entity;

public class Collider extends Component {
    
    PhysicsWorld world;
    
    private float width, height;

    public Collider(Entity entity, PhysicsWorld world, float width, float height) {
        super(entity);
        this.world = world;
        this.width = width; this.height = height;
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        world.addCollider(this);
    }

    @Override
    public void update(long deltaMs) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void destroy() {
        // TODO Auto-generated method stub
        world.removeCollider(this);
    }
    
    public float getX() { return getEntity().getTransform().getX(); }
    public float getY() { return getEntity().getTransform().getY(); }
    
    private float getWidth() { return width; }
    private float getHeight() { return height; }
    
    public float getMinX() { return getX() - getWidth()/2; }
    public float getMaxX() { return getX() + getWidth()/2; }
    public float getMinY() { return getY() - getHeight()/2; }
    public float getMaxY() { return getY() + getHeight()/2; }
}
