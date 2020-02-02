package game.render;

import game.properties.Mesh;
import game.properties.Texture;
import game.entity.Transform;
import game.math.Matrix4;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

/**
 * Shader program for rendering quads.
 * @author Alec Dorrington
 */
public class WorldShader extends Shader {
    
    public static final float FOV = 90.0F;
    private static final float NEAR_CLIP = 0.3F;
    private static final float FAR_CLIP = 1000.0F;
    
    /**
     * Create tile shader program from GLSL source.
     */
    public WorldShader() {
        super("vertex.glsl", "fragment.glsl");
    }

    @Override
    protected void onBind() {
        glBindAttribLocation(getShaderID(), 0, "vertex");
        glBindAttribLocation(getShaderID(), 1, "texmap");
        glBindAttribLocation(getShaderID(), 2, "normal");
    }
    
    /**
     * Render the given set of tiles.
     * @param tiles to render.
     * @param width of the window (pixels).
     * @param height of the window (pixels).
     */
    public void render(GraphicsWorld world, int width, int height) {
        
        //Shader shader and tile mesh.
        glUseProgram(getShaderProgramId());
        
        //Load view matrix to account for window size.
        setUniform("view", world.getCamera().getViewMatrix());
        setUniform("projection", Matrix4.projection(FOV, (float)width/height, NEAR_CLIP, FAR_CLIP));
        
        List<Light> lights = world.getLights();
        
        for(int i = 0; i < 20; i++) {
            
            if(i < lights.size()) {
                
                Light light = lights.get(i);
                
                setUniform("lightPos["+i+"]", light);
                setUniform("lightColour["+i+"]", light.getColour());
                setUniform("lightExpAtt["+i+"]", light.getExpAtt());
                setUniform("lightLinAtt["+i+"]", light.getLinAtt());
                
            }
        }
        Transform cam = world.getCamera().getEntity().getTransform();
        setUniform("camPos", cam.getX(), cam.getY(), cam.getZ());
        setUniform("numLights", lights.size());
        
        //Render each tile.
        world.getRenderables().forEach(this::renderEntity);
        
        //Unload shader and tile mesh.
        unloadMesh();
        glUseProgram(0);
    }
    
    /**
     * Render a tile to the screen.
     * @param tile to render.
     */
    private void renderEntity(Renderable ren) {
        
        if(!ren.isVisible()) return;
        
        loadMesh(ren.getMesh());
        
        //Load texture.
        if(ren.getTexture().isPresent()) {
            loadTexture(ren.getTexture().get());
        }
        
        setUniform("transform", ren.getTransf());
        setUniform("colour", ren.getColour());
        setUniform("textured", ren.getTexture().isPresent());
        setUniform("lightingOn", ren.hasLighting());
        
        //Render tile.
        glDrawArrays(GL_TRIANGLES, 0, ren.getMesh().getNumVertices());
    }
    
    /**
     * Load a mesh to OpenGL.
     * @param mesh to load.
     */
    private void loadMesh(Mesh mesh) {
        
        //Load VAO.
        glBindVertexArray(mesh.getVaoId());
        
        //Load each VBO.
        for(int i = 0; i < 3; i++) {
            glEnableVertexAttribArray(i);
        }
    }
    
    /**
     * Unload current mesh.
     */
    private void unloadMesh() {
        
        //Unload each VBO.
        for(int i = 0; i < 3; i++) {
            glDisableVertexAttribArray(i);
        }
        
        //Unload VAO.
        glBindVertexArray(0);
    }
    
    /**
     * Load a texture to OpenGL.
     * @param texture to load.
     */
    private void loadTexture(Texture texture) {
        
        //Bind texture to TEXTURE0.
        glActiveTexture(GL_TEXTURE0);
        
        //Enable culling iff the texture is fully opaque.
        if(texture.isOpaque()) glEnable(GL_CULL_FACE);
        else glDisable(GL_CULL_FACE);
        
        //Load texture.
        glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
    }
    
    @Override
    protected void onInit() {}
}