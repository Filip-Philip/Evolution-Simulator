package project;

import project.AbstractWorldMap;
import project.Vector2d;

import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;

public class WrappedRectangularMap extends AbstractWorldMap {

    public WrappedRectangularMap(int width, int height, double jungleRatio, int startNumberOfPlants,
                                 int plantsNutritionalValue, Random random) {
        super(width, height, jungleRatio, startNumberOfPlants, plantsNutritionalValue, random);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }
}
