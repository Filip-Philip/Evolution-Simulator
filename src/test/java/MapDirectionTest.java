import org.junit.jupiter.api.Test;
import project.MapDirection;
import project.Vector2d;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    void nextTest() {
        assertEquals(MapDirection.NORTH.next(), MapDirection.NORTH_EAST);
        assertEquals(MapDirection.SOUTH.next(), MapDirection.SOUTH_WEST);
        assertEquals(MapDirection.WEST.next(), MapDirection.NORTH_WEST);
        assertEquals(MapDirection.EAST.next(), MapDirection.SOUTH_EAST);
        assertEquals(MapDirection.NORTH_EAST.next(), MapDirection.EAST);
        assertEquals(MapDirection.NORTH_WEST.next(), MapDirection.NORTH);
        assertEquals(MapDirection.SOUTH_EAST.next(), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH_WEST.next(), MapDirection.WEST);
    }

    @Test
    void previousTest(){
        assertEquals(MapDirection.NORTH.previous(), MapDirection.NORTH_WEST);
        assertEquals(MapDirection.SOUTH.previous(), MapDirection.SOUTH_EAST);
        assertEquals(MapDirection.WEST.previous(), MapDirection.SOUTH_WEST);
        assertEquals(MapDirection.EAST.previous(), MapDirection.NORTH_EAST);
        assertEquals(MapDirection.NORTH_EAST.previous(), MapDirection.NORTH);
        assertEquals(MapDirection.NORTH_WEST.previous(), MapDirection.WEST);
        assertEquals(MapDirection.SOUTH_WEST.previous(), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH_EAST.previous(), MapDirection.EAST);
    }

    @Test
    void toUnitVectorTest() {
        assertEquals(MapDirection.NORTH.toUnitVector(), new Vector2d(0,1));
        assertEquals(MapDirection.NORTH_WEST.toUnitVector(), new Vector2d(-1,1));
        assertEquals(MapDirection.NORTH_EAST.toUnitVector(), new Vector2d(1,1));
        assertEquals(MapDirection.SOUTH.toUnitVector(), new Vector2d(0,-1));
        assertEquals(MapDirection.SOUTH_EAST.toUnitVector(), new Vector2d(1,-1));
        assertEquals(MapDirection.SOUTH_WEST.toUnitVector(), new Vector2d(-1,-1));
        assertEquals(MapDirection.EAST.toUnitVector(), new Vector2d(1,0));
        assertEquals(MapDirection.WEST.toUnitVector(), new Vector2d(-1,0));
    }
}
