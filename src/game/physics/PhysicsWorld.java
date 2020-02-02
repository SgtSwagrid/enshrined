package game.physics;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import game.math.Matrix4;

public class PhysicsWorld {
    
private Set<Collider> colliders = new HashSet<>();
    
    public void addCollider(Collider col) {
        colliders.add(col);
    }
    
    public void removeCollider(Collider col) {
        colliders.remove(col);
    }
    
    public Set<Collider> getColliders() {
        return Collections.unmodifiableSet(colliders);
    }
    
    public void update(long delta) {
        
    }
    
    public List<Collider> checkCollision(Collider collider) {
        
        List<Collider> colliders = new LinkedList<>();
        
        for(Collider c : this.colliders) {
            
            if(c == collider) continue;
            
            boolean xOverlap = (collider.getMinX() >= c.getMinX() && collider.getMinX() <= c.getMaxX())
                    || (collider.getMaxX() >= c.getMinX() && collider.getMaxX() <= c.getMaxX())
                    || (c.getMinX() >= collider.getMinX() && c.getMinX() <= collider.getMaxX())
                    || (c.getMaxX() >= collider.getMinX() && c.getMaxX() <= collider.getMaxX());
            
            boolean yOverlap = (collider.getMinY() >= c.getMinY() && collider.getMinY() <= c.getMaxY())
                    || (collider.getMaxY() >= c.getMinY() && collider.getMaxY() <= c.getMaxY())
                    || (c.getMinY() >= collider.getMinY() && c.getMinY() <= collider.getMaxY())
                    || (c.getMaxY() >= collider.getMinY() && c.getMaxY() <= collider.getMaxY());
            
            if(xOverlap && yOverlap) colliders.add(c);
        }
        return colliders;
    }
    
    public void moveToContact(Collider obj, Collider contact, float vx, float vy) {
        
        float dx = 0;
        if(vx > 0) {
            dx = contact.getMinX() - obj.getMaxX();
        } else if(vx < 0) {
            dx = contact.getMaxX() - obj.getMinX();
        }
        
        float dy = 0;
        if(vy > 0) {
            dy = contact.getMinY() - obj.getMaxY();
        } else if(vy < 0) {
            dy = contact.getMaxY() - obj.getMinY();
        }
        
        if(Math.abs(dx) < Math.abs(dy)) {
            dy = dx * vy/vx;
        } else {
            dx = dy * vx/vy;
        }
        
        obj.getEntity().getTransform().transfAbs(
                Matrix4.getTranslationMatrix(dx, dy, 0.0F));
    }
    
    public float[] getNormal(Collider obj, Collider contact, float vx, float vy) {
        
        float dx = 0;
        if(vx > 0) {
            dx = contact.getMinX() - obj.getMaxX();
        } else if(vx < 0) {
            dx = contact.getMaxX() - obj.getMinX();
        }
        
        float dy = 0;
        if(vy > 0) {
            dy = contact.getMinY() - obj.getMaxY();
        } else if(vy < 0) {
            dy = contact.getMaxY() - obj.getMinY();
        }
        
        if(Math.abs(dx) < Math.abs(dy)) {
            return new float[] {dx>0?1.0F:-1.0F, 0.0F};
        } else {
            return new float[] {0.0F, dy>0?1.0F:-1.0F};
        }
    }
}