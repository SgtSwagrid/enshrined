package game.render;

import game.properties.Colour;

public class Light {
    
    private float x, y, z;
    
    private float expAttenutation, linAttenutation;
    
    private Colour colour;
    
    public void setPos(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }
    
    public float getExpAtt() {
        return expAttenutation;
    }
    
    public void setExpAtt(float att) {
        expAttenutation = att;
    }
    
    public float getLinAtt() {
        return linAttenutation;
    }
    
    public void setLinAtt(float att) {
        linAttenutation = att;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    
    public void setColour(Colour colour) {
        this.colour = colour;
    }
    
    public Colour getColour() {
        return colour;
    }
}
