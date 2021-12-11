public class OptionsParser {
    public static MoveDirection[] parse(String[] args) throws IllegalArgumentException{
        MoveDirection[] directions = new MoveDirection[args.length];
        int count = 0;
        for (String arg : args) {
            switch (arg) {
                case "f", "forward" -> {
                    directions[count] = MoveDirection.FORWARD;
                    count++;
                }
                case "b", "backward" -> {
                    directions[count] = MoveDirection.BACKWARD;
                    count++;
                }
                case "r", "right" -> {
                    directions[count] = MoveDirection.RIGHT;
                    count++;
                }
                case "l", "left" -> {
                    directions[count] = MoveDirection.LEFT;
                    count++;
                }
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        return directions;
    }
}
