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
 * BossHealthBar class - Visual graphic Bar represents Boss current health:
 * changing colors accordingly and decreases bar.
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
     * Construct a new BossHealthBar instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
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
     * Updates the BossHealthBar status on screen.
     * @param deltaTime - Game clock.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int lifeCount = lifeCounter.value();
        switch (lifeCount) {
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

