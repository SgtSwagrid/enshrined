package game.window;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import game.properties.Colour;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.glfw.GLFWErrorCallback.*;

/**
 * A GLFW/OpenGL window.
 * @author Alec Dorrington
 */
public class Window {
    
    /** The ID of the window. */
    private long windowId;
    
    /** The size of the window (pixels). */
    private int width, height;
    
    /** The title of the window. */
    private String title;
    
    /** The scene shown in the window. */
    private Scene scene;
    
    /** Whether the window is currently open. */
    private volatile boolean open = false;
    
    
    
    /**
     * Create a new window. Use window.open() to open it.
     * @param width of window (pixels).
     * @param height of window (pixels).
     * @param title of window.
     * @param scene to display.
     * @param handler for use in handling input events.
     */
    public Window(int width, int height, String title, Scene scene) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.scene = scene;
    }
    
    /** @return the ID of the window/ */
    public long getWindowId() { return windowId; }
    
    /** @return the width of the window (pixels). */
    public int getWidth() { return width; }
    
    /** @return the height of the window (pixels). */
    public int getHeight() { return height; }
    
    /** @return the title of the window. */
    public String getTitle() { return title; }
    
    /**
     * Set a new title for this window.
     * @param title of this window.
     * @return this window.
     */
    public Window setTitle(String title) {
        this.title = title;
        return this;
    }
    
    /** @return the scene shown in the window. */
    public Scene getScene() { return scene; }
    
    /**
     * Open the window. Blocks execution until window closes.
     */
    public void open() {
        
        init();
        
        //Continue rendering while window is being resized.
        glfwSetFramebufferSizeCallback(windowId, (window, width, height) -> {
            this.width = width;
            this.height = height;
            glViewport(0, 0, width, height);
            render();
        });
        
        open = true;
        
        new Thread(() -> {
            
            long latest = System.currentTimeMillis();
            
            while(open) {
                /*try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                
                
                scene.update(System.currentTimeMillis() - latest);
                latest = System.currentTimeMillis();
            }
        });//.start();
        
        long latest = System.currentTimeMillis();
        
        //Update window and event handler until window is closed.
        while(open) {
            
            //if(latest != System.currentTimeMillis())
                //System.out.println(1000/(System.currentTimeMillis()-latest) + " fps");
            
            scene.update(System.currentTimeMillis() - latest);
            latest = System.currentTimeMillis();
            
            render();
            
            glfwPollEvents();
            glfwSetWindowTitle(windowId, title);
            open &= !glfwWindowShouldClose(windowId);
        }
        destroy();
    }
    
    /** @return whether the window is currently open. */
    public boolean isOpen() { return open; }
    
    /**
     * Close the window.
     * @return this window.
     */
    public Window close() {
        open = false;
        return this;
    }
    
    /**
     * Initialize and create a new window.
     */
    private void init() {
        
        //Initialize GLFW.
        glfwInit();
        glfwSetErrorCallback(createPrint(System.err));
        
        //Set version details.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        
        
        //Create and show window.
        windowId = glfwCreateWindow(width, height, title, 0, 0);
        glfwMakeContextCurrent(windowId);
        createCapabilities();
        //glfwSetWindowIcon(windowId, null);
        glfwShowWindow(windowId);
        
        //Initialize OpenGL settings.
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        //Initialize shader.
        scene.init(this, width, height);
    }
    
    /**
     * Render a frame to screen.
     */
    private void render() {
        
        //Clear colour/depth buffers.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Colour colour = scene.getColour();
        glClearColor(colour.R/255.0F, colour.G/255.0F, colour.B/255.0F, 1.0F);
        
        //Perform render.
        scene.render(width, height);
        
        
        glfwSwapBuffers(windowId);
    }
    
    /**
     * Destroy the window.
     */
    private void destroy() {
        //Destroy shader and window.
        scene.destroy();
        glfwDestroyWindow(windowId);
    }
    
    /**
     * Scene to render to screen.
     */
    public interface Scene {
        
        /**
         * Initialize scene shaders.
         * @param width of the window (pixels).
         * @param height of the window (pixels).
         * @param handler of window input events.
         */
        public void init(Window window, int width, int height);
        
        public void render(int width, int height);
        
        /**
         * Render the scene to the screen.
         * @param width of the window (pixels).
         * @param height of the window (pixels).
         */
        public void update(long delta);
        
        /**
         * @return the background colour of this scene.
         */
        public Colour getColour();
        
        /**
         * Perform cleanup upon window close.
         */
        public void destroy();
    }
}