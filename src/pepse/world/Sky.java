package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Class Sky - represents blue sky in the Game.
 */
public class Sky {

    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static final String SKY = "sky";
    private static Sky instance;
    private static GameObject sky;


    /**
     * Construct a new Sky instance.
     */
    private Sky(){}

    /**
     * Create Sky static function - creates the Sky Renderable.
     * @param gameObjects Game Object Collection of the game.
     * @param windowDimensions Window Dimensions
     * @param skyLayer sky Layer.
     * @return a GameObject represents the Sky - Rectangular shape Sky cover the window.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {
        if (instance == null){
            instance = new Sky();
        }
        else {
            return sky;
        }
        sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(SKY);
        return sky;
    }
}
