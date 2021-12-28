package gui;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import project.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class App extends Application implements MapStateChangeObserver{
    private AbstractWorldMap wrappedMap;
    private AbstractWorldMap boundedMap;
    private SimulationEngine engineWrapped;
    private SimulationEngine engineBounded;
    private GridPane gridWrapped;
    private GridPane gridBounded;
    private VBox leftBox;
    private HBox centerBox;
    private VBox rightBox;
    private HBox topBox;
    private HBox bottomBox;
    private LineChart wrappedMapChart;
    private LineChart boundedMapChart;
    private GridPane wrappedDominantGenotype;
    private GridPane boundedDominantGenotype;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Random random = new Random();

            gridWrapped = new GridPane();
            gridWrapped.setAlignment(Pos.CENTER);
            gridWrapped.setGridLinesVisible(true);

            gridBounded = new GridPane();
            gridBounded.setAlignment(Pos.CENTER);
            gridBounded.setGridLinesVisible(true);

            centerBox = new HBox(100.0, gridWrapped, gridBounded);


            Button pauseBoundedButton = new Button("Pause Bounded map");
            Button pauseWrappedButton = new Button("Pause Wrapped map");
            bottomBox = new HBox(10.0, pauseWrappedButton, pauseBoundedButton);
            pauseWrappedButton.setOnAction(event -> {
                    pause(engineWrapped);
                    });

            pauseBoundedButton.setOnAction(event -> {
                pause(engineBounded);
            });

            Button startButton = new Button("Start");
            TextField mapWidth = new TextField();
            TextField mapHeight = new TextField();
            TextField startNumberOfAnimals = new TextField();
            TextField startEnergy = new TextField();
            TextField moveEnergy = new TextField();
            TextField plantEnergy = new TextField();
            TextField jungleRatio = new TextField();
            topBox = new HBox(10.0, mapWidth, mapHeight, startNumberOfAnimals, startEnergy, moveEnergy, plantEnergy, jungleRatio, startButton);
            topBox.setAlignment(Pos.CENTER);
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(centerBox);
            borderPane.setTop(topBox);
            borderPane.setBottom(bottomBox);
            BorderPane.setAlignment(startButton, Pos.CENTER);
            mapWidth.setPromptText("Enter map width");
            mapHeight.setPromptText("Enter map height");
            startNumberOfAnimals.setPromptText("Enter start number of animals");
            startEnergy.setPromptText("Enter start energy of animals");
            moveEnergy.setPromptText("Enter move energy of animals");
            plantEnergy.setPromptText("Enter energy given by eating plants");
            jungleRatio.setPromptText("Enter jungle in map content");

            setLeftBox(borderPane);
            setRightBox(borderPane);

            Scene scene = new Scene(borderPane, 1100, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
            startButton.setOnAction(event -> {
                int width = Integer.parseInt(mapWidth.getText());
                int height = Integer.parseInt(mapHeight.getText());
                int startNumberAnimals = Integer.parseInt(startNumberOfAnimals.getText());
                int animalStartEnergy = Integer.parseInt(startEnergy.getText());
                int animalsMoveEnergy = Integer.parseInt(moveEnergy.getText());
                int plantConsumptionEnergy = Integer.parseInt(plantEnergy.getText());
                double jungleContent = Double.parseDouble(jungleRatio.getText());

                wrappedMap = new WrappedRectangularMap(width, height, jungleContent, 5, plantConsumptionEnergy, random);
                boundedMap = new BoundedRectangularMap(width, height, jungleContent, 5, plantConsumptionEnergy, random);
                constructMap(wrappedMap, gridWrapped);
                constructMap(boundedMap, gridBounded);
                engineWrapped = new SimulationEngine(wrappedMap, animalStartEnergy, animalsMoveEnergy, startNumberAnimals);
                engineBounded = new SimulationEngine(boundedMap, animalStartEnergy, animalsMoveEnergy, startNumberAnimals);
                engineWrapped.addObserver(this);
                engineBounded.addObserver(this);
                Thread engineThreadWrapped = new Thread(engineWrapped);
                Thread engineThreadBounded = new Thread(engineBounded);
                engineThreadWrapped.start();
                engineThreadBounded.start();
            });
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    private void setRightBox(BorderPane borderPane){
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Numbers");
        boundedMapChart = new LineChart<Number, Number>(xAxis, yAxis);
        boundedMapChart.setTitle("Bounded map stats");
        boundedDominantGenotype = new GridPane();
        Label genotypeLabel = new Label("Dominant Genotype");
        VBox dominantGenotype = new VBox(5.0, genotypeLabel, boundedDominantGenotype);
        rightBox = new VBox(10.0, boundedMapChart, dominantGenotype);
        dominantGenotype.setAlignment(Pos.CENTER);
        borderPane.setRight(rightBox);
    }

    private void setLeftBox(BorderPane borderPane){
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Numbers");
        wrappedMapChart = new LineChart<Number, Number>(xAxis, yAxis);
        wrappedMapChart.setTitle("Wrapped map stats");
        wrappedDominantGenotype = new GridPane();
        Label genotypeLabel = new Label("Dominant Genotype");
        VBox dominantGenotype = new VBox(5.0, genotypeLabel, wrappedDominantGenotype);
        leftBox = new VBox(10.0, wrappedMapChart, dominantGenotype);
        dominantGenotype.setAlignment(Pos.CENTER);
        borderPane.setLeft(leftBox);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public void pause(SimulationEngine engine){
        AbstractWorldMap mapEngine = engine.getMap();
        MapSwitch mapSwitch = mapEngine.getMapType();
        switch (mapSwitch){
            case WRAPPED_RECTANGULAR_MAP -> engineWrapped.simulationPause(true);
            case BOUNDED_RECTANGULAR_MAP -> engineBounded.simulationPause(true);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        engineWrapped.setStop(true);
        engineBounded.setStop(true);
    }

    private void constructMap(AbstractWorldMap map, GridPane grid){
        int width = map.getMapTopRightCorner().x;
        int height = map.getMapTopRightCorner().y;
        double column_width = 50.0;
        double row_width = 50.0;
        double picWidth = column_width * 0.4;
        double picHeight = row_width * 0.4;
        for (int i = 0; i < height + 2; i++) {
            grid.getRowConstraints().add(new RowConstraints(row_width));
        }
        for (int i = 0; i < width + 2; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(column_width));
        }
        Label label = new Label("y\\x");
        grid.add(label, 0,0);
        GridPane.setHalignment(label, HPos.CENTER);
        GuiElementBox box;
        for (int i = 1; i < height + 2; i++) {
            label = new Label(Integer.toString(height + 1 - i));
            grid.add(label, 0, i);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i = 1; i < width + 2; i++) {
            label = new Label(Integer.toString(i - 1));
            grid.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i = 1; i < width + 2; i++) {
            for (int j = 1; j < height + 2; j++) {
                Vector2d position = new Vector2d(i-1, j-1);
                if (map.isOccupied(position) && !map.isPlantOnPosition(position)){
                    box = new GuiElementBox(map.objectAt(position), picWidth, picHeight);
                    grid.add(box.getvBox(), i, height + 2 - j);
                    GridPane.setHalignment(box.getvBox(), HPos.CENTER);
                }
                else if(map.isPlantOnPosition(position)){
                    box = new GuiElementBox(map.objectAt(position), picWidth, picHeight);
                    grid.add(box.getvBox(), i, height + 2 - j);
                    GridPane.setHalignment(box.getvBox(), HPos.CENTER);
                }
            }
        }
    }

    public void updateMap(AbstractWorldMap map, GridPane grid){
        grid.setGridLinesVisible(false);
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.getChildren().clear();
        grid.setGridLinesVisible(true);
        constructMap(map, grid);
    }

    public void updateMapChart(MapSwitch mapType, XYChart.Series series){
        switch (mapType){
            case WRAPPED_RECTANGULAR_MAP -> wrappedMapChart.getData().add(series);
            case BOUNDED_RECTANGULAR_MAP -> boundedMapChart.getData().add(series);
        }
    }

    private void constructGenotype(AbstractWorldMap map, Genotype genotype){
        int genotypeLength = genotype.getGENOTYPE_LENGTH();
        ArrayList<Integer> genes = genotype.getGenes();
        double columnWidth = 15.0;
        double rowWidth = 15.0;

        MapSwitch mapType = map.getMapType();
        GridPane dominantGenotype = switch (mapType){
            case WRAPPED_RECTANGULAR_MAP -> wrappedDominantGenotype;
            case BOUNDED_RECTANGULAR_MAP -> boundedDominantGenotype;
        };

        for (int i = 0; i < genotypeLength; i++) {
            dominantGenotype.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        }
        dominantGenotype.getRowConstraints().add(new RowConstraints(rowWidth));

        if (map.getNumberOfAnimals() > 0) {
            for (int i = 0; i < genotypeLength; i++) {
                Label gene = new Label(Integer.toString(genes.get(i)));
                dominantGenotype.add(gene, i, 0);
            }
        }
    }

    public void updateDominantGenotype(AbstractWorldMap map, Genotype genotype){
        MapSwitch mapSwitch = map.getMapType();
        GridPane dominantGenotype = switch (mapSwitch){
            case WRAPPED_RECTANGULAR_MAP -> wrappedDominantGenotype;
            case BOUNDED_RECTANGULAR_MAP -> boundedDominantGenotype;
        };
        dominantGenotype.setGridLinesVisible(false);
        dominantGenotype.getRowConstraints().clear();
        dominantGenotype.getColumnConstraints().clear();
        dominantGenotype.getChildren().clear();
        constructGenotype(map, genotype);
        dominantGenotype.setGridLinesVisible(true);
    }

    @Override
    public void mapStateChanged(AbstractWorldMap map) {
        MapSwitch mapType = map.getMapType();
        switch (mapType){
            case WRAPPED_RECTANGULAR_MAP -> updateMap(map, gridWrapped);
            case BOUNDED_RECTANGULAR_MAP -> updateMap(map, gridBounded);
        }
    }
}
