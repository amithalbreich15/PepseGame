package pepse.Bonus;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.Block;

import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.function.Function;

public class Dragon extends GameObject{
    private static final String[] fireFrames = { "assets/CharizardFire.png", "assets/CharizardFire2.png",
            "assets/CharizardFire3.png"};
    private static final String[] flyFrames = {"assets/Charizard.png",
            "assets/Charizard2.png", "assets/Charizard22.png", "assets/Charizard3.png", "assets/Charizard4.png",
            "assets/Charizard5.png", "assets/Charizard6.png"};
    private static final float DRAGON_SPEED = 220;
    private final AnimationRenderable dragonFlyAnimation;
    private final AnimationRenderable dragonFireAnimation;
    private static final double updateFrameTime = 1.5;

    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> groundHeightFunc;
    private final Random random;
    private final Vector2 windowDimensions;
    private static final int DRAGON_SIZE = 200;
    private final ImageReader imageReader;
    public static final int COIN_LAYER = Layer.STATIC_OBJECTS - 30;
    private final int dragonLayer;
    public static final String DRAGON_TAG = "Dragon";
    private final Avatar avatar;
    private UserInputListener inputListener;
    private static final Counter healthCounter = new Counter(10);
    private BossHealthBar dragonHealth;
    private static final String INITIAL_HEALTH_PATH = "assets/VIDA_10.png";
    private boolean isDragonActive;
    private Counter avatarLivesCounter;
    private Counter coinCounter;
    private int prevLivesNum;
    /**
     * Construct a new GameObject instance.
     *  @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
 *                      Note that (0,0) is the top-left corner of the window.
     * @param avatar
     */
    public Dragon(Vector2 topLeftCorner, Vector2 windowDimensions,
                  ImageReader imageReader, GameObjectCollection gameObjects, UserInputListener inputListener,
                  Function<Float, Float> groundHeightFunc, Random random, int dragonLayer, Avatar avatar,
                  boolean isDragonActive, Counter avatarLivesCounter, int prevLivesNum, Counter coinCounter) {
        super(topLeftCorner, new Vector2(DRAGON_SIZE, DRAGON_SIZE),
                new AnimationRenderable(flyFrames, imageReader, true, updateFrameTime));
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.gameObjects = gameObjects;
        this.inputListener = inputListener;
        this.groundHeightFunc = groundHeightFunc;
        this.random = random;
        this.dragonLayer = dragonLayer;
        this.dragonFlyAnimation = new AnimationRenderable(flyFrames, imageReader, true,
                0.1);
        this.dragonFireAnimation = new AnimationRenderable(fireFrames, imageReader, true,
                0.3);
        this.avatar = avatar;
        this.avatarLivesCounter = avatarLivesCounter;
        this.coinCounter = coinCounter;
        this.isDragonActive = isDragonActive;
        this.dragonHealth = new BossHealthBar(this.getCenter(),new Vector2(200, 20),
                    imageReader.readImage(INITIAL_HEALTH_PATH,false), healthCounter,avatar,imageReader);
        this.setTag(DRAGON_TAG);
        this.prevLivesNum = prevLivesNum;
    }


    /**
     * Should be called once per frame.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (PepseGameManager.isDragonActive) {
            gameObjects.addGameObject(dragonHealth,Layer.DEFAULT);
            dragonHealth.setCenter(new Vector2(this.getCenter().x(), this.getCenter().y()-200));
        }
        if (!PepseGameManager.isDragonActive){
            gameObjects.removeGameObject(dragonHealth,Layer.DEFAULT);
            gameObjects.removeGameObject(this,Layer.DEFAULT);
        }
        float dragonXvel = 0;
        float dragonYvel = 0;
        float windowCenterX = avatar.getCenter().x();
        float windowCenterY = avatar.getCenter().y();
        if(windowCenterX <= this.getCenter().x())
        {
            dragonXvel -= 1f;
            transform().setVelocityX(dragonXvel * DRAGON_SPEED);
            this.renderer().setRenderable(dragonFlyAnimation);
            if (!avatar.getVelocity().equals(Vector2.ZERO)){
                this.renderer().setIsFlippedHorizontally(false);
            }
        }
        if(windowCenterX > this.getCenter().x()){
            dragonXvel += 1f;
            transform().setVelocityX(dragonXvel * DRAGON_SPEED);
            this.renderer().setRenderable(dragonFlyAnimation);
            if (!avatar.getVelocity().equals(Vector2.ZERO)){
                this.renderer().setIsFlippedHorizontally(true);
            }
        }
        if(windowCenterY >= this.getCenter().y()){
            if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
                dragonYvel += DRAGON_SPEED;
                transform().setVelocityY(dragonYvel);
                this.renderer().setRenderable(dragonFireAnimation);
            }
            else {
                if (transform().getVelocity().y() == 0) {
                    dragonYvel += DRAGON_SPEED;
                    transform().setVelocityY(dragonYvel);
                    this.renderer().setRenderable(dragonFireAnimation);
                }
            }
        }
        if(windowCenterY <= this.getCenter().y()){
            if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
                dragonYvel -= DRAGON_SPEED;
                transform().setVelocityY(dragonYvel);
                this.renderer().setRenderable(dragonFlyAnimation);
            }
            else {
                if (transform().getVelocity().y() == 0) {
                    dragonYvel -= DRAGON_SPEED;
                    transform().setVelocityY(dragonYvel);
                    this.renderer().setRenderable(dragonFireAnimation);
                }
            }
        }

        if (transform().getVelocity().equals(Vector2.ZERO)){
            this.renderer().setRenderable(dragonFlyAnimation);
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Blast){
            healthCounter.decrement();
                if (healthCounter.value() == 0)
            {
                healthCounter.increaseBy(10);
                PepseGameManager.isDragonActive = false;
            }
        }
        if (other instanceof Block) {
            Vector2 newVelocity = this.getVelocity().flipped(collision.getNormal());
            this.setVelocity(newVelocity);
        }
        if (other instanceof Avatar) {
            prevLivesNum = avatarLivesCounter.value();
            avatarLivesCounter.decrement();
            if (coinCounter.value() > 0) {
                this.coinCounter.decrement();
            }
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (other instanceof Avatar) || (other instanceof Blast) || (other instanceof  Block);
    }
}

