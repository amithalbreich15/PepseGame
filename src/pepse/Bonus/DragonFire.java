package pepse.Bonus;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.Block;

import java.util.Random;

/**
 * PuckStrategy cass- kind of CollisionStrategy and inherits from this class. When this strategy is activated, when
 * shattering a brick with this kind of strategy 3 Puck Objects will come out from the middle of the brick in different
 * Diagonals in 45 degrees.
 */
public class DragonFire extends GameObject {

    private static final float BLAST_SPEED = 140;
    private static final int ANGLES_NUM = 7;
    //    private final GameObjectCollection gameObjectCollection;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private static final String fireFrames = "assets/fireblast1.png";
    private static final double updateFrameTime = 0.5;
    private final GameObjectCollection gameObjects;
    private final Avatar avatar;
    private final int blastLayer;
    public static final String FIRE_TAG = "Fire";
    private int angle;
    private Dragon dragon;

    private Counter avatarLivesCounter;
    private Random random;
    private  Vector2 windowsDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public DragonFire(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable
            , GameObjectCollection gameObjects, ImageReader imageReader, SoundReader soundReader, int blastLayer,
                 Avatar avatar, Dragon dragon, Counter avatarLivesCounter, Vector2 windowsDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.blastLayer = blastLayer;
        this.avatar = avatar;
        this.dragon = dragon;
        this.avatarLivesCounter = avatarLivesCounter;
        this.random = new Random();
        this.angle = random.nextInt(ANGLES_NUM);;
        this.windowsDimensions = windowsDimensions;
        this.setTag(FIRE_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Block) {
            gameObjects.removeGameObject(this);
        }
        if (other instanceof Avatar) {
            gameObjects.removeGameObject(this);
            avatarLivesCounter.decrement();
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (other instanceof Avatar) || (other instanceof Block);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!PepseGameManager.isDragonActive) {
            gameObjects.removeGameObject(this);
        }
        switch (angle) {
            case 0:
                this.transform().setVelocity(new Vector2(1,-1).mult(100f));
                this.renderer().setRenderableAngle(225);
                break;
            case 1:
                this.transform().setVelocity(new Vector2(1,1).mult(100f));
                this.renderer().setRenderableAngle(-45);
                break;
            case 2:
                this.transform().setVelocity(new Vector2(-1,-1).mult(100f));
                this.renderer().setRenderableAngle(135);
                break;
            case 3:
                this.transform().setVelocity(new Vector2(1,-1).mult(100f));
                this.renderer().setRenderableAngle(45);
                break;
            case 4:
                this.transform().setVelocity(Vector2.DOWN.mult(100f));
                this.renderer().setRenderableAngle(270);
                break;
            case 5:
                this.transform().setVelocity(Vector2.DOWN.mult(100f));
                this.renderer().setRenderableAngle(270);
                break;
            case 6:
                this.transform().setVelocity(Vector2.RIGHT.mult(100f));
                break;
            case 7:
                this.transform().setVelocity(Vector2.LEFT.mult(100f));
                this.renderer().setRenderableAngle(180);
                break;

        }
    }
}