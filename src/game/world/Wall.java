package game.world;

import game.entity.Entity;
import game.entity.Model;
import game.math.Matrix4;
import game.physics.Collider;
import game.physics.PhysicsWorld;
import game.player.Damageable;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Scene3D;

public class Wall extends Entity implements Damageable {
    
    private boolean horz, invin;
    
    private float length = 8.0F;
    private float width = 0.5F;
    
    private final int MAX_HEALTH = 20000;
    private int health = MAX_HEALTH;
    
    private Scene3D scene;
    private Model model;
    
   private Colour colour;
    
    public Wall(boolean horz, boolean invin, Scene3D scene) {
        super(scene);
        colour = !invin ?
                Colour.SPICED_BUTTERNUT.darken(100) :
                    Colour.ELECTROMAGNETIC;
        this.invin = invin;
        this.horz = horz;
        this.scene = scene;
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        model = new Model(this, g, Shapes.CUBE, colour, null);
        addComponent(model);
        addComponent(new Collider(this, p, horz?length:width, horz?width:length));
        
        getTransform().transfRel(Matrix4.getScaleMatrix(
                horz?length:width, horz?width:length, 1.5F));
    }
    
    @Override
    public void update(long dt) {
        
        if(health <= 0) scene.removeEntity(this);
        
        model.setColour(colour.mix(Colour.PERIWINKLE,
                100-100*health/MAX_HEALTH));
        health = (int)Math.min(health + dt, MAX_HEALTH);
    }
    
    @Override
    public void damage(int amount) {
        if(!invin) health -= amount;
    }
}