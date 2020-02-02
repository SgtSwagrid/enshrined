package game.world;

import game.entity.Entity;
import game.entity.Model;
import game.math.Matrix4;
import game.physics.Collider;
import game.physics.PhysicsWorld;
import game.player.Bullet;
import game.player.Damageable;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Scene3D;

public class Mine extends Entity implements Damageable {
    
    private Colour colour = Colour.RISE_N_SHINE.mix(Colour.CARMINE_PINK, 60).darken(50);
    
    private final int NUM_BULLETS = 30;
    
    private float x, y;

    public Mine(Scene3D scene, float x, float y) {
        super(scene);
        this.x = x; this.y = y;
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        getTransform().transfRel(Matrix4.getTranslationMatrix(x, y, -0.3F));
        getTransform().transfRel(Matrix4.getScaleMatrix(0.75F, 0.75F, 0.75F));
        //getTransform().transfRel(Matrix4.getRotationMatrix(45));
        
        addComponent(new Model(this, g, Shapes.CUBE, colour, null));
        addComponent(new Collider(this, p, 1.0F, 1.0F));
        
    }

    @Override
    public void damage(int amount) {
        
        for(int i = 0; i < NUM_BULLETS; i++) {
            getScene().addEntity(new Bullet(getScene(), this, x, y, i*360/NUM_BULLETS));
        }
        
        getScene().removeEntity(this);
    }
}
