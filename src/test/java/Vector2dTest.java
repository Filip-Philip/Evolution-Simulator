import org.junit.jupiter.api.Test;
import project.Vector2d;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2dTest {
    @Test
    void testEquals() {
        Vector2d a = new Vector2d(1,3);
        Vector2d b = new Vector2d(3,3);
        Vector2d c = new Vector2d(1,3);
        assertEquals(a.equals(b), false);
        assertEquals(a.equals(a), true);
        assertEquals(a.equals(c), true);
    }

    @Test
    void testToString() {
        Vector2d a = new Vector2d(1,3);
        assertEquals(a.toString(), "(1, 3)");
    }

    @Test
    void testPrecedes() {
        Vector2d a = new Vector2d(1,3);
        Vector2d b = new Vector2d(3,3);
        assertEquals(a.precedes(a), true);
        assertEquals(a.precedes(b), true);
        assertEquals(b.precedes(a), false);
    }

    @Test
    void testFollows() {
        Vector2d a = new Vector2d(1,3);
        Vector2d b = new Vector2d(3,3);
        assertEquals(a.follows(a), true);
        assertEquals(a.follows(b), false);
        assertEquals(b.follows(a), true);
    }

    @Test
    void testUpperRight() {
        Vector2d a = new Vector2d(1,3);
        Vector2d b = new Vector2d(3,4);
        Vector2d c = new Vector2d(2,6);
        assertEquals(a.upperRight(a), new Vector2d(1, 3));
        assertEquals(a.upperRight(b), new Vector2d(3,4));
        assertEquals(b.upperRight(c), new Vector2d(3,6));
    }

    @Test
    void testLowerLeft() {
        Vector2d a = new Vector2d(1,3);
        Vector2d b = new Vector2d(3,4);
        Vector2d c = new Vector2d(2,6);
        assertEquals(a.lowerLeft(a), new Vector2d(1, 3));
        assertEquals(a.lowerLeft(b), new Vector2d(1,3));
        assertEquals(b.lowerLeft(c), new Vector2d(2,4));
    }

    @Test
    void testAdd() {
        Vector2d a = new Vector2d(1,3);
        Vector2d b = new Vector2d(3,3);
        assertEquals(a.add(b), new Vector2d(4, 6));
    }

    @Test
    void testSubtract() {
        Vector2d a = new Vector2d(1,3);
        Vector2d b = new Vector2d(3,3);
        assertEquals(a.subtract(b), new Vector2d(-2, 0));
    }

    @Test
    void testOpposite() {
        Vector2d a = new Vector2d(1,3);
        assertEquals(a.opposite(), new Vector2d(3,1));
    }
}
