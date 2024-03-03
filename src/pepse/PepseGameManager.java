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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PepseGameManager extends GameManager {

    //todo: random seeds, trees, leaves, perlin noise HeightXAt,

    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int NIGHT_DAY_CYCLE = 30;
    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 10;
    private static final int TERRAIN_TOP_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int TRUNK_LAYER = Layer.STATIC_OBJECTS -1;
    private static final int TREE_LAYER  = Layer.STATIC_OBJECTS - 20;
    private static final float COINS_BAG_SIZE = 85;
    private static final String COIN_COUNTER_TAG = "coinCounter";
    private static final float CURSOR_SIZE = 40;
    private final int COIN_LAYER = Layer.STATIC_OBJECTS - 30;
    private static final int SEED_BOUND = 15;
    private static final int INITIAL_ENERGY = 100;
    private static final int ENERGY_LOCATION = 400;
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
    private static final float MAX_ENERGY = 100;
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
    private static final float MIN_NUM_COUNTER_DIST = 200;
    private static final int MIN_DIST_OF_HEART = 135;
    private static final int TARGET_FRAME_RATE = 80;
    private static final int HEART_HEIGHT = 80;
    private static final int HEART_WIDTH = 80;
    private Renderable figureRend;
    private GameObject figureProfile;
    private GameObject figureName;
    private BossHealthBar dragonHealth;
    private static final int MAX_COINS = 4;
    private Counter coinCounter;
    private int prevSummon;
    private int numOfCoins;
    private WindowController windowController;
//    private Counter lifeCounter;
    private int prevLivesNum;
    private Vector2 avatarResetLocation;
    private SoundReader soundReader;
    private int seed;

    public PepseGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * Runs the entire simulation.
     *
     * @param args This argument should not be used.
     */
    public static void main(String[] args) {

        new PepseGameManager("Tommy's Adventure",
                new Vector2(1650,900)).run();
    }

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
        this.initialAvatarLocation = new Vector2(windowsDimensions.mult(0.5f));
        this.leftBound = (int) (-0.5*windowController.getWindowDimensions().x());
        this.rightBound = (int) (1.5*windowController.getWindowDimensions().x());
        this.random = new Random();
        this.coinCounter = new Counter(0);
        this.numOfCoins = 0;
        this.livesCounter = new Counter(numOfLives);
        this.prevSummon = 0;
        this.prevLivesNum = livesCounter.value();
        this.soundReader = soundReader;
        this.seed = new Random().nextInt(SEED_BOUND);

        Renderable coinsBagRend = imageReader.readImage("assets/CoinBag.png", true);
        this.coinsBag = new GameObject(new Vector2(windowsDimensions.x()*0.88f, windowsDimensions.y()*0.88f)
                ,new Vector2(COINS_BAG_SIZE,COINS_BAG_SIZE),coinsBagRend);
        gameObjects().addGameObject(coinsBag, Layer.FOREGROUND);
        coinsBag.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        Renderable figureRend = imageReader.readImage("assets/Tommy.png", false);
        this.figureProfile = new GameObject(new Vector2(70, 40)
                ,new Vector2(COINS_BAG_SIZE,COINS_BAG_SIZE),figureRend);
        figureProfile.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(figureProfile, Layer.FOREGROUND);

        Renderable figureNameRend = imageReader.readImage("assets/TommyName.png", true);
        this.figureName = new GameObject(new Vector2(190, 40)
                ,new Vector2(240,80),figureNameRend);
        figureName.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(figureName, Layer.FOREGROUND);

        Sky.create(gameObjects(), windowsDimensions, Layer.BACKGROUND);

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
        this.avatarResetLocation = new Vector2(windowsDimensions.x()/2,
                terrain.groundHeightAt(windowsDimensions.x()/2)-windowsDimensions.y()*0.4f);
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avatarResetLocation, inputListener, imageReader);
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

        // Creare Fire and Blast Maker
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
     * Checks if game should end. Logics for reset when the user loses life, creates prompt messages etc...
     */
    private void checkForGameEnd() {
        windowController.setPauseButton(KeyEvent.VK_P);
        windowController.setExitButton(KeyEvent.VK_ESCAPE);
        String prompt = "";
        if (livesCounter.value() != prevLivesNum) {
            avatar.setCenter(avatarResetLocation);
        }
        if ((this.coinCounter.value() == 5) ||
                inputListener.isKeyPressed(KeyEvent.VK_W)) {
            //we win
            prompt = "You win!";
        }
        if (this.livesCounter.value() == 0) {
            //we lost
            prompt = "You Lose!";
        }
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (livesCounter.value() == 0) {
                windowController.showMessageBox("You lost! Game will terminate");
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                Runnable task = () -> windowController.closeWindow();
                int delay = 5; // delay for 5 seconds
                executor.schedule(task, delay, TimeUnit.SECONDS);
            }
        }
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        checkForGameEnd();
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
