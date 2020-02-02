package game;

import game.entity.Cameraman;
import game.entity.Floor;
import game.math.Matrix4;
import game.player.Controls;
import game.player.Player;
import game.properties.Colour;
import game.render.Light;
import game.render.Scene3D;
import game.window.Window;
import game.world.Wall;
import game.world.WallGenerator;
import game.world.World;

import static org.lwjgl.glfw.GLFW.*;

public class Launch {
    
    public static void main(String[] args) {
        
        new World();
        
    }
}