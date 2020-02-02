package game.render;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import game.properties.Colour;
import game.entity.Entity;
import game.input.InputHandler;
import game.physics.PhysicsWorld;
import game.window.Window;
import game.window.Window.Scene;

/**
 * Scene of 2D tiles to display in window.
 * @author Alec Dorrington
 */
public class Scene3D implements Scene {
    
    /** Scene tile shader. */
    protected WorldShader shader = new WorldShader();
    
    protected InputHandler input = new InputHandler();
    
    private Set<Entity> entities = new HashSet<>();
    
    protected GraphicsWorld gWorld = new GraphicsWorld();
    
    private PhysicsWorld pWorld = new PhysicsWorld();
    
    public InputHandler getHandler() { return input; }
    
    public void addLight(Light light) {
        gWorld.addLight(light);
    }
    
    public void removeLight(Light light) {
        gWorld.removeLight(light);
    }

    @Override
    public void init(Window window, int width, int height) {
        
        //Initialize shader.
        input.init(window);
        shader.init();
    }

    @Override
    public void render(int width, int height) {
        
        //Render tiles.
        shader.render(gWorld, width, height);
    }
    
    @Override
    public void update(long delta) {
        new LinkedList<>(entities).forEach(e -> e.update(delta));
        pWorld.update(delta);
    }
    
    public PhysicsWorld getPhysicsWorld() { return pWorld; }
    
    @Override
    public Colour getColour() {
        return Colour.LYNX_WHITE;
    }

    @Override
    public void destroy() {
        shader.destroy();
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
        entity.init(gWorld, pWorld);
    }
    
    public void removeEntity(Entity entity) {
        entity.destroy();
        entities.remove(entity);
    }
}