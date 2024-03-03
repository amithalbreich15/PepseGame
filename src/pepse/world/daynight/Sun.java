package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String SUN_TAG = "sun";
    private static final int SUN_SIZE = 150;
    private static final Vector2 sunVector = new Vector2(SUN_SIZE, SUN_SIZE);
    private static Sun instance;
    private static GameObject sun;


    private Sun(){}

    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        if(instance == null){
            instance = new Sun();
        }
        else {
            return sun;
        }
        sun = new GameObject( Vector2.ZERO,
                sunVector, new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN_TAG);
        float pi =(float) Math.PI;
        Vector2 centerVector = new Vector2(windowDimensions.x()/2 , windowDimensions.y() / 2);
        Vector2 sunlocation = new Vector2(0, windowDimensions.y() / 2);
        new Transition<Float>(sun,
                angle -> sun.setCenter(centerVector.add(centerVector.multX((float) Math.cos(angle)).multY((float) Math.sin(-angle))))
                ,pi/2,
                pi*2+ pi/2,Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }
}
