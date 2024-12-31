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
 * Class DragonFire - represents the Fire of the Dragon shots.
 */
public class DragonFire extends GameObject {

    private static final int ANGLES_NUM = 7;
    private static final String fireFrames = "assets/fireblast1.png";
    private final GameObjectCollection gameObjects;
    public static final String FIRE_TAG = "Fire";
    private int angle;
    private Dragon dragon;
    private Counter avatarLivesCounter;
    private Random random;
    private  Vector2 windowsDimensions;

    /**
     * Construct a new DragonFire instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public DragonFire(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable
            , GameObjectCollection gameObjects, Dragon dragon, Counter avatarLivesCounter,
                      Vector2 windowsDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.dragon = dragon;
        this.avatarLivesCounter = avatarLivesCounter;
        this.random = new Random();
        this.angle = random.nextInt(ANGLES_NUM);;
        this.windowsDimensions = windowsDimensions;
        this.setTag(FIRE_TAG);
    }

    /**
     * Decide what to do on Collisions with Avatar or Blocks
     * Will disappear if hits the Blocks
     * will decrease CoinCounter and LifeCounter if hits the the Avatar
     * @param other other GameObject
     * @param collision Collision instance.
     */
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

    /**
     * Updates the Dragon Fire Object on screen - sets it in a random angle.
     * @param deltaTime Update frame time.
     */
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