package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

public class Leaf extends GameObject {

    public static final String LEAF = "Leaf";
    private static final float INTIAL_ANGLE = 4f;
    private static final String BOTTOM_GROUND = "bottomGround";
    private static final float DOWN_VELOCITY = 50f;
    private static final float FADE_OUT_TIME = 8;
    private final Random rand;
    private final float cycletime;
    private static final Color LEAF_COLOR = new Color(50,200,30, 100);
    private final float reGenerateTime;
    float timepassed;
    private final Vector2 initialTopLeft;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner,dimensions, renderable);
        this.initialTopLeft = topLeftCorner;
        rand = new Random();
        this.cycletime = rand.nextInt(200)/10 + 1;
        this.reGenerateTime = rand.nextInt(200)/10 + 1;
        this.timepassed = 0;
        leafCreation();
        leafDisappear();
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
        timepassed+=deltaTime;
        if(timepassed>= 2*cycletime+ 7 + reGenerateTime){
            this.renderer().fadeIn(0);
            this.setTopLeftCorner(initialTopLeft);
            leafCreation();
            timepassed =0;
        }
    }

    private void leafCreation(){
        new ScheduledTask(this, cycletime,
                false, this::setLeafRend);
    }
    private void setLeafRend(){

        new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                INTIAL_ANGLE, -INTIAL_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT,
                1, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        Vector2 temp = getDimensions();
        new Transition<Vector2>(this,
                this::setDimensions,
                temp.mult(0.9f),temp.mult(1.05f), Transition.CUBIC_INTERPOLATOR_VECTOR,4,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }
    private void leafDisappear(){
        new ScheduledTask(this, 2*cycletime, false, this::leafFall);



    }

    private void leafFall() {
        this.setVelocity(Vector2.DOWN.mult(DOWN_VELOCITY));
        this.renderer().fadeOut(FADE_OUT_TIME);

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
            this.setVelocity(Vector2.ZERO);

        }
    }

}