package game.world;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;

import game.entity.Cameraman;
import game.entity.Floor;
import game.input.InputHandler.KeyboardEvent;
import game.math.Matrix4;
import game.player.Controls;
import game.player.Player;
import game.properties.Colour;
import game.properties.Texture;
import game.render.Banner;
import game.render.Light;
import game.render.Scene3D;
import game.window.Window;

public class World extends Scene3D {
    
    
    private boolean started = false, ended = false;
    
    Player p1, p2;
    Cameraman c;
    
    public World() {
        
        Window window = new Window(1200, 900, "Enshrined", this);
        

        p1 = new Player(this, Colour.NAVAL,
                new Controls(getHandler(),
                GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_D, GLFW_KEY_S, GLFW_KEY_SPACE),
                -3.75F, -3.75F);
        
        p2 = new Player(this, Colour.CARMINE_PINK,
                new Controls(getHandler(),
                GLFW_KEY_UP, GLFW_KEY_LEFT, GLFW_KEY_RIGHT, GLFW_KEY_DOWN, GLFW_KEY_RIGHT_SHIFT),
                3.75F, 3.75F);
        
        
        //scene.addEntity(new Wall());
        addEntity(p1);
        addEntity(p2);
        
        WallGenerator.generateWalls(this, p1, p2);
        
        c = new Cameraman(this, p1, p2);
        addEntity(c);
        c.getTransform().transfRel(Matrix4.getTranslationMatrix(0, 0, 10));
        addEntity(new Floor(this));
        
        window.open();
    }
    
    @Override
    public void init(Window window, int width, int height) {
        
        super.init(window, width, height);
        
        Banner b = new Banner(this, 8, 6, new Texture("res/instructions.png"));
        addEntity(b);
        
        input.getHandler().register(KeyboardEvent.class, e -> {
            
            if(e.ACTION == GLFW_RELEASE && e.KEY == GLFW_KEY_ENTER && !started && !ended) {
                started = true;
                removeEntity(b);
            }
        });
    }
    

    @Override
    public void update(long delta) {
        
        
        if(started) super.update(delta);
        
        if(!ended) {
            
            if(p1.score >= 5) {
                started = false; ended = true;
                //c.getTransform().transfRel(Matrix4.getTranslationMatrix(0, 0, 10-c.getTransform().getZ()));
                Banner b = new Banner(this, 4, 6, new Texture("res/p1win.png"));
                c.end();
                b.getTransform().transfRel(Matrix4.getTranslationMatrix(c.getTransform().getX(), c.getTransform().getY(), 0));
                addEntity(b);
            }
            
            if(p2.score >= 5) {
                started = false; ended = true;
                //c.getTransform().transfRel(Matrix4.getTranslationMatrix(0, 0, 10-c.getTransform().getZ()));
                Banner b = new Banner(this, 4, 6, new Texture("res/p2win.png"));
                c.end();
                b.getTransform().transfRel(Matrix4.getTranslationMatrix(c.getTransform().getX(), c.getTransform().getY(), 0));
                addEntity(b);
            }
        } else {
            c.update(delta);
        }
    }
}