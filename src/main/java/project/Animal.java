package project;

import java.util.*;

public class Animal {
    private Vector2d position;
    private MapDirection orientation;
    private final int START_ENERGY;
    private int energy;
    private final int moveEnergy;
    private final Genotype genotype;
    private ArrayList<Animal> children = new ArrayList<>();
    private int lifetime = 0;
    private final Random random;
    private final AbstractWorldMap map;

    public Animal(Vector2d initialPosition, int energy, int moveEnergy, AbstractWorldMap map, Random random) {
        this.genotype = new Genotype();
        this.START_ENERGY = energy;
        this.random = random;
        this.moveEnergy = moveEnergy;
        this.map = map;
        this.isGenericAnimal(initialPosition, energy);
        map.place(this);
    }

    public Animal(Vector2d initialPosition, int energy, int START_ENERGY, int moveEnergy, Animal parent1,
                  Animal parent2, AbstractWorldMap map, Random random){
        this.genotype = new Genotype(parent1, parent2);
        this.START_ENERGY = START_ENERGY;
        this.random = random;
        this.moveEnergy = moveEnergy;
        this.map = map;
        this.isGenericAnimal(initialPosition, energy);
    }

    public Animal(Vector2d initialPosition, int startEnergy, int moveEnergy, Animal animal, AbstractWorldMap map, Random random){
        this.genotype = animal.getGenotype();
        this.START_ENERGY = startEnergy;
        this.moveEnergy = moveEnergy;
        this.map = map;
        this.random = random;
        isGenericAnimal(initialPosition, startEnergy);
        map.place(this);
    }

    private void isGenericAnimal(Vector2d initialPosition, int energy){
        this.orientation = randomOrientation();;
        this.position = initialPosition;
        this.energy = energy;
    }

    public int getLifetime(){ return lifetime; }

    public void updateLifetime(){
        lifetime++;
    }

    public int getMoveEnergy(){
        return moveEnergy;
    }

    public int getSTART_ENERGY(){ return START_ENERGY; }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    private MapDirection randomOrientation(){
        int numberOfUniqueGenes = genotype.getNumberOfUniqueGenes();
        int orientationNumber = random.nextInt(numberOfUniqueGenes);
        MapDirection randomDirection = MapDirection.NORTH;
        for (int i = 0; i < orientationNumber; i++) {
            randomDirection = randomDirection.next();
        }
        return randomDirection;
    }

    private void useEnergy(int n){ this.energy -= n; }

    private void gainEnergy(int n) { this.energy += n; }

    public ArrayList getChildren(){
        return children;
    }

    public Animal copulateWith(Animal other){
        int energyUsedByParent1 = this.getEnergy() / 4;
        int energyUsedByParent2 = other.getEnergy() / 4;
        int energyOfChild = energyUsedByParent1 + energyUsedByParent2;
        Animal child = new Animal(position, energyOfChild, START_ENERGY, moveEnergy, this, other, map, random);
        this.useEnergy(energyUsedByParent1);
        other.useEnergy(energyUsedByParent2);
        this.addChild(child);
        other.addChild(child);
        return child;
    }

    private void addChild(Animal child){
        this.children.add(child);
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() { return orientation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return START_ENERGY == animal.START_ENERGY && energy == animal.energy && Objects.equals(position, animal.position)
                && orientation == animal.orientation && Objects.equals(genotype, animal.genotype)
                && Objects.equals(children, animal.children) && Objects.equals(random, animal.random)
                && Objects.equals(map, animal.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, orientation, START_ENERGY, energy, genotype, children, random, map);
    }

    @Override
    public String toString() {
        String direction = orientation.toString();
        String coordinates = position.toString();
        return "(" + energy + "," + direction + ", " + coordinates + ")";
    }

    private void turnNTimes(int n){
        for (int i = 0; i < n; i++) {
            orientation = orientation.next();
        }
    }

    public void move(int turn) throws IllegalArgumentException {
        switch (turn) {
            case 0, 4 -> {
                Vector2d movementVector = orientation.toUnitVector();
                Vector2d newPosition;
                if (turn == 0) newPosition = position.add(movementVector);
                else newPosition = position.subtract(movementVector);

                if (map.canMoveTo(newPosition)) {
                    int mapSizeX = map.getMapTopRightCorner().x + 1;
                    int mapSizeY = map.getMapTopRightCorner().y + 1;
                    int newX = ((newPosition.x % mapSizeX) + mapSizeX) % mapSizeX;
                    int newY = ((newPosition.y % mapSizeY) + mapSizeY) % mapSizeY;
                    newPosition = new Vector2d(newX, newY);
                    position = newPosition;
                }
            }
            case 1, 2, 3, 5, 6, 7 -> this.turnNTimes(turn);
            default -> throw new IllegalArgumentException(turn + " is not legal move specification");
        }
        useEnergy(moveEnergy);
    }

    public void eat(Plant plant, int numberOfAnimalsSharingTheFood){
        if (plant.getPosition().equals(this.position)) {
            int onePortion = plant.getNutritionalValue() / numberOfAnimalsSharingTheFood;
            this.gainEnergy(onePortion);
        }
    }

    public String getGraphicRepresentation(MapDirection direction){
        return switch (direction) {
            case NORTH -> "src/main/resources/Animal NORTH.png";
            case NORTH_EAST -> "src/main/resources/Animal NORTH EAST.png";
            case NORTH_WEST -> "src/main/resources/Animal NORTH WEST.png";
            case EAST -> "src/main/resources/Animal EAST.png";
            case WEST -> "src/main/resources/Animal WEST.png";
            case SOUTH -> "src/main/resources/Animal SOUTH.png";
            case SOUTH_EAST -> "src/main/resources/Animal SOUTH EAST.png";
            case SOUTH_WEST -> "src/main/resources/Animal SOUTH WEST.png";
        };
    }
}