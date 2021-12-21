package project;

public class Plant {
    private final Vector2d plantPosition;
    private final int nutritionalValue;

    public Plant(Vector2d plantPosition, int nutritionalValue){
        this.plantPosition = plantPosition;
        this.nutritionalValue = nutritionalValue;
    }

    public Vector2d getPosition(){
        return plantPosition;
    }

    public int getNutritionalValue(){ return nutritionalValue; }

    public String toString(){
        return "*";
    }
}
