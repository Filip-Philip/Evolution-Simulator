package project;

import java.util.Objects;
import java.lang.Math;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
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
        int max_x = Math.max(this.x, other.x);
        int max_y = Math.max(this.y, other.y);
        return new Vector2d(max_x, max_y);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int min_x = Math.min(this.x, other.x);
        int min_y = Math.min(this.y, other.y);
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
