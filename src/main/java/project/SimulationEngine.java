package project;

import gui.App;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.util.*;

public class SimulationEngine implements Runnable{
    private AbstractWorldMap map;
    private App appObserver;
    private final int plantsToSpawnEveryday = 3;
    private final double jungleSpawnPercentage = 0.66;
    private final int numberOfAnimalsForMagicalReproduction = 5;
    private final int moveDelay = 300;
    private double meanLifetime = 0;
    private int animalsDied = 0;
    private boolean stop = false;
    private boolean pause = false;
    private XYChart.Series totalAnimalsSeries = new XYChart.Series();
    private XYChart.Series totalPlantsSeries = new XYChart.Series();
    private XYChart.Series averageEnergySeries = new XYChart.Series();
    private XYChart.Series averageLifetimeSeries = new XYChart.Series();
    private XYChart.Series averageChildrenNumberSeries = new XYChart.Series();

    public SimulationEngine(AbstractWorldMap map, int startEnergy, int moveEnergy, int startNumberOfAnimals) {
        this.map = map;
        totalAnimalsSeries.setName("Total number of animals");
        totalPlantsSeries.setName("Total number of plants");
        averageEnergySeries.setName("Average energy");
        averageLifetimeSeries.setName("Average lifetime");
        averageChildrenNumberSeries.setName("Average children number");
        map.spawnAnimalsInJungle(startEnergy, moveEnergy, startNumberOfAnimals);
    }

    public int getNumberOfAnimalsForMagicalReproduction() {
        return numberOfAnimalsForMagicalReproduction;
    }

    public void addObserver(App observer){ appObserver = observer; }

    private void mapStateChanged(AbstractWorldMap map) {
        appObserver.mapStateChanged(map);
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    private void updateMapStat(AbstractWorldMap map, XYChart.Series series){
        appObserver.updateMapChart(map.getMapType(), series);
    }

    private void updateMapDominantGenotype(AbstractWorldMap map, Genotype genotype){
        appObserver.updateDominantGenotype(map, genotype);
    }

    public AbstractWorldMap getMap(){
        return map;
    }

    public void simulationPause(boolean pause){
        this.pause = pause;
    }

    @Override
    public void run() {
        int i = 0;
        while(!stop) {
            while (!pause && !stop) {
                map.removeDeadAnimals();
                map.animalsMove();
                map.animalsEat();
                map.animalsCopulate();
                map.spawnPlants(plantsToSpawnEveryday, jungleSpawnPercentage);
                int finalI = i;
                Platform.runLater(() -> {
                    mapStateChanged(map);
                    totalAnimalsSeries.getData().add(new XYChart.Data(finalI, map.getNumberOfAnimals()));
                    totalPlantsSeries.getData().add(new XYChart.Data(finalI, map.getNumberOfPlants()));
                    averageEnergySeries.getData().add(new XYChart.Data(finalI, map.getMeanEnergyForAnimalsNow()));
                    averageLifetimeSeries.getData().add(new XYChart.Data(finalI, map.getMeanLifetime()));
                    averageChildrenNumberSeries.getData().add(new XYChart.Data(finalI, map.getMeanNumberOfChildren()));
                    updateMapStat(map, totalAnimalsSeries);
                    updateMapStat(map, totalPlantsSeries);
                    updateMapStat(map, averageEnergySeries);
                    updateMapStat(map, averageLifetimeSeries);
                    updateMapStat(map, averageChildrenNumberSeries);
                    updateMapDominantGenotype(map, map.getDominantGenotype());
                });
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException interruptedException) {
                    interruptedException.getMessage();
                }
                i++;
            }
            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException interruptedException) {
                interruptedException.getMessage();
            }
        }
    }
}
