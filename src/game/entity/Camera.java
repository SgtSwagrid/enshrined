package game.entity;

import game.math.Matrix4;
import game.render.GraphicsWorld;

public class Camera extends Component {
    
    private GraphicsWorld world;

    protected Camera(Entity entity, GraphicsWorld world) {
        super(entity);
        this.world = world;
        world.setCamera(this);
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(long deltaMs) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void destroy() {
        // TODO Auto-generated method stub
        
    }
    
    public Matrix4 getViewMatrix() {
        return getEntity().getTransform().getTransf().invert();
    }
}
