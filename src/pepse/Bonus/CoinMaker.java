package pepse.Bonus;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class CoinMaker {

    private static final String[] coinFrames = {"assets/goldCoin1.png", "assets/goldCoin2.png", "assets/goldCoin3.png",
            "assets/goldCoin4.png","assets/goldCoin5.png","assets/goldCoin6.png","assets/goldCoin7.png",
        "assets/goldCoin8.png", "assets/goldCoin9.png"};
    private final AnimationRenderable coinAnimation;
    private static final double updateFrameTime = 0.1;
    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> groundHeightFunc;
    private final Random random;
    private final Vector2 windowDimensions;
    private static final int COIN_SIZE = 70;
    private final ImageReader imageReader;
    public static final int COIN_LAYER = Layer.STATIC_OBJECTS - 30;
    private final int coinLayer;
    public static final String COIN_TAG = "Coin";
    private static final int MAX_COINS = 4;
    private Counter coinCounter;
    private int numOfCoins;


    /**
     * Construct a new GameObject instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public CoinMaker(Vector2 topLeftCorner, Vector2 windowDimensions,
                     ImageReader imageReader, GameObjectCollection gameObjects,
                     Function<Float, Float> groundHeightFunc, int seed, int coinLayer,
                     Counter coinCounter, int numOfCoins) {
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.gameObjects = gameObjects;
        this.groundHeightFunc = groundHeightFunc;
        this.random = new Random(Objects.hash(60, seed));
        this.coinLayer = coinLayer;
        this.coinAnimation = new AnimationRenderable(coinFrames, imageReader, true, 2.5);
        this.coinCounter = coinCounter;
        this.numOfCoins = numOfCoins;

    }

    public void createInRange(int minX, int maxX, int minY) {
        float curX = minX;
        int maxY = Block.round((int)(Math.ceil(groundHeightFunc.apply(curX) - 100)));
        while (curX<= maxX) {
            if (random.nextInt(20) == 1) {
                int randY = random.nextInt(maxY-minY)+minY;
                if (numOfCoins < MAX_COINS)
                {
                    Coin coin = new Coin(new Vector2(curX, randY),new Vector2(COIN_SIZE, COIN_SIZE),
                            coinAnimation,coinLayer,coinCounter);
                    numOfCoins++;
                    coin.renderer().setRenderable(coinAnimation);
                    gameObjects.addGameObject(coin,coinLayer);
                    coin.setTag(COIN_TAG);
                }
                if (numOfCoins == MAX_COINS){
                    numOfCoins =0;
                }
                curX += Block.SIZE*20;
            }
        }
    }
}
