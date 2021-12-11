public class Grass implements IOverlappable{
    private final Vector2d grassPosition;

    public Grass(Vector2d grassPosition){
        this.grassPosition = grassPosition;
    }

    public Vector2d getPosition(){
        return grassPosition;
    }

    public String toString(){
        return "*";
    }

    @Override
    public boolean canBeOverlapped() { return true; }
}
