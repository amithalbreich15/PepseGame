package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.PerlinNoise;
import pepse.util.ColorSupplier;
import java.awt.*;

/**
 * Class Terrain - Create ground of the game made of Blocks.
 */
public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public static final String TOP_FLOOR_TAG ="TopFloor";
    public static final String BOTTOM_FLOOR_TAG ="BottomFloor";

    private float groundHeightAtX0;
    private GameObjectCollection gameObjects;
    private int groundLayer;
    private Vector2 windowDimensions;
    private PerlinNoise perlinNoise;

    /**
     * Constructor for the Terrain class.
     * @param gameObjects collection of game objects that the terrain belongs to
     * @param groundLayer the layer on which the terrain will be rendered
     * @param windowDimensions the dimensions of the window in which the terrain will be rendered
     * @param seed the seed used to generate the Perlin noise for the terrain
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.y()*0.5f;
        this.perlinNoise = new PerlinNoise(seed);
    }

    /**
     * createInRange - creates a range of stacks of blocks with a given minimum and maximum x-coordinate
     * @param minX - the minimum x-coordinate for the range of stacks of blocks
     * @param maxX - the maximum x-coordinate for the range of stacks of blocks
     */
    public void createInRange(int minX, int maxX) {
        minX = Block.round(minX);
        maxX = Block.round(maxX);
        float curX = minX;
        while (curX <= maxX){
            createStack((int)Math.floor((groundHeightAt(curX))/Block.SIZE), (int)curX);
            curX += Block.SIZE;
        }
    }

    /**
     * createStack - creates a stack of blocks with a given block amount and starting x-coordinate
     * @param blockAmount - the number of blocks to be created in the stack
     * @param curX - the starting x-coordinate for the stack of blocks
     */
    private void createStack(int blockAmount, int curX){
        for (int i = 0; i <= blockAmount; i++) {
            Vector2 topleft = new Vector2(curX, Block.round((int) (windowDimensions.y()-(i*Block.SIZE))));
            RectangleRenderable blkRend = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
            Block newBlock = new Block(topleft,blkRend);
            if (i == blockAmount) {
                gameObjects.addGameObject(newBlock, groundLayer+1);
                // private static final int TERRAIN_TOP_LAYER = Layer.STATIC_OBJECTS + 1;
                newBlock.setTag(TOP_FLOOR_TAG);
            }
            else {
                gameObjects.addGameObject(newBlock, groundLayer);
                newBlock.setTag(BOTTOM_FLOOR_TAG);
            }
        }
    }


    /**
     * This method return the ground height at a given location.
     *
     * @param x A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x) {
        float groundHeight = (float) (Block.SIZE * perlinNoise.noise(x / Block.SIZE) *28);
        if (groundHeight < 0 ) {
            return groundHeightAtX0;
        } else if (groundHeight + groundHeightAtX0 > windowDimensions.y()) {
            return windowDimensions.y() - 90;
        }
        return groundHeight + groundHeightAtX0;
    }
}
