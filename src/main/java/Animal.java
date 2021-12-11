import java.util.ArrayList;
import java.util.List;

public class Animal implements IOverlappable{
    private Vector2d position;
    private MapDirection orientation;
    private IWorldMap map;
    private List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(Vector2d initialPosition, IWorldMap map) {
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        map.place(this);
    }

    @Override
    public boolean canBeOverlapped() {
        return false;
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        for(IPositionChangeObserver observer1 : observers){
            if(observer1.equals(observer)) observers.remove(observer1);
        }
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(oldPosition, newPosition);
        }
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return switch (orientation) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case EAST -> "E";
            case WEST -> "W";
        };
    }

    public void move(MoveDirection direction, IWorldMap map) {
        if (direction.equals(MoveDirection.LEFT)) {
            orientation = switch (orientation) {
                case NORTH -> MapDirection.WEST;
                case SOUTH -> MapDirection.EAST;
                case WEST -> MapDirection.SOUTH;
                case EAST -> MapDirection.NORTH;
            };
        } else if (direction.equals(MoveDirection.RIGHT)) {
            orientation = switch (orientation) {
                case NORTH -> MapDirection.EAST;
                case SOUTH -> MapDirection.WEST;
                case WEST -> MapDirection.NORTH;
                case EAST -> MapDirection.SOUTH;
            };
        } else {
            Vector2d movementVector = switch (orientation) {
                case NORTH -> new Vector2d(0, 1);
                case SOUTH -> new Vector2d(0, -1);
                case WEST -> new Vector2d(-1, 0);
                case EAST -> new Vector2d(1, 0);
            };

            if (direction.equals(MoveDirection.FORWARD) && map.canMoveTo(position.add(movementVector))) {
                this.positionChanged(position, position.add(movementVector));
                position = position.add(movementVector);
            } else if (direction.equals(MoveDirection.BACKWARD) && map.canMoveTo(position.subtract(movementVector))) {
                this.positionChanged(position, position.subtract(movementVector));
                position = position.subtract(movementVector);
            }
        }
    }
}