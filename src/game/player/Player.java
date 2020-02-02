package game.player;

import game.entity.Cameraman;
import game.entity.Entity;
import game.entity.Model;
import game.input.InputHandler;
import game.input.InputHandler.KeyboardEvent;
import game.math.Matrix4;
import game.physics.Collider;
import game.physics.PhysicsWorld;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Light;
import game.render.Scene3D;
import game.world.Shrine;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;
import java.util.List;

public class Player extends Entity implements Damageable {
    
    public int score = 0;
    private final int MAX_HEALTH = 4000;
    private int health = MAX_HEALTH;
    private int SHOOT_INTERVAL = 250;
    
    private Collider collider;
    
    private final Controls controls;
    private Colour colour;
    
    private volatile float vx = 0, vy = 0;
    private float maxV = 0, a = 25.0F, turnSpeed = 200.0F;
    private float damping = 0.07F; // smaller = more damping
    
    private float angle;
    
    private Light light;
    private Scene3D scene;
    private Model model;
    
    private long lastShot = 0;
    
    private Cameraman cam;
    
    GraphicsWorld g; PhysicsWorld p;
    
    private boolean dead = false;
    private long deadTime;
    private final int RESPAWN_TIME = 5000;
    
    private List<Shrine> shrines = new LinkedList<>();
    
    private float x, y;
    
    //controls
    private volatile boolean left, down, right, up, shoot;
    
    public Player(Scene3D scene, Colour colour, Controls controls, float x, float y) {
        super(scene);
        this.x = x;
        this.y = y;
        this.controls = controls;
        this.colour = colour;
        this.scene = scene;
        light = new Light();
        light.setColour(Colour.WHITE.darken(100));
        light.setPos(0.0F, 0.0F, 2.0F);
        light.setExpAtt(0.15F);
        light.setLinAtt(0.004F);
    }
    
    public boolean isAlive() { return !dead; }
    
    public void setCam(Cameraman cam) { this.cam = cam; }
    
    public void registerShrine(Shrine shrine) {
        shrines.add(shrine);
    }
    
    public void incrementScore() { ++score; }
    
    public Light getLight() { return light; }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        getTransform().transfAbs(Matrix4.getTranslationMatrix(x, y, -0.2F));
        
        this.g = g; this.p = p;
        
        g.addLight(light);
        
        model = new Model(this, g, Shapes.PLAYER, colour, null);
        addComponent(model);
        collider = new Collider(this, p, 1.0F, 1.0F);
        addComponent(collider);
        
        controls.INPUT.getHandler().register(KeyboardEvent.class, e -> {
            if(e.ACTION == GLFW_PRESS) {
                
                left |= e.KEY == controls.MOVE_LEFT;
                down |= e.KEY == controls.MOVE_DOWN;
                right |= e.KEY == controls.MOVE_RIGHT;
                up |= e.KEY == controls.MOVE_UP;
                shoot |= e.KEY == controls.SHOOT;
                
            } else if(e.ACTION == GLFW_RELEASE) {
                
                left &= e.KEY != controls.MOVE_LEFT;
                down &= e.KEY != controls.MOVE_DOWN;
                right &= e.KEY != controls.MOVE_RIGHT;
                up &= e.KEY != controls.MOVE_UP;
                shoot &= e.KEY != controls.SHOOT;
            }
        });
    }
    
    @Override
    public void damage(int amount) {
        long respawnTime = System.currentTimeMillis() - deadTime - RESPAWN_TIME;
        if(respawnTime > 2000) {
            health -= amount;
            shrines.forEach(Shrine::resetCapture);
        }
    }
    
    public void addVelocity(float vx, float vy) {
        this.vx += vx;
        this.vy += vy;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        scene.removeLight(light);
    }
    
    private void kill() {
        g.removeRenderable(model);
        p.removeCollider(collider);
        g.removeLight(light);
        cam.untrack(this);
        deadTime = System.currentTimeMillis();
        dead = true;
    }
    
    private void respawn() {
        
        float dx = x - getTransform().getX();
        float dy = y - getTransform().getY();
        getTransform().transfAbs(Matrix4.getTranslationMatrix(dx, dy, 0.0F));
        
        g.addRenderable(model);
        p.addCollider(collider);
        g.addLight(light);
        cam.track(this);
        dead = false;
    }
    
    @Override
    public void update(long dt) {
        
        if(health <= 0) kill();
        
        if(dead && System.currentTimeMillis() - deadTime > RESPAWN_TIME) {
            respawn();
        }
        
        long respawnTime = System.currentTimeMillis() - deadTime - RESPAWN_TIME;
        if(respawnTime < 2000) {
            model.setColour(colour.mix(Colour.RISE_N_SHINE, 100-(int)(100*respawnTime/2000)));
        } else {
        
            model.setColour(colour.mix(Colour.RISE_N_SHINE,
                    100-100*health/MAX_HEALTH));
        }
        health = (int)Math.min(health + dt, MAX_HEALTH);
        
        if(left) angle += turnSpeed*dt/1000.0F;
        if(right)angle -= turnSpeed*dt/1000.0F;
        if(down) {
            vx -= Math.cos(Math.toRadians(angle))*a*dt/1000.0F;
            vy -= Math.sin(Math.toRadians(angle))*a*dt/1000.0F;
        }
        if(up) {
            vx += Math.cos(Math.toRadians(angle))*a*dt/1000.0F;
            vy += Math.sin(Math.toRadians(angle))*a*dt/1000.0F;
        }
        
        if(left || down || right || up) {
            //angle = (float)Math.toDegrees(Math.atan2(vy, vx));
        }
        
        if(shoot && System.currentTimeMillis() - lastShot > SHOOT_INTERVAL && !dead) {
            scene.addEntity(new Bullet(scene, this, getTransform().getX(),
                    getTransform().getY(), angle));
            lastShot = System.currentTimeMillis();
        }
        
        Matrix4 transf = Matrix4.getTranslationMatrix(
                getTransform().getX() + vx*dt/1000.0F,
                getTransform().getY() + vy*dt/1000.0F,
                0.0F);
        transf = transf.mul(Matrix4.getRotationMatrix(angle));
        
        getTransform().setTransf(transf);
        
        float d = (float)Math.pow(damping, dt/1000.0F);
        vx *= d;
        vy *= d;
        
        light.setPos(getTransform().getX(), getTransform().getY(), 3);
        
        if(angle == 0.0F) angle = 0.001F;
        
        List<Collider> colliders = scene.getPhysicsWorld().checkCollision(collider);
        colliders.removeIf(c -> c.getEntity() instanceof Bullet);
        
        if(!colliders.isEmpty()) {
            if(!(colliders.get(0).getEntity() instanceof Player)) {
                scene.getPhysicsWorld().moveToContact(collider, colliders.get(0), vx, vy);
            }
            float[] normal = scene.getPhysicsWorld().getNormal(collider, colliders.get(0), vx, vy);
            
            if(normal[0] != 0.0F) vx *= -1;
            else if(normal[1] != 0.0F) vy *= -1;
        }
    }
}