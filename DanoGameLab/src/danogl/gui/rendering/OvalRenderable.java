package danogl.gui.rendering;

import danogl.util.Vector2;
import java.awt.*;

/**
 * An oval Renderable. Note that only the color is supplied in the
 * constructor; the dimensions and position are supplied as usual in
 * every call to {@link #render}.
 * @author Dan Nirel
 */
public class OvalRenderable extends ShapeRenderable {
    /**
     * Create an OvalRenderable in a given color. The exact shape will be determined
     * by the GameObject with this Renderable.
     */
    public OvalRenderable(Color color) {
        super(color);
    }

    @Override
    protected void callGraphicsMethod(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions) {
        g.fillOval(
                (int)topLeftCorner.x()/2*2,
                (int)topLeftCorner.y()/2*2,
                (int)dimensions.x(),
                (int)dimensions.y());
    }
}
