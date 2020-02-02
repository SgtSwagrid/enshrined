package game.entity;

import game.math.Matrix4;
import game.physics.PhysicsWorld;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Scene3D;

public class Floor extends Entity {
    
    public Floor(Scene3D scene) {
        super(scene);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        addComponent(new Model(this, g, Shapes.SQUARE, Colour.LYNX_WHITE, null));
        getTransform().transfRel(Matrix4.getTranslationMatrix(0.0F, 0.0F, -0.5F));
        getTransform().transfRel(Matrix4.getScaleMatrix(1000.0F, 1000.0F, 1.0F));
    }
}