package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.util.ColorSupplier;

import java.awt.*;

/**
 * Class of Trunk Object.
 */
public class Trunk {
    private static final Color TRUNK_COLOR = new Color(100,50,20, 50);
    public static final String TRUNK_TAG = "trunk";
    private final int trunkHeight;
    private final float groundHeight;
    private final int xLocation;

    /**
     * Constructor for Trunk Object.
     * @param gameObjects Game Object
     * @param xLocation x coordinate to create tree in.
     * @param trunkHeight Total tree Height.
     * @param groundHeight Ground Height (Terrain).
     * @param layer Trunk's Layer.
     */
    public Trunk(GameObjectCollection gameObjects, int xLocation, int trunkHeight, float groundHeight,int layer) {
        this.trunkHeight = trunkHeight;
        this.groundHeight = groundHeight;
        this.xLocation = xLocation;
        createTrunk(gameObjects, layer);
    }

    /**
     * Create a single Trunk Object.
     * @param gameObjects Game Object Collection of the game.
     * @param layer Trunk's Layer.
     */
    private void createTrunk(GameObjectCollection gameObjects, int layer){
        for (int i = 0; i <= trunkHeight; i++) {
            Vector2 topLeft = new Vector2(xLocation,groundHeight - (i*Block.SIZE));
            RectangleRenderable blkRend = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
            Block block = new Block(topLeft, blkRend);
            gameObjects.addGameObject(block, layer);
            block.setTag(TRUNK_TAG);
        }
    }
}
