package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.PerlinNoise;

import java.awt.*;


public class Terrain {
    //todo: maybe add deleteInrange func to avoid overload.

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public static final String TOP_FLOOR_TAG ="TopFloor";
    public static final String BOTTOM_FLOOR_TAG ="BottomFloor";

    private float groundHeightAtX0;
    private GameObjectCollection gameObjects;
    private int groundLayer;
    private Vector2 windowDimensions;
    private PerlinNoise perlinNoise;


    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.y()*0.5f;
        this.perlinNoise = new PerlinNoise(seed);
    }

    public void createInRange(int minX, int maxX) {
        minX = Block.round(minX);
        maxX = Block.round(maxX);
        float curX = minX;
        while (curX <= maxX){
            createStack((int)Math.floor((groundHeightAt(curX))/Block.SIZE), (int)curX);
            curX += Block.SIZE;
        }


    }
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
