package game.world;

import game.math.Matrix4;
import game.player.Player;
import game.properties.Colour;
import game.render.Scene3D;

public class WallGenerator {
    
    private static final int width = 8, height = 6;
    private static final float spacing = 8.0F-0.5F;
    private static final float density = 0.4F;
    
    private static final int numShrines = 5;
    private static final int numMines = 15;
    private static final int numTurrets = 2;
    
    public static void generateWalls(Scene3D scene, Player p1, Player p2) {
        
        boolean[][] taken = new boolean[width][height];
        
        float left = -width/2 * spacing;
        float right = width/2 * spacing;
        float top = height/2 * spacing;
        float bottom = -height/2 * spacing;
        
        //horizontal walls
        for(float x = left+spacing/2; x <= right-spacing/2; x += spacing) {
            for(float y = bottom+spacing; y <= top-spacing; y += spacing) {
                if(Math.random() < density) {
                    createWall(scene, x, y, true, false);
                }
            }
        }
        
        //vertical walls
        for(float x = left+spacing; x <= right-spacing; x += spacing) {
            for(float y = bottom+spacing/2; y <= top-spacing/2; y += spacing) {
                if(Math.random() < density) {
                    createWall(scene, x, y, false, false);
                }
            }
        }
        
        //bottom walls
        for(float x = left+spacing/2; x <= right-spacing/2; x += spacing) {
            createWall(scene, x, bottom, true, true);
        }
        
        //top walls
        for(float x = left+spacing/2; x <= right-spacing/2; x += spacing) {
            createWall(scene, x, top, true, true);
        }
        
        //left walls
        for(float y = bottom+spacing/2; y <= top-spacing/2; y += spacing) {
            createWall(scene, left, y, false, true);
        }
        
        //right walls
        for(float y = bottom+spacing/2; y <= top-spacing/2; y += spacing) {
            createWall(scene, right, y, false, true);
        }
        generateShrines(scene, p1, Colour.NAVAL.darken(75), taken);
        generateShrines(scene, p2, Colour.CARMINE_PINK.darken(75), taken);
        generateMines(scene, p1, p2, taken);
    }
    
    private static void createWall(Scene3D scene, float x, float y, boolean horz, boolean invin) {
        
        Wall wall = new Wall(horz, invin, scene);
        wall.getTransform().transfRel(Matrix4
                .getTranslationMatrix(x, y, 0.25F));
        scene.addEntity(wall);
    }
    
    private static void generateShrines(Scene3D scene, Player player, Colour colour, boolean[][] taken) {
        
        float sx = -width/2 * spacing;
        float sy = -height/2 * spacing;
        
        for(int i = 0; i < numShrines; i++) {
            
            int x, y;
            
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while(taken[x][y] || inMiddle(x, y));
            taken[x][y] = true;
            
            float xOffset = Math.random()<0.5 ? 1.0F/3.0F : 2.0F/3.0F;
            float yOffset = Math.random()<0.5 ? 1.0F/3.0F : 2.0F/3.0F;
            
            float xx = sx + x*spacing + xOffset*spacing;
            float yy = sy + y*spacing + yOffset*spacing;
            
            //float x = (int) (Math.random() * width * spacing) + sx;
            //float y = (int) (Math.random() * height * spacing) + sy;
            
            scene.addEntity(new Shrine(scene, player, colour, xx, yy));
            
            
        }
    }
    
    private static boolean inMiddle(int x, int y) {
        
        return Math.abs(width/2 - 0.5F - x) < 2 && Math.abs(height/2 - 0.5F - y) < 2;
    }
    
    private static void generateMines(Scene3D scene, Player p1, Player p2, boolean[][] taken) {
        
        float minX = -width/2 * spacing, maxX = width/2 * spacing;
        float minY = -height/2 * spacing, maxY = height/2 * spacing;
        
        for(int i = 0; i < numMines; i++) {
            
            int x, y;
            
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while(taken[x][y]);
            taken[x][y] = true;
            
            float xOffset = Math.random()<0.5 ? 1.0F/3.0F : 2.0F/3.0F;
            float yOffset = Math.random()<0.5 ? 1.0F/3.0F : 2.0F/3.0F;
            
            float xx = minX + x*spacing + xOffset*spacing;
            float yy = minY + y*spacing + yOffset*spacing;
            
            scene.addEntity(new Mine(scene, xx, yy));
        }
        
        for(int i = 0; i < numTurrets; i++) {
            
            int x, y;
            
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while(taken[x][y] || inMiddle(x, y));
            taken[x][y] = true;
            
            float xOffset = Math.random()<0.5 ? 1.0F/3.0F : 2.0F/3.0F;
            float yOffset = Math.random()<0.5 ? 1.0F/3.0F : 2.0F/3.0F;
            
            float xx = minX + x*spacing + xOffset*spacing;
            float yy = minY + y*spacing + yOffset*spacing;
            
            scene.addEntity(new Turret(scene, xx, yy, p1, p2));
        }
    }
}
