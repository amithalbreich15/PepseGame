package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.Bonus.*;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Leaf;
import pepse.world.trees.Tree;
import pepse.world.trees.Trunk;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;


public class PepseGameManager extends GameManager {


    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 10;
    private static final int TERRAIN_TOP_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int TRUNK_LAYER = Layer.STATIC_OBJECTS -1;
    private static final float COINS_BAG_SIZE = 85;
    private static final String COIN_COUNTER_TAG = "coinCounter";
    private static final float CURSOR_SIZE = 40;
    private static final float MID_FACTOR = 0.5F;
    private static final float COIN_LOC = 0.88f;
    private static final Vector2 FIGURE_LOC = new Vector2(70,40);
    private final int COIN_LAYER = Layer.STATIC_OBJECTS - 30;
    private static final int SEED_BOUND = 9;
    private static final int ENERGY_COUNTER_SIZE = 50;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private int leftBound;
    private int rightBound;
    private Vector2 windowsDimensions;
    private Terrain terrain;
    private Tree tree;
    private Avatar avatar;
    private Vector2 initialAvatarLocation;
    private Counter energyCounter;
    private CoinNumericCounter coinNumericCounter;
    private CoinMaker coinMaker;
    private Random random;
    private GameObject coinsBag;
    public static boolean isDragonActive = false;
    private Dragon dragon;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private final int numOfLives = 9;
    private Counter livesCounter;
    private GraphicLifeCounter graphicLifeCounter;
    private static final int HEART_HEIGHT = 80;
    private static final int HEART_WIDTH = 80;
    private GameObject figureProfile;
    private GameObject figureName;
    private static final int MAX_COINS = 4;
    private Counter coinCounter;
    private int prevSummon;
    private int numOfCoins;
    private WindowController windowController;
    private int prevLivesNum;
    private int seed;

//    /**
//     *
//     * @param windowTitle the title
//     * @param windowDimensions the window size
//     */
//    public PepseGameManager(String windowTitle, Vector2 windowDimensions) {
//        super(windowTitle, windowDimensions);
//    }

