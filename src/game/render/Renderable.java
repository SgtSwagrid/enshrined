package game.render;

import java.util.Optional;

import game.math.Matrix4;
import game.properties.Colour;
import game.properties.Mesh;
import game.properties.Texture;

public interface Renderable {
    
    Mesh getMesh();
    
    Colour getColour();
    
    Optional<Texture> getTexture();
    
    Matrix4 getTransf();
    
    boolean isVisible();
    
    boolean hasLighting();
}