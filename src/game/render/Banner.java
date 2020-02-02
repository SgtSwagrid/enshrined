package game.render;

import game.entity.Entity;
import game.entity.Model;
import game.math.Matrix4;
import game.physics.PhysicsWorld;
import game.properties.Colour;
import game.properties.Shapes;
import game.properties.Texture;

public class Banner extends Entity {

    private float width, height;
    private Texture texture;
    public Banner(Scene3D scene, float width, float height, Texture texture) {
        super(scene);
        // TODO Auto-generated constructor stub
        this.width = width; this.height = height;
        this.texture = texture;
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        Model m = new Model(this, g, Shapes.SQUARE, Colour.WHITE, texture);
        m.setLighting(false);
        addComponent(m);
        getTransform().transfRel(Matrix4.getScaleMatrix(width, height, 1));
        getTransform().transfAbs(Matrix4.getTranslationMatrix(0, 0, 3));
    }
}