    /**
     * Runs the entire simulation.
     *
     * @param args This argument should not be used.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
//        new PepseGameManager("Tommy's Adventure",
//                new Vector2(1650,900)).run();
    }

    /**
     * Initializes the Game and runs it will all the Game Object.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowsDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        this.imageReader = imageReader;
        Renderable cursorImage = imageReader.readImage("assets/cursor.png",true);
        this.windowController.setMouseCursor(cursorImage,new Vector2(CURSOR_SIZE,CURSOR_SIZE),Vector2.ZERO);
        this.inputListener = inputListener;
        this.initialAvatarLocation = new Vector2(windowsDimensions.mult(MID_FACTOR));
        this.leftBound = (int) (-MID_FACTOR*windowController.getWindowDimensions().x());
        this.rightBound = (int) (3*MID_FACTOR*windowController.getWindowDimensions().x());
        this.random = new Random();
        this.coinCounter = new Counter();
        this.numOfCoins = 0;
        this.livesCounter = new Counter(numOfLives);
        this.prevSummon = 0;
        this.prevLivesNum = livesCounter.value();
        this.seed = new Random().nextInt(SEED_BOUND)+2;


        Renderable coinsBagRend = imageReader.readImage("assets/CoinBag.png", true);
        this.coinsBag = new GameObject(new Vector2(windowsDimensions.x()*COIN_LOC, windowsDimensions.y()*0.88f)
                ,new Vector2(COINS_BAG_SIZE,COINS_BAG_SIZE),coinsBagRend);
        gameObjects().addGameObject(coinsBag, Layer.FOREGROUND);
        coinsBag.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        Renderable figureRend = imageReader.readImage("assets/Tommy.png", false);
        this.figureProfile = new GameObject(FIGURE_LOC
                ,new Vector2(COINS_BAG_SIZE,COINS_BAG_SIZE),figureRend);
        figureProfile.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(figureProfile, Layer.FOREGROUND);

        Renderable figureNameRend = imageReader.readImage("assets/TommyName.png", true);
        this.figureName = new GameObject(new Vector2(190, 40)
                ,new Vector2(240,80),figureNameRend);
        figureName.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(figureName, Layer.FOREGROUND);

        Sky.create(gameObjects(), windowsDimensions, Layer.BACKGROUND);

        // Create Numeric Energy Counter
        createNumericEnergyCounter();

        //create terrain
        this.terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowsDimensions, seed);
        terrain.createInRange(leftBound,rightBound);

        //create Trees
        tree = new Tree(gameObjects(),TRUNK_LAYER, LEAF_LAYER, seed, terrain::groundHeightAt, windowsDimensions);
        tree.createInRange(leftBound,rightBound);

        // create Night
        Night.create(gameObjects(), Layer.FOREGROUND, windowsDimensions, 30);

        // create Sun
        GameObject sun = Sun.create(gameObjects(),Layer.BACKGROUND + 1, windowsDimensions, 30);

        // create sun halo
        SunHalo.create(gameObjects(),Layer.BACKGROUND + 2, sun, HALO_COLOR);


        // Create Graphic Lives visualization
        createGraphicLives(imageReader);

        // creates Avatar
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER,
                new Vector2(windowsDimensions.x()/2,
                        terrain.groundHeightAt(windowsDimensions.x()/2)-100f),
                inputListener, imageReader);
        Camera camera = new Camera(avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(), windowController.getWindowDimensions());
        setCamera(camera);

        // Create Dragon
        dragon = new Dragon(new Vector2(avatar.getCenter().x(), avatar.getCenter().y() - 300)
                ,windowsDimensions, imageReader,gameObjects(),inputListener,terrain::groundHeightAt,
                random,AVATAR_LAYER,avatar, isDragonActive, livesCounter, prevLivesNum, coinCounter);

        // Create coinMaker
        coinMaker = new CoinMaker(new Vector2(windowsDimensions.x(),windowsDimensions.y()),windowsDimensions,imageReader,
                gameObjects(),terrain::groundHeightAt,seed,COIN_LAYER, coinCounter, numOfCoins);
        coinMaker.createInRange(leftBound,rightBound,-700);

        //Create Coin Numeric Counter
        this.coinNumericCounter = new CoinNumericCounter(coinCounter,new Vector2( windowsDimensions.x()*0.95f,
                windowsDimensions.y()*0.91f),new Vector2(50,50),avatar);
        gameObjects().addGameObject(coinNumericCounter, Layer.UI);
        coinNumericCounter.setTag(COIN_COUNTER_TAG);

        // Create Fire and Blast Maker
        FireBlastsMaker fireBlastsMaker = new FireBlastsMaker(Vector2.ZERO, Vector2.ZERO,windowsDimensions,
                imageReader,soundReader, gameObjects(),inputListener,AVATAR_LAYER,avatar, dragon, 0, livesCounter);
        gameObjects().addGameObject(fireBlastsMaker,Layer.BACKGROUND);

        // Handle Layers Collision Logic
        gameObjects().layers().shouldLayersCollide(LEAF_LAYER, TERRAIN_TOP_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TERRAIN_TOP_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TRUNK_LAYER, true);
        gameObjects().layers().shouldLayersCollide(COIN_LAYER, AVATAR_LAYER, true);


    }

    /**
     * creating Numeric energy Counter which represents in Numeric way
     * haw many energy left to the avatar in the game.
     */
    private void createNumericEnergyCounter() {
        this.energyCounter = new Counter();
        GameObject numericEnergyCounter = new NumericEnergyCounter(energyCounter,
                new Vector2(windowsDimensions.x()* 0.55f, windowsDimensions.y()*0.03f),
                new Vector2(ENERGY_COUNTER_SIZE, ENERGY_COUNTER_SIZE),
                gameObjects());
        gameObjects().addGameObject(numericEnergyCounter, Layer.FOREGROUND);
    }

