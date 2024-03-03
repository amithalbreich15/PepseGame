package pepse.Bonus;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;

/**
 * PuckStrategy cass- kind of CollisionStrategy and inherits from this class. When this strategy is activated, when
 * shattering a brick with this kind of strategy 3 Puck Objects will come out from the middle of the brick in different
 * Diagonals in 45 degrees.
 */
public class Blast extends GameObject {

    private static final float BLAST_SPEED = 100;
    //    private final GameObjectCollection gameObjectCollection;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
//    private final AnimationRenderable blastAnimation;
    private static final double updateFrameTime = 0.5;
    private static final String blastPath = "assets/FireBlasts.png";
    //    private final WindowController windowController;
    private final GameObjectCollection gameObjects;
//    private final Function<Float, Float> groundHeightFunc;
    private final Avatar avatar;
//    private final Vector2 windowDimensions;
    private static final int BLAST_SIZE = 40;
    public static final int COIN_LAYER = Layer.STATIC_OBJECTS - 30;
    private final int blastLayer;
    public static final String BLAST_TAG = "Blast";
    private static final Counter blastCounter = new Counter(0);
    private float angle;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Blast(Vector2 topLeftCorner, Vector2 dimensions
                 ,GameObjectCollection gameObjects,ImageReader imageReader, SoundReader soundReader, int blastLayer,
                 Avatar avatar, float angle) {
        super(topLeftCorner, dimensions, imageReader.readImage(blastPath,true));
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.angle = angle;
        this.blastLayer = blastLayer;
        this.avatar = avatar;

    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Dragon || other instanceof Block){
            gameObjects.removeGameObject(this);
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (other instanceof Dragon) || (other instanceof Block);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.setVelocity(Vector2.RIGHT.rotated(angle).mult(BLAST_SPEED));
        this.renderer().setRenderableAngle(angle);
    }
}

