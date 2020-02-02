package game.world;

import game.entity.Entity;
import game.entity.Model;
import game.math.Matrix4;
import game.physics.PhysicsWorld;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Scene3D;

public class WallPost extends Entity {
    
    private Colour colour;
    private float width = 0.5F;
    
    public WallPost(Scene3D scene, Colour colour) {
        super(scene);
        this.colour = colour;
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        addComponent(new Model(this, g, Shapes.CUBE, colour, null));
        
        getTransform().transfRel(Matrix4.getScaleMatrix(width, width, 1.5F));
    }
}
