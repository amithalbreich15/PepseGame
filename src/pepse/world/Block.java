package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.Function;

public class Block extends GameObject {
    public static final int SIZE = 30;

    /**
     * Constructor for Block Object.
     * @param topLeftCorner
     * @param renderable
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Rounds the Block to get its exact location.
     * @param num an int number.
     * @return round it based on Block size
     */
    public static int round(int num){
        return (num/Block.SIZE)*Block.SIZE;
    }

    public static Function<Integer,Integer> round = (num)-> (num/Block.SIZE);
}
