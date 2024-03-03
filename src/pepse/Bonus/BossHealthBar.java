package pepse.Bonus;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.Color;

/**
 * NumericLifeCounter class - Visual graphic number represents life counter - changing colors accordingly.
 * Also switching colors when life is reducing/increasing:
 *      * Green - 3  or 4 lives remaining
 *      * Yellow - 2 lives remaining
 *      * Red - 1 lives remaining
 */
public class BossHealthBar extends GameObject {
    private static final String[] HEALTH_BARS_PATHS = {"assets/VIDA_10.png", "assets/VIDA_9.png",
            "assets/VIDA_8.png", "assets/VIDA_7.png","assets/VIDA_6.png", "assets/VIDA_5.png","assets/VIDA_4.png",
            "assets/VIDA_3.png","assets/VIDA_2.png", "assets/VIDA_1.png", "assets/VIDA_0.png"};
    private Counter lifeCounter;
    private Renderable renderable;
    private ImageReader imageReader;
    private final Avatar avatar;
    /**
     * Construct a new NumericLifeCounter instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameObjectCollection      The total Game Objects collection
     */
    public BossHealthBar(Vector2 topLeftCorner, Vector2 dimensions , Renderable renderable, Counter healthCounter,
                         Avatar avatar, ImageReader imageReader) {
        super(topLeftCorner,dimensions,renderable);
        this.imageReader = imageReader;
        this.renderable = imageReader.readImage(HEALTH_BARS_PATHS[0],false );
        this.lifeCounter = healthCounter;
        this.avatar = avatar;
    }

    /**
     * Increments livesCounter by 1 at a time;
     * @return livesCounter value increased by 1.
     */
    public void incrementCoinsCounter() { lifeCounter.increment(); }

    /**
     * Updates the NumericLifeCounter counter on board.
     * Counts down according to current lifeCount remaining for the user to play.
     * Also switching colors when life is reducing:
     * Green - 3 or 4 lives remaining
     * Yellow - 2 lives remaining
     * Red - 1 lives remaining
     * @param deltaTime - Game clock.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int lifeCount = lifeCounter.value();
        switch (lifeCount) {
            case 10:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[1],false ));
                break;
            case 9:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[2],false ));
                break;
            case 8:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[3],false ));
                break;
            case 7:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[4],false ));
                break;
            case 6:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[5],false ));
                break;
            case 5:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[6],false ));
                break;
            case 4:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[7],false ));
                break;
            case 3:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[8],false ));
                break;
            case 2:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[9],false ));
                break;
            case 1:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[10],false ));
                break;
            case 0:
                this.renderer().setRenderable(null);
                break;
            default:
                this.renderer().setRenderable(imageReader.readImage(HEALTH_BARS_PATHS[1],false ));
                break;
        }
    }
}

