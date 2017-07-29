package engine;

/**
 * Contains our game loop code. Implements Runnable interface because the game loop will be run inside a separate thread.
 * Delegates the input and update methods to the IGameLogic instance. The render method is delegated to the IGameLogic instance
 * and updates the window.
 */
public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private final Window window;
    private final Thread gameLoopThread;
    private final Timer timer;
    private final IGameLogic gameLogic;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {

        // Create a new thread that executes the run method of our GameEngine, which will contain our game loop.
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = new Timer();

    }

    /**
     * Start our Thread so the run method will be executed asynchronously. Performs initialization tasks and will run the game
     * loop until our window is closed. Is handled differently based on OS.
     */
    public void start() {

        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            gameLoopThread.run();
        }
        else {
            gameLoopThread.start();
        }

    }

    @Override
    public void run() {

        try {
            init();
            gameLoop();
        }
        catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }

    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init();
    }

    /**
     * Pseudocode for my first game loop.
     */
    private void gameLoop() {

        // Set up the elapsed time, the accumulated time, and the target update interval
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running) {

            // Set the elapsed time and increment the accumulated time
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            // Get input
            input();

            while (accumulator >= interval) {

                // Update the game state and decrement the accumulated time
                update(interval);
                accumulator -= interval;

            }

            // Render the game
            render();

            // Avoid issues with sleep accuracy by:
            // 1. Calculate the time we should exit the wait method and start another iteration of our game loop
            // 2. Compare the current time with taht end time and wait just a millisecond if we've not reached that time
            if (!window.isvSync()) {
                sync();
            }

        }

    }

    /**
     * Calculate the number of seconds our game loop iteration should last (loopSlot) and wait for that amount of time taking into
     * consideration the amount of time we spent in our loop.
     *
     */
    private void sync() {

        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {

            // Only wait in millisecond increments
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException ie) {
            }

        }

    }

    protected void input() {
        gameLogic.input(window);
    }

    protected void update(float interval) {
        gameLogic.update(interval);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }

}
