package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String SUN_HALO_TAG = "sunHalo";
    private static SunHalo instance;
    private static GameObject sunHalo;


    private SunHalo(){}

    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {
        if(instance == null){
            instance = new SunHalo();
        }
        else {
            return sunHalo;
        }
        sunHalo = new GameObject( Vector2.ZERO,
                sun.getDimensions().mult(1.6f), new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}
