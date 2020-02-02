package game.entity;

public abstract class Component {
    
    private Entity entity;
    public Entity getEntity() { return entity; }
    
    protected Component(Entity entity) { this.entity = entity; }
    
    protected abstract void init();
    
    public abstract void update(long deltaMs);
    
    protected abstract void destroy();
}