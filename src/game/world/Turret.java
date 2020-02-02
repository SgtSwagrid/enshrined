package game.world;

import game.entity.Entity;
import game.entity.Model;
import game.math.Matrix4;
import game.physics.Collider;
import game.physics.PhysicsWorld;
import game.player.Bullet;
import game.player.Damageable;
import game.player.Player;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Scene3D;

public class Turret extends Entity implements Damageable {
    
    private Colour colour = Colour.ELECTROMAGNETIC;
    
    private static final int MAX_HEALTH = 10000;
    private int health = MAX_HEALTH;
    
    private long lastShot;
    private final int SHOOT_SPEED = 600;
    private final int RANGE = 15;
    
    private float x, y;
    private float angle;
    
    private Player p1, p2;
    
    private Model model;

    public Turret(Scene3D scene, float x, float y, Player p1, Player p2) {
        super(scene);
        this.x = x; this.y = y;
        this.p1 = p1; this.p2 = p2;
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        getTransform().transfRel(Matrix4.getTranslationMatrix(x, y, -0.2F));
        //getTransform().transfRel(Matrix4.getScaleMatrix(1.25F, 1.25F, 0.1F));
        //getTransform().transfRel(Matrix4.getRotationMatrix(45));
        
        model = new Model(this, g, Shapes.PLAYER, colour, null);
        addComponent(model);
        addComponent(new Collider(this, p, 1.0F, 1.0F));
        
    }
    
    @Override
    public void update(long dt) {
        
        if(health <= 0) getScene().removeEntity(this);
        health = (int)Math.min(health+dt, MAX_HEALTH);
        model.setColour(colour.mix(Colour.RISE_N_SHINE,
                100-100*health/MAX_HEALTH));
        
        float xd1 = x-p1.getTransform().getX();
        float yd1 = y-p1.getTransform().getY();
        float p1d = (float)Math.sqrt(xd1*xd1 + yd1*yd1);
        
        float xd2 = x-p2.getTransform().getX();
        float yd2 = y-p2.getTransform().getY();
        float p2d = (float)Math.sqrt(xd2*xd2 + yd2*yd2);
        
        if(!p1.isAlive()) p1d = 10000;
        if(!p2.isAlive()) p2d = 10000;
        
        Player nearest = p1d<p2d ? p1:p2;
        float dist = p1d<p2d ? p1d:p2d;
        
        float newAngle = (float)Math.toDegrees(Math.atan2(
                nearest.getTransform().getY()-y,
                nearest.getTransform().getX()-x));
        
        float angleDiff = newAngle-angle;
        getTransform().transfRel(Matrix4.getRotationMatrix(angleDiff));
        angle = newAngle;
        
        if(System.currentTimeMillis() - lastShot > SHOOT_SPEED && dist < RANGE) {
            getScene().addEntity(new Bullet(getScene(), this, x, y, angle));
            lastShot = System.currentTimeMillis();
        }
    }

    @Override
    public void damage(int amount) {
        health -= amount;
    }
}