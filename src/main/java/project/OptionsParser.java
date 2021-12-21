package project;

public class OptionsParser {
    public static Direction[] parse(String[] args) throws IllegalArgumentException{
        Direction[] directions = new Direction[args.length];
        int count = 0;
        for (String arg : args) {
            switch (arg) {
                case "f", "forward" -> {
                    directions[count] = Direction.FORWARD;
                    count++;
                }
                case "b", "backward" -> {
                    directions[count] = Direction.BACKWARD;
                    count++;
                }
                case "r", "right" -> {
                    directions[count] = Direction.RIGHT;
                    count++;
                }
                case "l", "left" -> {
                    directions[count] = Direction.LEFT;
                    count++;
                }
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        return directions;
    }
}
