package engine.graph;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

    private final int vaoId;
    private final int posVboId;
    private final int colourVboId;
    private final int idxVboId;
    private final int vertexCount;

    public Mesh(float[] positions, float[] colours, int[] indices) {

        FloatBuffer posBuffer = null;
        IntBuffer indicesBuffer = null;
        try {

            // Get the vertex count from the number of indices
            vertexCount = indices.length;

            // Create the VAO and bind to it
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Create the VBO and bind to it
            // Allocate memory for all the positions in the mesh, then put the floats in the buffer
            // Define structure of the data
            posVboId = glGenBuffers();
            posBuffer = memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Colour VBO
            colourVboId = glGenBuffers();
            FloatBuffer colourBuffer = memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            memFree(colourBuffer);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            idxVboId = glGenBuffers();
            indicesBuffer = memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Unbind the VBO and VAO
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

        } finally {

            // Unbind the vertices buffer
            if (posBuffer != null) {
                memFree(posBuffer);
            }

            if (indicesBuffer != null) {
                memFree(indicesBuffer);
            }

        }

    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {

        // Disable the vertex attribute array
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(colourVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glDeleteVertexArrays(vaoId);
        glBindVertexArray(0);

    }

}
