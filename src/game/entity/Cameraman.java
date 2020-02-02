package game.entity;

import game.math.Matrix4;
import game.physics.PhysicsWorld;
import game.player.Player;
import game.render.GraphicsWorld;
import game.render.Scene3D;
import game.render.WorldShader;

public class Cameraman extends Entity {
    
    private Player p1, p2;
    private boolean p1t = true, p2t = true;
    
    public Cameraman(Scene3D scene, Player p1, Player p2) {
        super(scene);
        this.p1 = p1; this.p2 = p2;
        p1.setCam(this); p2.setCam(this);
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        addComponent(new Camera(this, g));
        //getTransform().transfRel(Matrix4.getTranslationMatrix(0.0F, 0.0F, 10.0F));
    }
    
    public void untrack(Player p) {
        if(p == p1) p1t = false;
        else if(p == p2) p2t = false;
    }
    
    public void track(Player p) {
        if(p == p1) p1t = true;
        else if(p == p2) p2t = true;
    }
    
    private boolean end = false;
    public void end() {
        end = true;
    }
    
    public void update(long dt) {
        
        float x = (p1.getTransform().getX() + p2.getTransform().getX()) / 2.0F;
        float y = (p1.getTransform().getY() + p2.getTransform().getY()) / 2.0F;
        
        float width = Math.abs(p1.getTransform().getX() - p2.getTransform().getX())*4;
        float height = Math.abs(p1.getTransform().getY() - p2.getTransform().getY())*4;
        float fov = WorldShader.FOV;
        
        float xDist = width / (2*(float)Math.tan(fov/2));
        float yDist = height / (2*(float)Math.tan(fov/2));
        float dist = Math.max(xDist, yDist);
        
        float camX = getTransform().getX(), camY = getTransform().getY(), camZ = getTransform().getZ();
        
        if(!p1t || !p2t) dist = camZ;
        if(!p1t) {
            x = p2.getTransform().getX();
            y = p2.getTransform().getY();
        } else if(!p2t) {
            x = p1.getTransform().getX();
            y = p1.getTransform().getY();
        }
        
        if(dist < 5) dist = 5;
        
        if(end) { x = camX; y = camY; dist = 10; }
        
        getTransform().transfAbs(Matrix4.getTranslationMatrix((x-camX)/25.0F, (y-camY)/25.0F, (dist-camZ)/25.0F));
    }
}
