package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class of GraphicLifeCounter represents a visual indication of life remaining for the user in the Bricker Game.
 * Manager of the Heart GameObjects which represents the lives left.
 */
public class GraphicLifeCounter extends GameObject {
    private static final int HEART_WIDTH = 50;
    private static final int HEART_HEIGHT = 50;
    private Counter livesCounter;
    private final int numOfLives;
    private final GameObjectCollection gameObjectCollection;
    private final GameObject[] heartObjects;
    private int prevLivesNum;
    private final Renderable renderable;
    private final Avatar avatar;

    /**
     * Construct a new GraphicLifeCounter instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param livesCounter Counter for remaining lives.
     * @param renderable  The renderable representing the object. Can be null, in which case
     * @param gameObjectCollection Total Game Objects participating in the Bricker Game.
     * @param numOfLives Total number of lives given to the user in the game. Doesn't change over time.
     * @param avatar
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Counter livesCounter,
                              Renderable renderable, GameObjectCollection gameObjectCollection,
                              int numOfLives, Avatar avatar) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.prevLivesNum = livesCounter.value();
        this.numOfLives = numOfLives;
        this.gameObjectCollection = gameObjectCollection;
        this.avatar = avatar;
        this.heartObjects = new GameObject[this.numOfLives];
        this.renderable = renderable;
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        createGraphicLives(renderable);

    }

    /**
     * increments the livesCounter by 1.
     */
    public void incrementLivesCounter() {
        livesCounter.increment();
    }

    /**
     * increments the livesCounter by 1.
     */
    public void decrementLivesCounter() {
        livesCounter.decrement();
    }


    /**
     * Getter for livesCounter
     * @return return the livesCounter.
     */
    public Counter getLivesCounter() {
        return this.livesCounter;
    }

    /**
     * Updates the GraphicLifeCounter instance over time.
     * Reducing the visuals of the heart representing lives over time period, removes the heart Object when the
     * user loses lives by one at each time and increases by one life at each time when gaining life from falling life.
     * Cannot get more than 4 lives - game restriction.
     * @param deltaTime Game clock.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.livesCounter.value() < prevLivesNum && livesCounter.value() >= 0) {

            boolean wasHeartRemoved = this.gameObjectCollection.removeGameObject
                    (heartObjects[livesCounter.value()], Layer.FOREGROUND);
            if (wasHeartRemoved)
            {
                this.heartObjects[livesCounter.value()] = null;
                this.prevLivesNum = this.livesCounter.value();
            }
        }
        if (prevLivesNum < livesCounter.value()) {
            GameObject heartSymbol = new GameObject(
                    new Vector2(this.getTopLeftCorner().x()*(livesCounter.value()),this.getTopLeftCorner().y())
                    ,new Vector2(HEART_WIDTH, HEART_HEIGHT),renderable);
            this.gameObjectCollection.addGameObject(heartSymbol, Layer.UI);
            heartObjects[livesCounter.value() - 1] = heartSymbol;
            this.prevLivesNum = this.livesCounter.value();
        }
    }

    /**
     * Creates the GraphicLives visual indication on the screen and places them on the screen window.
     * @param renderable An image renderable to draw the heart object on screen.
     */
    private void createGraphicLives(Renderable renderable) {
        for (int i = 0; i < numOfLives - 1; i++) {
            GameObject heartSymbol = new GameObject(
                    new Vector2(this.getTopLeftCorner().x()+(HEART_WIDTH + 5)*i,this.getTopLeftCorner().y())
                    ,new Vector2(HEART_WIDTH, HEART_HEIGHT),renderable);
            heartSymbol.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            this.gameObjectCollection.addGameObject(heartSymbol, Layer.FOREGROUND);
            heartObjects[i] = heartSymbol;
        }
    }


}
