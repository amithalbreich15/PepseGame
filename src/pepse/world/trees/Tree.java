package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Class for Tree Object.
 */
public class Tree {
    private TreeFactory treeFactory;
    private GameObjectCollection gameObjects;
    private int trunkLayer;
    private int leafLayer;
    private int seed;
    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 10;
    private Function<Float, Float> groundHeightFunc;
    private Vector2 windowsDimensions;
    private static final int TRUNK_LAYER = Layer.STATIC_OBJECTS + 8;


    /**
     @param gameObjects: GameObjectCollection - The collection of gameObjects where the tree will be added
     @param trunkLayer: int - the layer of the trunk
     @param leafLayer: int - the layer of the leaves
     @param seed: int - the seed used to generate the randomness
     @param groundHeightFunc: Function<Float, Float> - the function to calculate the ground height
     @param windowsDimensions: Vector2 - the dimensions of the window in pixels
     */
    public Tree(GameObjectCollection gameObjects, int trunkLayer, int leafLayer, int seed,
                Function<Float, Float> groundHeightFunc, Vector2 windowsDimensions){
        this.gameObjects = gameObjects;
        this.trunkLayer = trunkLayer;
        this.leafLayer = leafLayer;
        this.treeFactory = new TreeFactory(gameObjects, trunkLayer);
        this.groundHeightFunc = groundHeightFunc;
        this.windowsDimensions = windowsDimensions;
        this.seed = seed;

    }

    /**
     Creates trees in a given range.
     @param minX The minimum x-coordinate for the range of the trees.
     @param maxX The maximum x-coordinate for the range of the trees.
     */
    public void createInRange(int minX, int maxX) {
        minX = Block.round(minX);
        maxX = Block.round(maxX);
        float curX = minX;
        Random ran = new Random(Objects.hash(60,seed));
        while (curX<= maxX) {
            int y = (int) Math.floor(groundHeightFunc.apply(curX));
            if (ran.nextInt(10) == 1) {
                if (Math.abs(curX - windowsDimensions.x()/2) > 2*Block.SIZE){
                    int x = 0;
                    treeFactory.createTree((int) curX, ran.nextInt(8)+4,
                            Block.round((int)(Math.floor(
                                    windowsDimensions.y() -groundHeightFunc.apply(curX)))));
                }

            }
            curX += Block.SIZE;
        }
    }
}
