package game.player;

import java.util.List;

import game.entity.Entity;
import game.entity.Model;
import game.math.Matrix4;
import game.physics.Collider;
import game.physics.PhysicsWorld;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Light;
import game.render.Scene3D;
import game.world.Turret;

public class Bullet extends Entity {
    
    private final float VELOCITY = 75.0F;
    private final int DAMAGE = 2000;
    
    private Entity owner;
    private float x, y;
    private float angle;
    
    private PhysicsWorld p;
    private Collider collider;
    private Scene3D scene;
    
    private float vx, vy;
    
    private Light light;
    
    public Bullet(Scene3D scene, Entity owner, float x, float y, float angle) {
        super(scene);
        this.scene = scene;
        this.owner = owner;
        this.x = x; this.y = y;
        this.angle = angle;
        
        light = new Light();
        light.setColour(Colour.TURBO.darken(20));
        light.setPos(0.0F, 0.0F, 1.0F);
        light.setExpAtt(0.3F);
        light.setLinAtt(0.02F);
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        addComponent(new Model(this, g, Shapes.CUBE, Colour.TURBO, null));
        
        collider = new Collider(this, p, 0.5F, 0.5F);
        addComponent(collider);
        
        getTransform().transfRel(Matrix4.getTranslationMatrix(x, y, 0.1F));
        getTransform().transfRel(Matrix4.getScaleMatrix(0.25F, 0.25F, 0.25F));
        getTransform().transfRel(Matrix4.getRotationMatrix(angle));
        
        this.p = p;
        
        vx = VELOCITY * (float) Math.cos(Math.toRadians(angle));
        vy = VELOCITY * (float) Math.sin(Math.toRadians(angle));
    }
    
    @Override
    public void update(long dt) {
        
        if(Math.abs(getTransform().getX()) > 500 || Math.abs(getTransform().getY()) > 500) scene.removeEntity(this);
        
        light.setPos(getTransform().getX(), getTransform().getY(), 1);
        
        getTransform().transfRel(Matrix4.getTranslationMatrix(
                VELOCITY * dt / 1000.0F, 0.0F, 0.0F));
        
        List<Collider> collisions = p.checkCollision(collider);
        collisions.stream()
            .filter(c -> c.getEntity() instanceof Damageable)
            .map(c -> (Damageable) (c.getEntity()))
            .filter(e -> e != owner)
            .peek(e -> e.damage(DAMAGE))
            .filter(e -> e instanceof Player)
            .map(e -> (Player) e)
            .forEach(p -> p.addVelocity(vx/5.0F, vy/5.0F));
        
        collisions.removeIf(c -> c.getEntity() == owner || c.getEntity() instanceof Bullet);
        if(!collisions.isEmpty()) scene.removeEntity(this);
    }
}