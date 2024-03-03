package pepse.Bonus;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;

import java.awt.event.KeyEvent;

public class FireBlastsMaker extends GameObject{

    private static final String[] fireFrames = {"assets/fireblast1.png", "assets/fireblast2.png"};
    private static final int MAX_SHOTS = 8;
    private final AnimationRenderable fireAnimation;
    private static final double updateFrameTime = 0.5;
    private static final String staticCoinPath = "assets/FireBlasts.png";
    private final GameObjectCollection gameObjects;
    private final Avatar avatar;
    private final Vector2 windowDimensions;
    private static final int BLAST_SIZE = 40;
    private final ImageReader imageReader;
    public static final int COIN_LAYER = Layer.STATIC_OBJECTS - 30;
    private final int blastLayer;
    public static final String BLAST_TAG = "Blast";
    private UserInputListener inputListener;
    private SoundReader soundReader;
    private float angle;
    private Dragon dragon;
    private boolean isDragonActive;
    private Counter blastCounter;
    private Counter fireCounter;
    private Blast[] blastsArray;
    private DragonFire[] dragonFiresArray;
    private int blastSize;
    private int fireSize;
    private Counter avatarLivesCounter;



    /**
     * Construct a new GameObject instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public FireBlastsMaker(Vector2 topLeftCorner, Vector2 dimensions, Vector2 windowDimensions,
                     ImageReader imageReader, SoundReader soundReader, GameObjectCollection gameObjects,
                           UserInputListener inputListener, int blastLayer, Avatar avatar, Dragon dragon, float angle,
                           Counter avatarLivesCounter) {
        super(topLeftCorner, dimensions,null);
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.gameObjects = gameObjects;
        this.inputListener = inputListener;
        this.angle = angle;
        this.blastLayer = blastLayer;
        this.fireAnimation = new AnimationRenderable(fireFrames, imageReader, true, updateFrameTime);
        this.avatar = avatar;
        this.dragon = dragon;
        this.isDragonActive = PepseGameManager.isDragonActive;
        this.blastCounter = new Counter(0);
        this.fireCounter = new Counter(0);
        this.blastSize = 0;
        this.fireSize = 0;
        this.blastsArray = new Blast[MAX_SHOTS];
        this.dragonFiresArray = new DragonFire[MAX_SHOTS];
        this.avatarLivesCounter = avatarLivesCounter;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        createBlast();
        if (blastSize == MAX_SHOTS){
            for (int i = 0; i < MAX_SHOTS; i++) {
                gameObjects.removeGameObject(blastsArray[i], blastLayer);
//                gameObjects.removeGameObject(dragonFiresArray[i],blastLayer);
            }
            fireSize = 0;
            blastSize = 0;
            blastCounter.reset();
        }
    }

    public void createBlast() {
        if (inputListener.isKeyPressed(KeyEvent.VK_UP))
        {
            angle += 5;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_DOWN))
        {
            angle -= 5;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_CONTROL) && blastCounter.value() <= MAX_SHOTS) {
            if (PepseGameManager.isDragonActive){
                DragonFire dragonFire = new DragonFire(dragon.getCenter(),new Vector2(BLAST_SIZE, BLAST_SIZE),
                        fireAnimation, gameObjects, imageReader, soundReader,Layer.DEFAULT,avatar, dragon,
                        avatarLivesCounter, windowDimensions);
                gameObjects.addGameObject(dragonFire, Layer.DEFAULT);
                dragonFiresArray[fireSize++] = dragonFire;
            }
            Blast blast = new Blast(avatar.getCenter(),new Vector2(BLAST_SIZE, BLAST_SIZE),gameObjects,
                    imageReader, soundReader,Layer.DEFAULT,avatar, angle);
            blast.setAngle(angle);
            blast.transform().setAccelerationY(500f);
            gameObjects.addGameObject(blast, Layer.DEFAULT);
            blastsArray[blastSize++] = blast;
            blastCounter.increment();
        }
    }
}

