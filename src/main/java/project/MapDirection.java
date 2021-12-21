package project;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    @Override
    public String toString() {
        return switch (this){
            case NORTH -> "N";
            case NORTH_WEST -> "NW";
            case NORTH_EAST -> "NE";
            case SOUTH -> "S";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case EAST -> "E";
        };
    }

    public MapDirection next(){
        return switch (this){
            case NORTH -> NORTH_EAST;
            case NORTH_WEST -> NORTH;
            case NORTH_EAST -> EAST;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case EAST -> SOUTH_EAST;
        };
    }

    public MapDirection previous(){
        return switch (this){
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case NORTH_EAST -> NORTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case SOUTH_WEST -> SOUTH;
            case WEST -> SOUTH_WEST;
            case EAST -> NORTH_EAST;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1,1);
            case NORTH_WEST -> new Vector2d(-1,1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case EAST -> new Vector2d(1, 0);
        };
    }
}
