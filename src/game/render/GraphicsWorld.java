package game.render;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game.entity.Camera;

public class GraphicsWorld {
    
    private List<Renderable> renderables = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();
    private Camera camera;
    
    public void addRenderable(Renderable ren) {
        renderables.add(ren);
    }
    
    public void removeRenderable(Renderable ren) {
        renderables.remove(ren);
    }
    
    public List<Renderable> getRenderables() {
        return Collections.unmodifiableList(renderables);
    }
    
    public void addLight(Light light) {
        lights.add(light);
    }
    
    public void removeLight(Light light) {
        lights.remove(light);
    }
    
    public List<Light> getLights() {
        return Collections.unmodifiableList(lights);
    }
    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public Camera getCamera() { return camera; }
}