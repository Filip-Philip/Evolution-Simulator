package project;

import java.util.Random;

public class World {
    public static void main(String[] args){
        try {
            Random random = new Random();
            AbstractWorldMap map = new WrappedRectangularMap(100, 50, 0.5, 50
                    ,5, random);
            IEngine engine = new SimulationEngine(map, 100, 2, 150);
//            map.printIfEqual();
            engine.run();
            System.out.println(map);
//            map.printIfEqual();
        }
        catch (IllegalArgumentException illegalArgument){
            System.out.println(illegalArgument.getMessage());
        }
        catch (RuntimeException runtimeException){
            System.out.println(runtimeException.getMessage());
        }
    }
}
