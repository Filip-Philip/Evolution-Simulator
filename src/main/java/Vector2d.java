import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) { // ctrl shift do gory w dol
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" +
                x +
                ", " +
                y +
                ")";
    }

    public boolean precedes(Vector2d other) {
        if (other.x >= this.x && other.y >= this.y) {
            return true;
        }
        return false;
    }

    public boolean follows(Vector2d other) {
        if (other.x <= this.x && other.y <= this.y) {
            return true;
        }
        return false;
    }

    public Vector2d upperRight(Vector2d other) {
        int max_x = 0;
        int max_y = 0;
        if (other.x > this.x) {
            max_x = other.x;
        }
        else {
            max_x = this.x;
        }
        if (other.y > this.y) {
            max_y = other.y;
        }
        else {
            max_y = this.y;
        }
        return new Vector2d(max_x, max_y);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int min_x = 0;
        int min_y = 0;
        if (other.x < this.x) {
            min_x = other.x;
        }
        else {
            min_x = this.x;
        }
        if (other.y < this.y) {
            min_y = other.y;
        }
        else {
            min_y = this.y;
        }
        return new Vector2d(min_x, min_y);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2d)) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d opposite(){
        return new Vector2d(this.y, this.x);
    }

}
