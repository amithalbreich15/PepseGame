package pepse.Bonus;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.Color;

/**
 * CoinNumericCounter class - Visual graphic number represents coin counter.
 */
public class CoinNumericCounter extends GameObject {
    private final Counter coinCounter;
    private final TextRenderable renderable;
    private final Avatar avatar;
    /**
     * Construct a new CoinLifeCounter instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public CoinNumericCounter(Counter coinsCounter,Vector2 topLeftCorner,
                              Vector2 dimensions,Avatar avatar) {
        super(topLeftCorner,dimensions,null);
        TextRenderable textRenderable = new TextRenderable("0");
        this.renderer().setRenderable(textRenderable);
        this.renderable = textRenderable;
        this.coinCounter = coinsCounter;
        this.avatar = avatar;
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Updates the CoinNumericCounter counter on screen.
     * @param deltaTime - Game clock.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (coinCounter.value() >= 0) {
            String numString = String.valueOf(coinCounter.value());
            this.renderable.setString(numString);
        }
        this.renderable.setColor(Color.yellow);
    }

    /**
     * Decides what should be done
     * @param other
     * @param collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Avatar)
        {
            coinCounter.increment();
        }
    }
}

