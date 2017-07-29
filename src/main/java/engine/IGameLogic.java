package engine;

/**
 * Encapsulates game logic. This makes the engine reusable across different games. Contains methods to get the input, update the
 * game state, and render game-specific data.
 */
public interface IGameLogic {

    void init() throws Exception;

    void input(Window window);

    void update(float interval);

    void render(Window window);

    void cleanup();

}
