package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Class of Night Object.
 */
public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String SHADE_TAG = "shade";
    private static Night instance;
    private static GameObject shade;

    /**
     * Constructor for Night.
     */
    private Night(){}

    /**
     * Night create function - shades the screen with scheduled Task.
     * @param gameObjects
     * @param layer Night's Layer
     * @param windowDimensions
     * @param cycleLength
     * @return
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        if(instance == null){
            instance = new Night();
        }
        else {
            return shade;
        }
        shade = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Color.BLACK));
        shade.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(shade, layer);
        shade.setTag(SHADE_TAG);
        new Transition<Float>(shade, shade.renderer()::setOpaqueness,
                0f,MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength/2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return shade;

    }
}
