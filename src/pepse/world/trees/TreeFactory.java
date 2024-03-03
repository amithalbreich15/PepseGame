package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class TreeFactory {
    private static final String LEAF_TAG = "leaf";
    private static final Color LEAF_COLOR = new Color(50,200,30);
    private GameObjectCollection gameObjects;
    private int trunkLayer;
    private static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 8;
    private int LEAF_LAYER = Layer.STATIC_OBJECTS + 10;;

    public TreeFactory(GameObjectCollection gameObjects, int trunkLayer){
        this.gameObjects = gameObjects;
        this.trunkLayer = trunkLayer;
    }

    public void createTree(int xLocation, int totalHeight, float groundHeight) {
        int leavesCount = totalHeight/2;
        Trunk trunk = new Trunk(gameObjects, xLocation, totalHeight, groundHeight, trunkLayer);
        createLeaves(xLocation, totalHeight, groundHeight, leavesCount);
    }


    private void createLeaves(int xLocation, int totalHeight, float groundHeight, int leavesCount) {
        Leaf[][] leaves = new Leaf[leavesCount][leavesCount];
        float leafXLocation = xLocation - ((leavesCount -0.5f)* Block.SIZE/2f);
        float leafYLocation = groundHeight -(totalHeight -(leavesCount /2f))*Block.SIZE;

        for (int i = 0; i < leaves.length; i++) {
            for (int j = 0; j < leaves.length; j++)  {
                    Vector2 topleft = new Vector2(leafXLocation + i * (Block.SIZE + 1), leafYLocation - j * (Block.SIZE + 1));
                    RectangleRenderable leafRend = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
                    Leaf temp = new Leaf(topleft, new Vector2(Block.SIZE, Block.SIZE), leafRend);
                    gameObjects.addGameObject(temp, LEAF_LAYER);
                    temp.setTag(LEAF_TAG);
                }
            }
    }
}
