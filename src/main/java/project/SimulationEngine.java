package project;

import java.util.*;

public class SimulationEngine implements IEngine{
    private AbstractWorldMap map;
    private final int plantsToSpawnEveryday = 3;
    private final double jungleSpawnPercentage = 0.66;
    private final int numberOfAnimalsForMagicalReproduction = 5;

    public SimulationEngine(AbstractWorldMap map, int startEnergy, int moveEnergy, int startNumberOfAnimals) {
        this.map = map;
        map.spawnAnimalsInJungle(startEnergy, moveEnergy, startNumberOfAnimals);
    }

    public int getNumberOfAnimalsForMagicalReproduction() {
        return numberOfAnimalsForMagicalReproduction;
    }

    @Override
    public void run() {
        int i = 0;
        while(i < 100) {
            map.removeDeadAnimals();
            map.animalsMove();
            map.animalsEat();
            map.animalsCopulate();
            map.spawnPlants(plantsToSpawnEveryday, jungleSpawnPercentage);
            i++;
        }
    }
}
