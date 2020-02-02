package game.world;

import game.entity.Entity;
import game.entity.Model;
import game.math.Matrix4;
import game.physics.Collider;
import game.physics.PhysicsWorld;
import game.player.Player;
import game.properties.Colour;
import game.properties.Shapes;
import game.render.GraphicsWorld;
import game.render.Light;
import game.render.Scene3D;

public class Shrine extends Entity {
    
    private final float CAPTURE_RANGE = 2.5F;
    private final long CAPTURE_TIME = 2000L;
    
    private Model model;
    
    private Player player;
    private Colour colour;
    private float x, y;
    
    private Light light;
    
    private boolean captured = false;
    private long captureProgress = 0L;
    
    public Shrine(Scene3D scene, Player player, Colour colour, float x, float y) {
        super(scene);
        this.player = player;
        this.colour = colour;
        this.x = x;
        this.y = y;
        
        light = new Light();
        light.setColour(Colour.PERIWINKLE.darken(100));
        light.setPos(x, y, 2.5F);
        light.setExpAtt(0.3F);
        light.setLinAtt(0.8F);
    }
    
    @Override
    public void init(GraphicsWorld g, PhysicsWorld p) {
        
        g.addLight(light);
        
        model = new Model(this, g, Shapes.SHRINE, colour, null);
        addComponent(model);
        addComponent(new Collider(this, p, 1.0F, 1.0F));
        
        getTransform().transfRel(Matrix4.getTranslationMatrix(x, y, 0.0F));
        //getTransform().transfRel(Matrix4.getScaleMatrix(1.0F, 1.0F, 2.0F));
    }
    
    public void resetCapture() {
        captureProgress = 0;
    }
    
    @Override
    public void update(long dt) {
        
        if(!captured) {
            if(distanceTo(player) < CAPTURE_RANGE) {
                captureProgress += dt;
                model.setColour(colour.lighten((int)(200*captureProgress/CAPTURE_TIME)));
                light.setColour(Colour.TURBO.darken(25));
            } else {
                captureProgress = 0;
                model.setColour(colour);
                light.setColour(Colour.PERIWINKLE.darken(100));
            }
            
            if(captureProgress >= CAPTURE_TIME) {
                
                model.setColour(Colour.WILD_CARIBBEAN_GREEN);
                light.setColour(Colour.WILD_CARIBBEAN_GREEN);
                captured = true;
                player.incrementScore();
            }
        }
    }
    
    private float distanceTo(Entity entity) {
        
        float dx = getTransform().getX() - entity.getTransform().getX();
        float dy = getTransform().getY() - entity.getTransform().getY();
        
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
}
