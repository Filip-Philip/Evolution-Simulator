import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected ArrayList<Animal> animals = new ArrayList<>();
    protected Map<Vector2d, Animal> animalsHashMap = new HashMap<>();

    @Override
    public String toString() {
        Vector2d bottomLeftMap = getMapBottomLeftCorner();
        Vector2d topRightMap = getMapTopRightCorner();
        MapVisualiser mapVisualisation = new MapVisualiser(this);
        return mapVisualisation.draw(bottomLeftMap, topRightMap);
    }

    public abstract Vector2d getMapTopRightCorner();

    public abstract Vector2d getMapBottomLeftCorner();

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        Animal animal = animalsHashMap.get(oldPosition);
        animalsHashMap.remove(oldPosition);
        animalsHashMap.put(newPosition, animal);
    }

    @Override
    public boolean place(Animal animal) {
        if (animalsHashMap.containsKey(animal.getPosition())) {
            throw new IllegalArgumentException(animal.getPosition().toString() + " is already occupied");
        }
        else{
            animals.add(animal);
            animalsHashMap.put(animal.getPosition(), animal);
            return true;
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalsHashMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return animalsHashMap.get(position);
    }

}
