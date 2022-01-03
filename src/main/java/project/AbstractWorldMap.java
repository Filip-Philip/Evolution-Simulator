package project;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap {
    protected final Vector2d MAP_BOTTOM_LEFT_CORNER = new Vector2d(0, 0);
    protected final Vector2d mapTopRightCorner;
    protected final Vector2d jungleBottomLeftCorner;
    protected final Vector2d jungleTopRightCorner;
    protected Map<Vector2d, SortedSet<Animal>> animalsHashMap = new HashMap<>();
    protected Map<Vector2d, Plant> plantsInJungleHashMap = new HashMap<>();
    protected Map<Vector2d, Plant> plantsInSteppeHashMap = new HashMap<>();
    protected final int plantsNutritionalValue;
    protected double meanLifetime = 0;
    protected int animalsDied = 0;
    protected final Random random;
    protected Map<Genotype, Integer> genotypeOccurrencesMap = new HashMap<>();

    // THROWS IllegalArgumentException jesli startNumberOfPlants > jungleArea
    public AbstractWorldMap(int width, int height, double jungleRatio, int startNumberOfPlants, int plantsNutritionalValue,
                            Random random) {
        this.mapTopRightCorner = new Vector2d(width, height);
        Vector2d[] jungleCorners = createJungleCorners(width, height, jungleRatio);
        this.jungleBottomLeftCorner = jungleCorners[0];
        this.jungleTopRightCorner = jungleCorners[1];
        this.plantsNutritionalValue = plantsNutritionalValue;
        this.random = random;
        spawnPlantsInJungle(startNumberOfPlants);
    }

    public void spawnAnimalsInJungle(int startEnergy, int moveEnergy, int startNumberOfAnimals) {
        Vector2d bottomLeftOfSpawnArea = getJungleBottomLeftCorner();
        Vector2d topRightOfSpawnArea = getJungleTopRightCorner();
        Vector2d spawnPosition;
        int spawnArea = (topRightOfSpawnArea.x - bottomLeftOfSpawnArea.x + 1) * (topRightOfSpawnArea.y - bottomLeftOfSpawnArea.y + 1);
        int i = 0;
        while (i < startNumberOfAnimals && getNumberOfSpotsOccupiedByAnimalsInJungle() < spawnArea) {
            int spawnPositionX = random.nextInt(topRightOfSpawnArea.x + 1 - bottomLeftOfSpawnArea.x) + bottomLeftOfSpawnArea.x;
            int spawnPositionY = random.nextInt(topRightOfSpawnArea.y + 1 - bottomLeftOfSpawnArea.y) + bottomLeftOfSpawnArea.y;
            spawnPosition = new Vector2d(spawnPositionX, spawnPositionY);
            if (!animalsHashMap.containsKey(spawnPosition)) {
                Animal animal = new Animal(spawnPosition, startEnergy, moveEnergy, this, random);
                i++;
            }
        }
    }

    public void spawnAnimalOnMap(int startEnergy, int moveEnergy, Animal animal) {
        Vector2d bottomLeftOfSpawnArea = getMapBottomLeftCorner();
        Vector2d topRightOfSpawnArea = getMapTopRightCorner();
        Vector2d spawnPosition;
        int spawnArea = (topRightOfSpawnArea.x - bottomLeftOfSpawnArea.x + 1) * (topRightOfSpawnArea.y - bottomLeftOfSpawnArea.y + 1);
        if (animalsHashMap.size() < spawnArea) {
            int spawnPositionX = random.nextInt(topRightOfSpawnArea.x + 1 - bottomLeftOfSpawnArea.x) + bottomLeftOfSpawnArea.x;
            int spawnPositionY = random.nextInt(topRightOfSpawnArea.y + 1 - bottomLeftOfSpawnArea.y) + bottomLeftOfSpawnArea.y;
            spawnPosition = new Vector2d(spawnPositionX, spawnPositionY);
            if (!animalsHashMap.containsKey(spawnPosition)) {
                Animal animalSpawned = new Animal(spawnPosition, startEnergy, moveEnergy, animal, this, random);
            }
        }
    }

    public void spawnPlantsInJungle(int numberOfPlantsToBeSpawned) {
        int widthOfJungle = jungleTopRightCorner.x - jungleBottomLeftCorner.x + 1;
        int heightOfJungle = jungleTopRightCorner.y - jungleBottomLeftCorner.y + 1;
        int areaOfJungle = widthOfJungle * heightOfJungle;
        int numberOfPlantsInJungle = plantsInJungleHashMap.size();
        int numberOfAnimalsInJungle = getNumberOfSpotsOccupiedByAnimalsInJungle();
        int numberOfSpotsLeft = areaOfJungle - numberOfPlantsInJungle - numberOfAnimalsInJungle;
        int plantsAdded = 0;
        int positionX;
        int positionY;
        while (plantsAdded < numberOfPlantsToBeSpawned && plantsAdded < numberOfSpotsLeft) {
            positionX = random.nextInt(widthOfJungle) + jungleBottomLeftCorner.x;
            positionY = random.nextInt(heightOfJungle) + jungleBottomLeftCorner.y;
            Vector2d position = new Vector2d(positionX, positionY);
            if (plantsInJungleHashMap.containsKey(position) || animalsHashMap.containsKey(position)) continue;
            Plant plant = new Plant(position, plantsNutritionalValue);
            plantsInJungleHashMap.put(position, plant);
            plantsAdded++;
        }
    }

    private int getNumberOfSpotsOccupiedByAnimalsInJungle() {
        int counter = 0;
        List<Vector2d> positions = new ArrayList<Vector2d>(animalsHashMap.keySet());
        for (Vector2d position : positions) {
            if (isInJungle(position)) counter++;
        }
        return counter;
    }

    public void spawnPlantsInSteppe(int numberOfPlantsToBeSpawned) {
        int widthOfMap = mapTopRightCorner.x - MAP_BOTTOM_LEFT_CORNER.x + 1;
        int heightOfMap = mapTopRightCorner.y - MAP_BOTTOM_LEFT_CORNER.y + 1;
        int areaOfJungle = (jungleTopRightCorner.x - jungleBottomLeftCorner.x + 1) * (jungleTopRightCorner.y - jungleBottomLeftCorner.y + 1);
        int areaOfMap = widthOfMap * heightOfMap;
        int areaOfSteppe = areaOfMap - areaOfJungle;
        int numberOfAnimalsOnSteppe = getNumberOfSpotsOccupiedByAnimalsOnSteppe();
        int numberOfPlantsOnSteppe = plantsInSteppeHashMap.size();
        int numberOfSpotsLeft = areaOfSteppe - numberOfPlantsOnSteppe - numberOfAnimalsOnSteppe;
        int positionX = random.nextInt(widthOfMap) + MAP_BOTTOM_LEFT_CORNER.x;
        int positionY = random.nextInt(heightOfMap) + MAP_BOTTOM_LEFT_CORNER.y;
        Vector2d position = new Vector2d(positionX, positionY);
        Vector2d somePositionInJungle = new Vector2d(jungleBottomLeftCorner.x, jungleBottomLeftCorner.y);
        int plantsAdded = 0;

        int numberOfAttempts = 0;
        while (plantsAdded < numberOfPlantsToBeSpawned && plantsAdded < numberOfSpotsLeft && numberOfAttempts < 100) {
            while (isInJungle(position)) {
                positionX = random.nextInt(widthOfMap) + MAP_BOTTOM_LEFT_CORNER.x;
                positionY = random.nextInt(heightOfMap) + MAP_BOTTOM_LEFT_CORNER.y;
                position = new Vector2d(positionX, positionY);
            }

            if (plantsInSteppeHashMap.containsKey(position) || animalsHashMap.containsKey(position)) {
                position = somePositionInJungle;
                numberOfAttempts++;
                continue;
            }
            Plant plant = new Plant(position, plantsNutritionalValue);
            plantsInSteppeHashMap.put(position, plant);
            plantsAdded++;
        }
    }

    public void removePlant(Vector2d position) {
        if (isInJungle(position)) plantsInJungleHashMap.remove(position);
        else plantsInSteppeHashMap.remove(position);
    }

    private int getNumberOfSpotsOccupiedByAnimalsOnSteppe() {
        return animalsHashMap.size() - getNumberOfSpotsOccupiedByAnimalsInJungle();
    }

    private Vector2d[] createJungleCorners(int width, int height, double jungleRatio) {
        int jungleWidth = (int) (width * jungleRatio);
        int jungleHeight = (int) (height * jungleRatio);
        if ((width % 2 == 1 && jungleWidth % 2 == -0) || (width % 2 == 0 && jungleWidth % 2 == 1)) jungleWidth -= 1;
        if ((height % 2 == 1 && jungleHeight % 2 == 0) || (height % 2 == 0 && jungleHeight % 2 == 1)) jungleHeight -= 1;
        Vector2d jungleBottomLeftCorner;
        Vector2d jungleTopRightCorner;
        int jungleBottomLeftCornerX = (width - jungleWidth) / 2;
        int jungleBottomLeftCornerY = (height - jungleHeight) / 2;
        jungleBottomLeftCorner = new Vector2d(jungleBottomLeftCornerX, jungleBottomLeftCornerY);
        int jungleTopRightCornerX = width - jungleBottomLeftCornerX;
        int jungleTopRightCornerY = height - jungleBottomLeftCornerY;
        jungleTopRightCorner = new Vector2d(jungleTopRightCornerX, jungleTopRightCornerY);
        Vector2d[] jungleCorners = {jungleBottomLeftCorner, jungleTopRightCorner};
        return jungleCorners;
    }

    @Override
    public String toString() {
        Vector2d bottomLeftMap = getMapBottomLeftCorner();
        Vector2d topRightMap = getMapTopRightCorner();
        MapVisualiser mapVisualisation = new MapVisualiser(this);
        return mapVisualisation.draw(bottomLeftMap, topRightMap);
    }


    public Vector2d getJungleBottomLeftCorner() {
        return jungleBottomLeftCorner;
    }

    public Vector2d getJungleTopRightCorner() {
        return jungleTopRightCorner;
    }

    @Override
    public Vector2d getMapTopRightCorner() {
        return mapTopRightCorner;
    }

    @Override
    public Vector2d getMapBottomLeftCorner() {
        return MAP_BOTTOM_LEFT_CORNER;
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d animalPosition = animal.getPosition();
        if (animalsHashMap.containsKey(animalPosition)) {
            SortedSet<Animal> animalsOnThisPosition = animalsHashMap.get(animalPosition);
            animalsOnThisPosition.add(animal);
        } else {
            SortedSet<Animal> animalsOnThisPosition = new TreeSet<Animal>(new AnimalComparator());
            animalsOnThisPosition.add(animal);
            animalsHashMap.put(animalPosition, animalsOnThisPosition);
        }
        addGenotype(animal.getGenotype());
        return true;
    }

    private void removeGenotype(Genotype genotype){
        int numberOfOccurrences = genotypeOccurrencesMap.get(genotype);
        numberOfOccurrences--;
        genotypeOccurrencesMap.remove(genotype);
        if (numberOfOccurrences > 0) {
            genotypeOccurrencesMap.put(genotype, numberOfOccurrences);
        }
    }

    private void addGenotype(Genotype genotype){
        if (genotypeOccurrencesMap.containsKey(genotype)) {
            int numberOfOccurrences = genotypeOccurrencesMap.get(genotype);
            numberOfOccurrences++;
            genotypeOccurrencesMap.remove(genotype);
            genotypeOccurrencesMap.put(genotype, numberOfOccurrences);
        }
        else {
            genotypeOccurrencesMap.put(genotype, 1);
        }
    }

    public void animalsMove() {
        ArrayList<Animal> animalThatChangedPosition = new ArrayList<>();
        ArrayList<Vector2d> positionsEmptied = new ArrayList<>();
        for (var entry : animalsHashMap.entrySet()) {
            SortedSet<Animal> setOfAnimals = entry.getValue();
            for (Iterator<Animal> iter = setOfAnimals.iterator(); iter.hasNext(); ) {
                Animal animal = iter.next();
                animal.updateLifetime();
                ArrayList<Integer> genes = animal.getGenotype().getGenes();
                int genotypeLength = genes.size();
                int numberOfGene = random.nextInt(genotypeLength);
                int animalMove = genes.get(numberOfGene);
                animal.move(animalMove);
                if (animalMove == 0 || animalMove == 4) {
                    animalThatChangedPosition.add(animal);
                    iter.remove();
                }
            }
            if (setOfAnimals.isEmpty()) positionsEmptied.add(entry.getKey());
        }
        for (Vector2d positionEmptied : positionsEmptied) {
            animalsHashMap.remove(positionEmptied);
        }

        for (Animal animal : animalThatChangedPosition) {
            place(animal);
        }
    }

    public void animalsEat() {
        List<Animal> animalsToFeed;
        for (var entry : animalsHashMap.entrySet()) {
            Vector2d position = entry.getKey();
            if (isPlantOnPosition(position)) {
                animalsToFeed = new ArrayList<>();
                SortedSet<Animal> setOfAnimals = entry.getValue();
                Animal animalWithHighestEnergy = setOfAnimals.last();
                for (Animal animal : setOfAnimals) {
                    if (animal.getEnergy() == animalWithHighestEnergy.getEnergy()) animalsToFeed.add(animal);
                }
                int numberOfAnimalsSharingFood = animalsToFeed.size();
                Plant plant = getPlantOnPosition(position);
                for (Animal animal : animalsToFeed) {
                    animal.eat(plant, numberOfAnimalsSharingFood);
                }
                removePlant(position);
            }
        }
    }

    public void animalsCopulate() {
        List<Animal> animalsBorn = new ArrayList<>();
        List<Animal> animalsCopulated = new ArrayList<>();
        List<Animal> animalsCompetingToCopulate;
        for (var entry : animalsHashMap.entrySet()) {
            TreeSet<Animal> setOfAnimals = (TreeSet<Animal>) entry.getValue();
            if (setOfAnimals.size() >= 2) {
                Animal animalWithHighestEnergy = setOfAnimals.last();
                animalsCompetingToCopulate = new ArrayList<>();
                int lowestEnergyContender;
                for (Iterator<Animal> it = setOfAnimals.descendingIterator(); it.hasNext(); ) {
                    Animal animal = it.next();
                    if (animal.getEnergy() < animalWithHighestEnergy.getEnergy() && animalsCompetingToCopulate.size() >= 2)
                        break;
                    animalsCompetingToCopulate.add(animal);
                    lowestEnergyContender = animal.getEnergy();
                    if (lowestEnergyContender < animalWithHighestEnergy.getEnergy()) break;
                }

                Collections.shuffle(animalsCompetingToCopulate);
                List<Animal> animalsCopulating = animalsCompetingToCopulate.subList(0, 2);
                Animal animalCopulating1 = animalsCopulating.get(0);
                Animal animalCopulating2 = animalsCopulating.get(1);
                int energyNeeded = animalCopulating1.getSTART_ENERGY() / 2;
                if (animalCopulating1.getEnergy() >= energyNeeded && animalCopulating2.getEnergy() >= energyNeeded) {
                    setOfAnimals.removeAll(animalsCopulating);
                    Animal child = animalCopulating1.copulateWith(animalCopulating2);
                    animalsCopulated.addAll(animalsCopulating);
                    animalsBorn.add(child);
                }
            }
        }
        for (Animal animal : animalsBorn) {
            place(animal);
        }
        for (Animal animal : animalsCopulated){
            place(animal);
        }
    }

    public void spawnPlants(int plantsToSpawnEveryday, double jungleSpawnPercentage) {
        int plantsSpawnedInJungle = (int) Math.round(plantsToSpawnEveryday * jungleSpawnPercentage);
        int plantsSpawnedOnSteppe = plantsToSpawnEveryday - plantsSpawnedInJungle;
        spawnPlantsInJungle(plantsSpawnedInJungle);
        spawnPlantsInSteppe(plantsSpawnedOnSteppe);
    }

    public void removeDeadAnimals() {
        int lifetimeOfDead = 0;
        int numberOfDead = 0;
        ArrayList<Vector2d> positionsEmptied = new ArrayList<>();
        for (var entry : animalsHashMap.entrySet()) {
            SortedSet<Animal> setOfAnimals = entry.getValue();
            for (Iterator<Animal> iter = setOfAnimals.iterator(); iter.hasNext(); ) {
                Animal animal = iter.next();
                if (animal.getEnergy() <= 0) {
                    lifetimeOfDead += animal.getLifetime();
                    numberOfDead++;
                    removeGenotype(animal.getGenotype());
                    iter.remove();
                }
            }
            if (setOfAnimals.isEmpty()) positionsEmptied.add(entry.getKey());
        }
        updateMeanLifetime(numberOfDead, lifetimeOfDead);
        for (Vector2d positionEmptied : positionsEmptied) {
            animalsHashMap.remove(positionEmptied);
        }
    }

    private void updateMeanLifetime(int animalsDiedThisTurn, int lifetimeOfDeadAnimalsThisTurn) {
        if (animalsDiedThisTurn != 0) {
            double newTotalLifetime = animalsDied * meanLifetime + lifetimeOfDeadAnimalsThisTurn;
            animalsDied += animalsDiedThisTurn;
            meanLifetime = newTotalLifetime / animalsDied;
        }
    }

    public double getMeanLifetime() {
        return meanLifetime;
    }

    public void animalsMagicallyReproduce(int numberOfAnimalsForMagicalReproduction) {
        int countAnimals = 0;
        ArrayList<Animal> animalsToCopy = new ArrayList<>();
        for (var entry : animalsHashMap.entrySet()) {
            SortedSet<Animal> setOfAnimals = entry.getValue();
            animalsToCopy.addAll(setOfAnimals);
            countAnimals += setOfAnimals.size();
        }
        if (countAnimals == numberOfAnimalsForMagicalReproduction) {
            for (int i = 0; i < countAnimals; i++) {
                Animal animalToCopy = animalsToCopy.get(i);
                spawnAnimalOnMap(animalToCopy.getSTART_ENERGY(), animalToCopy.getMoveEnergy(), animalToCopy);
            }
        }
    }

    private boolean isInJungle(Vector2d position) {
        return position.precedes(jungleTopRightCorner) && position.follows(jungleBottomLeftCorner);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return (animalsHashMap.containsKey(position) && !animalsHashMap.get(position).isEmpty())
                || plantsInJungleHashMap.containsKey(position) || plantsInSteppeHashMap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animalsHashMap.containsKey(position)) return animalsHashMap.get(position).first();
        return getPlantOnPosition(position);
    }

    public abstract MapSwitch getMapType();

    @Override
    public abstract boolean canMoveTo(Vector2d position);

    public boolean isOutsideTheMap(Vector2d position) {
        return position.precedes(getMapTopRightCorner()) && position.follows(getMapBottomLeftCorner());
    }

    public boolean isPlantOnPosition(Vector2d position) {
        return plantsInJungleHashMap.containsKey(position) || plantsInSteppeHashMap.containsKey(position);
    }

    public Plant getPlantOnPosition(Vector2d position) {
        if (isInJungle(position)) {
            return plantsInJungleHashMap.get(position);
        }
        return plantsInSteppeHashMap.get(position);
    }

    public int getNumberOfAnimals() {
        int countAnimals = 0;
        for (var entry : animalsHashMap.entrySet()) {
            SortedSet<Animal> setOfAnimals = entry.getValue();
            countAnimals += setOfAnimals.size();
        }
        return countAnimals;
    }

    public int getNumberOfPlants() {
        int countPlants = 0;
        countPlants += plantsInJungleHashMap.size();
        countPlants += plantsInSteppeHashMap.size();
        return countPlants;
    }

    public double getMeanEnergyForAnimalsNow() {
        int totalEnergy = 0;
        int countAnimals = 0;
        for (var entry : animalsHashMap.entrySet()) {
            SortedSet<Animal> setOfAnimals = entry.getValue();
            for (Animal animal : setOfAnimals) {
                totalEnergy += animal.getEnergy();
                countAnimals++;
            }
        }
        if (countAnimals == 0) return 0;
        return  ((double) totalEnergy) / countAnimals;
    }

    public Genotype getDominantGenotype(){
        Genotype dominantGenotype = new Genotype();
        int occurrences = 0;
        for (var entry : genotypeOccurrencesMap.entrySet()){
            if (occurrences > entry.getValue()){
                dominantGenotype = entry.getKey();
            }
        }
        return dominantGenotype;
    }

    public double getMeanNumberOfChildren(){
        int totalAnimals = 0;
        int totalChildren = 0;
        for (var entry : animalsHashMap.entrySet()){
            SortedSet<Animal> set = entry.getValue();
            for(Animal animal : set){
                totalAnimals++;
                if (!animal.getChildren().isEmpty()) {
                    totalChildren += animal.getChildren().size();
                }
            }
        }
        if (totalAnimals == 0) return 0;
        return ((double) totalChildren) / totalAnimals;
    }
}
