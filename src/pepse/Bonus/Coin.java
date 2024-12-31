package pepse.Bonus;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.function.Function;

/**
 * Coin Class - Coins are collected by the Avatar when he collides them and.
 */
public class Coin extends GameObject {

    private final Vector2 windowDimensions;
    private static final int COIN_SIZE = 70;
    private final int coinLayer;
    private Counter coinCounter;

    /**
     * Construct a new Coin instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Coin(Vector2 topLeftCorner, Vector2 windowDimensions, Renderable renderable,int coinLayer, Counter coinCounter) {
        super(topLeftCorner, new Vector2(COIN_SIZE, COIN_SIZE), renderable);
        this.windowDimensions = windowDimensions;
        this.coinLayer = coinLayer;
        this.coinCounter = coinCounter;
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Avatar) {
            coinCounter.increment();
        }
    }
}
