package game.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import game.physics.PhysicsWorld;
import game.render.GraphicsWorld;
import game.render.Scene3D;

public class Entity {
    
    private Map<Class<? extends Component>, List<Component>> components = new HashMap<>();
    private Transform transform = new Transform(this);
    
    private Scene3D scene;
    
    public Entity(Scene3D scene) {
        this.scene = scene;
        addComponent(transform);
    }
    
    public Scene3D getScene() { return scene; }
    
    public List<Component> getComponents(Class<? extends Component> type) {
        return Collections.unmodifiableList(components.get(type));
    }
    
    public Transform getTransform() {
        return transform;
    }
    
    public void addComponent(Component component) {
        
        if(!components.containsKey(component.getClass())) {
            components.put(component.getClass(), new LinkedList<>());
        }
        components.get(component.getClass()).add(component);
        component.init();
    }
    
    public void update(long deltaMs) {
        components.forEach((t, c) -> c.forEach(comp -> comp.update(deltaMs)));
    }
    
    public void destroy() {
        components.forEach((t, c) -> c.forEach(Component::destroy));
    }
    
    public void init(GraphicsWorld g, PhysicsWorld p) {}
}