package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glViewport;

import engine.IGameLogic;
import engine.Window;

/**
 * Simple game logic class that increases and decreases the clear color of the window whenever we press
 * the up or down key respectively. The render method will just clear the window with that color.
 */
public class DummyGame implements IGameLogic {

    private int direction = 0;
    private float color = 0.0f;
    private final Renderer renderer;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
    }

    @Override
    public void input(Window window) {

        // Set direction to 1, -1, or default to 0
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }

    }

    @Override
    public void update(float interval) {

        // Increment the color by a fraction of the direction
        color += direction * 0.01f;

        // Check the boundaries on the color (between 0 and 1)
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }

    }

    @Override
    public void render(Window window) {

        // Check to see if the window is being resized to update the viewport
        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // Set the clear color of the window
        window.setClearColor(color, color, color, 0.0f);
        renderer.clear();

    }

}
