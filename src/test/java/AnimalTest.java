import org.junit.jupiter.api.Test;
import project.*;

import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    @Test
    void orientationTest() {
        Random random = new Random();
        AbstractWorldMap map = new BoundedRectangularMap(4,4, 0.5, 3,
                5, random);
        random.setSeed(0);
        Animal animal = new Animal(new Vector2d(2,2), 10, 2, map, random);

        animal.move(3);
        assertEquals(animal.toString(), "(N, (2, 2))");
        animal.move(5);
        assertEquals(animal.toString(), "(SW, (2, 2))");
    }

    @Test
    void positionTest() {
        Random random = new Random();
        AbstractWorldMap map = new WrappedRectangularMap(4,4, 0.5, 3,
                5, random);
        random.setSeed(0);
        Animal animal = new Animal(new Vector2d(2,2), 10, 2, map, random);
        animal.move(0);
        assertEquals(animal.toString(), "(SW, (1, 1))");
        animal.move(4);
        assertEquals(animal.toString(), "(SW, (2, 2))");
        animal.move(0);
        animal.move(0);
        animal.move(0);
        assertEquals(animal.toString(), "(SW, (4, 4))");
        animal.move(5);
        animal.move(0);
        assertEquals(animal.toString(), "(E, (0, 4))");
    }
}
