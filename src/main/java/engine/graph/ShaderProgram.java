package engine.graph;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    /**
     * Creates a new program in OpenGL and provides methods to add vertex and fragment shaders. Those shaders and compiled and attached
     * to the OpenGL program. When all shaders are attached the link method should be invoked, which links all the code and verifies that
     * everything has been done correctly.
     *
     * @throws Exception exception
     */
    public ShaderProgram() throws Exception {

        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }

    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * Create the shader, compile it, then attach it to the program.
     *
     * @param shaderCode the shader code
     * @param shaderType the shader type
     * @return the shader id
     * @throws Exception exception
     */
    public int createShader(String shaderCode, int shaderType) throws Exception {

        // Create the shader and check to see if it was created
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        // Compile the shader source based on the id and code, then check to see if it was created
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        // Attach the shader to the program
        glAttachShader(programId, shaderId);

        return shaderId;

    }

    /**
     * Link the program, free up the shaders, and validate that shader is correct given the current OpenGL state.
     *
     * @throws Exception exception
     */
    public void link() throws Exception {

        // Link the program and check to see if it linked
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        // The shader program has been linked, so the compiled vertex and fragment shaders can be freed up
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        // Verification is done through the glValidateProgram() call and is used mainly for debugging purposes. Tries to validate if the
        // shader is correct given the current OpenGL state, but it can fail in some cases even if the shader is correct. Prints error.
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    /**
     * Activate the program for rendering.
     */
    public void bind() {
        glUseProgram(programId);
    }

    /**
     * Stop using the program for rendering.
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Invoked when the game loop finishes.
     */
    public void cleanup() {

        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }

    }

}
