package game.entity;

import java.util.Optional;

import game.math.Matrix4;
import game.properties.Colour;
import game.properties.Mesh;
import game.properties.Texture;
import game.render.GraphicsWorld;
import game.render.Renderable;

public class Model extends Component implements Renderable {
    
    private GraphicsWorld world;
    
    private Mesh mesh;
    private Colour colour;
    private Texture texture;

    public Model(Entity entity, GraphicsWorld world, Mesh mesh, Colour colour, Texture texture) {
        super(entity);
        this.world = world;
        this.mesh = mesh;
        this.colour = colour;
        this.texture = texture;
        // TODO Auto-generated constructor stub
    }
    
    private boolean l = true;
    public void setLighting(boolean l) {
        this.l = l;
    }
    
    private boolean visible = true;
    
    public void setVisible(boolean visible) { this.visible = visible; }

    @Override
    public Mesh getMesh() {
        // TODO Auto-generated method stub
        return mesh;
    }

    @Override
    public Colour getColour() {
        // TODO Auto-generated method stub
        return colour;
    }

    @Override
    public Optional<Texture> getTexture() {
        // TODO Auto-generated method stub
        return Optional.ofNullable(texture);
    }

    @Override
    public Matrix4 getTransf() {
        // TODO Auto-generated method stub
        return getEntity().getTransform().getTransf();
    }

    @Override
    public boolean isVisible() {
        // TODO Auto-generated method stub
        return visible;
    }

    @Override
    protected void init() {
        world.addRenderable(this);
    }

    @Override
    public void update(long deltaMs) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void destroy() {
        world.removeRenderable(this);
    }
    
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    @Override
    public boolean hasLighting() {
        // TODO Auto-generated method stub
        return l;
    }
}