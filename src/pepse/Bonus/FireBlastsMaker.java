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
    private final int blastLayer;
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

    /**
     * Update the Fire and Blasts location and logics within time frame.
     * @param deltaTime delta Time.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        createBlast();
        if (blastSize == MAX_SHOTS){
            for (int i = 0; i < MAX_SHOTS; i++) {
                gameObjects.removeGameObject(blastsArray[i], blastLayer);
            }
            fireSize = 0;
            blastSize = 0;
            blastCounter.reset();
        }
    }

    /**
     * This function is responsible for creating new instances of the "Blast" and "DragonFire" classes when certain key
     * presses are detected by the input listener.
     * The function listens for key presses of the up and down arrow keys to increment or decrement the angle variable.
     * Then, the function listens for a key press of the control key. If the control key is pressed and the blast
     * counter is less than or equal to the maximum allowed shots, the function creates new instances of the "Blast"
     * and "DragonFire" classes with specific properties and positions. These new instances are then added to the
     * collection of game objects. The function also increments the blast counter each time a new instance of "Blast"
     * is created.
     */
    public void createBlast() {
        if (inputListener.isKeyPressed(KeyEvent.VK_UP))
        {
            angle += 1;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_DOWN))
        {
            angle -= 1;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_CONTROL) && blastCounter.value() <= MAX_SHOTS) {
            if (PepseGameManager.isDragonActive){
                DragonFire dragonFire = new DragonFire(dragon.getCenter(),new Vector2(BLAST_SIZE, BLAST_SIZE),
                        fireAnimation, gameObjects, dragon,
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