    /**
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.energyCounter.reset();
        this.energyCounter.increaseBy((int) Avatar.getEnergy());
        infiniteWorld();
        if (numOfCoins == MAX_COINS) {
            numOfCoins = 0;
        }

        if (coinCounter.value() % 2 == 0 && coinCounter.value() != 0 && prevSummon != coinCounter.value()) {
            if (!isDragonActive) {
                gameObjects().addGameObject(dragon, AVATAR_LAYER);
                isDragonActive = true;
                prevSummon = coinCounter.value();
            }
        }
    }

    /**
     The infiniteWorld() method is responsible for generating new terrain, trees, and coins as the avatar
     moves to the right or left of the screen.
     It checks if the avatar's position is greater than the right bound minus half the window's width,
     or less than the left bound plus half the window's width.
     If either of these conditions are met, it creates new terrain, trees, and coins in the appropriate
     range. It also updates the left and right bounds, and removes any objects that are no longer visible on the screen.
     */
    private void infiniteWorld(){
        float boundChange = 0;
        if(avatar.getCenter().x() > rightBound-0.5*windowsDimensions.x()){
            terrain.createInRange(rightBound, (int)(rightBound+0.5*windowsDimensions.x()));
            tree.createInRange(rightBound, (int)(rightBound+0.5*windowsDimensions.x()));
            boundChange = 0.5f*windowsDimensions.x();
            if (coinCounter.value() <= MAX_COINS)
            {
                coinMaker.createInRange(leftBound, rightBound, -500);
            }
        }
        if(avatar.getCenter().x() < leftBound+0.5*windowsDimensions.x()){
            terrain.createInRange((int)(leftBound-0.5*windowsDimensions.x()), leftBound);
            tree.createInRange((int)(leftBound-0.5*windowsDimensions.x()), leftBound);
            boundChange = -0.5f*windowsDimensions.x();
            if (coinCounter.value() <= MAX_COINS)
            {
                coinMaker.createInRange(leftBound, rightBound, -500);
            }
        }
        if(boundChange!=0){
            rightBound+=boundChange;
            leftBound+=boundChange;
            removeInRange(leftBound, rightBound);
        }
    }

    /**
     * removeInRange method removes objects which are not within the given range of minX and maxX.
     * It checks the x-coordinate of the center of each object in the gameObjects collection and if the x-coordinate
     *is greater than maxX or smaller than minX and the object is one of the following tags: Terrain.TOP_FLOOR_TAG,
     * Leaf.LEAF, Trunk.TRUNK_TAG, CoinMaker.COIN_TAG, it removes that object from the gameObjects collection.
     * @param minX the lower bound of range for x-coordinate
     * @param maxX the upper bound of range for x-coordinate
     */

    private void removeInRange(int minX, int maxX){
        for(GameObject obj : gameObjects()){
            if((obj.getCenter().x() > maxX || obj.getCenter().x() < minX) &&
                    ((obj.getTag().equals(Terrain.TOP_FLOOR_TAG)) || (obj.getTag().equals(Leaf.LEAF))
                            || (obj.getTag().equals(Trunk.TRUNK_TAG)) || (obj.getTag().equals(CoinMaker.COIN_TAG)))){
                Vector2 vec = obj.getCenter();
                gameObjects().removeGameObject(obj);
            }
        }
    }

    /**
     * Creates GraphicLivesCounter on screen visualization.
     * @param imageReader Read the image
     */
    private void createGraphicLives(ImageReader imageReader) {
        Renderable livesImage =
                imageReader.readImage("assets/Heart.png", true);
        this.graphicLifeCounter = new GraphicLifeCounter(
                new Vector2(windowsDimensions.x()*0.02f, windowsDimensions.y()*0.92f),
                new Vector2(HEART_WIDTH, HEART_HEIGHT),livesCounter, livesImage, gameObjects(), numOfLives, avatar);
        graphicLifeCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(graphicLifeCounter,Layer.FOREGROUND);
    }

}
