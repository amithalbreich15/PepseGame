package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Trunk {
    private static final Color TRUNK_COLOR = new Color(100,50,20, 50);
    public static final String TRUNK_TAG = "trunk";
    private final int trunkHeight;
    private static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 8;
    private final float groundHeight;
    private final int xLocation;




    public Trunk(GameObjectCollection gameObjects, int xLocation, int trunkHeight, float groundHeight,int layer) {
        this.trunkHeight = trunkHeight;
        this.groundHeight = groundHeight;
        this.xLocation = xLocation;
        createTrunk(gameObjects, layer);
    }


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
