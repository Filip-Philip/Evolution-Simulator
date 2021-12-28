package project;

import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;

public class BoundedRectangularMap extends AbstractWorldMap {

    public BoundedRectangularMap(int width, int height, double jungleRatio, int startNumberOfPlants, int plantsNutritionalValue,
                                 Random random){
        super(width, height, jungleRatio, startNumberOfPlants, plantsNutritionalValue, random);
    }

    @Override
    public MapSwitch getMapType() {
        return MapSwitch.BOUNDED_RECTANGULAR_MAP;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return isOutsideTheMap(position);
    }
}
