package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.Bonus.CoinMaker;
import pepse.Bonus.Dragon;
import pepse.Bonus.DragonFire;

import java.awt.event.KeyEvent;

import static pepse.world.trees.Trunk.TRUNK_TAG;

public class Avatar extends GameObject {

    private static final float AVATAR_SPEED = 300f;
    private static Avatar instance;
    private static final float WIDTH = 3*Block.SIZE;
    private static final float HEIGHT = 3*Block.SIZE;
    private static float energyLevel = 0;
    private static final float INITIAL_ENERGY = 100 ;
    private static final double ENERGY_FACTOR = 0.5;
    private static final String[] staticAvatarPath = {"assets/TommyStanding.png", "assets/TommyStanding3.png",
            "assets/TommyStanding2.png", "assets/TommyStanding4.png", "assets/TommyResting.png",
            "assets/TommyResting2.png", "assets/TommyStanding5.png",
            "assets/TommyStanding6.png", "assets/TommyResting.png"};
    private static final String[] avatarWalkFrames = {"assets/TommyWalking.png","assets/TommyWalking2.png",
            "assets/TommyWalking3.png","assets/TommyWalking4.png", "assets/TommyWalking5.png",
            "assets/TommyWalking6.png","assets/TommyWalking7.png", "assets/TommyWalking8.png",
            "assets/TommyWalking9.png"};
    private static final double updateFrameTime = 0.3;
    private static final String[] avatarJumpFrames = {"assets/TommyFly.png", "assets/TommyFly1.png",
            "assets/TommyFly2.png", "assets/TommyFly3.png", "assets/TommyFly4.png", "assets/TommyFly5.png"};
    private static final String[] avatarFlyFrames = {"assets/TommyJumping2.png", "assets/TommyJumping1.png"
            ,"assets/TommyJumping.png", "assets/TommyJumping3.png"};
    private final UserInputListener inputListener;
    private final AnimationRenderable animationWalk;
    private final AnimationRenderable animationJump;
    private final AnimationRenderable animationFly;
    private final AnimationRenderable animationRest;
    private boolean shouldIncreaseEnergy;
    private final GameObjectCollection gameObjects;
    private Counter coinCounter;
    private GraphicLifeCounter graphicLifeCounter;
    private Counter livesCounter;
    private int layer;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,UserInputListener inputListener,
                   ImageReader imageReader) {
        super(topLeftCorner, new Vector2(WIDTH , HEIGHT),
                new AnimationRenderable(staticAvatarPath, imageReader, true, updateFrameTime));
        this.inputListener = inputListener;
        energyLevel = INITIAL_ENERGY;
        this.shouldIncreaseEnergy = false;
        this.gameObjects = gameObjects;
        this.animationRest = new AnimationRenderable(staticAvatarPath, imageReader, true, 0.4);
        this.animationWalk =
                new AnimationRenderable(avatarWalkFrames, imageReader, true, updateFrameTime);
        this.animationJump =
                new AnimationRenderable(avatarJumpFrames, imageReader, true, 0.2);
        this.animationFly =
                new AnimationRenderable(avatarFlyFrames, imageReader, true, 0.1);
    }


    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        if(instance == null){
            instance = new Avatar(gameObjects, layer,topLeftCorner,inputListener,imageReader);
            gameObjects.addGameObject(instance, layer);
            instance.transform().setAccelerationY(500f);
            instance.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        }
        return instance;
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
        float avatar_X_vel = 0;
        float avatar_Y_vel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
        {
            avatar_X_vel += 1f;
            this.renderer().setRenderable(animationWalk);
            this.renderer().setIsFlippedHorizontally(false);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            avatar_X_vel -= 1f;
            this.renderer().setRenderable(animationWalk);
            this.renderer().setIsFlippedHorizontally(true);
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE)){
            if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energyLevel > 0) {
                energyLevel -= ENERGY_FACTOR;
                avatar_Y_vel -= AVATAR_SPEED;
                transform().setVelocityY(avatar_Y_vel);
                this.renderer().setRenderable(animationFly);
            }
            else {
                if (transform().getVelocity().y() == 0) {
                    avatar_Y_vel -= AVATAR_SPEED;
                    transform().setVelocityY(avatar_Y_vel);
                    this.renderer().setRenderable(animationJump);
                }
            }
        }
        transform().setVelocityX(avatar_X_vel*AVATAR_SPEED);
        if (transform().getVelocity().equals(Vector2.ZERO)){
            this.renderer().setRenderable(animationRest);
            energyLevel = (energyLevel <=INITIAL_ENERGY)? (float) (energyLevel + ENERGY_FACTOR) : energyLevel;
        }
    }

    /**
     * Called on the first frame of a collision.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Terrain.TOP_FLOOR_TAG)){
            instance.transform().setVelocity(Vector2.ZERO);
        }
        if (other.getTag().equals(Terrain.BOTTOM_FLOOR_TAG)) {
            instance.transform().setVelocity(Vector2.ZERO);
        }
        if (other.getTag().equals(CoinMaker.COIN_TAG)){
            gameObjects.removeGameObject(other, CoinMaker.COIN_LAYER);
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return ((other.getTag().equals(Terrain.TOP_FLOOR_TAG) ||
                        ((other.getTag().equals(TRUNK_TAG))) || (other.getTag().equals(Terrain.BOTTOM_FLOOR_TAG))
                && (other instanceof  Block)))
                || other.getTag().equals(CoinMaker.COIN_TAG) || (other instanceof Dragon) ||
                (other instanceof DragonFire);
    }


    /**
     * This method returns the current energy
     * @return the current energy
     */
    public static float getEnergy() {
        return energyLevel;
    }

}