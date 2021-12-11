public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    @Override
    public String toString() {
        return switch (this){
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case WEST -> "Zachód";
            case EAST -> "Wschód";
            default -> "Błędny kierunek";
        };
    }

    public MapDirection next(){
        return switch (this){
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
        };
    }

    public MapDirection previous(){
        return switch (this){
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector(){
        Vector2d unitVector = switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
            case EAST -> new Vector2d(1, 0);
        };
        return unitVector;
    }

    public static void main(String[] args){
        MapDirection direction1 = NORTH;
        MapDirection direction2 = WEST;
        System.out.println(direction2.toUnitVector());
    }
}
