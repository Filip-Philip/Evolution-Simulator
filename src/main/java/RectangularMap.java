public class RectangularMap extends AbstractWorldMap{
    private final Vector2d MAP_BOTTOM_LEFT_CORNER = new Vector2d(0,0);
    private final Vector2d mapTopRightCorner;

    public RectangularMap(int width, int height) {
        this.mapTopRightCorner = new Vector2d(width, height);
    }

    @Override
    public Vector2d getMapTopRightCorner(){
        return mapTopRightCorner;
    }

    @Override
    public Vector2d getMapBottomLeftCorner(){
        return MAP_BOTTOM_LEFT_CORNER;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) && position.precedes(mapTopRightCorner)
                && position.follows(MAP_BOTTOM_LEFT_CORNER);
    }
}
