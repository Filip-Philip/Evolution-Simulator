package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import project.Animal;
import project.Plant;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private final VBox vBox;

    public GuiElementBox(Object object, double width, double height) {
        VBox potBox = new VBox();
        if (object instanceof Animal) potBox = getGraphicOfAnimal((Animal) object, width, height);
        else if (object instanceof Plant) potBox = getGraphicOfPlant((Plant) object, width, height);
        this.vBox = potBox;
    }

    public VBox getvBox() {
        return vBox;
    }

    public VBox getGraphicOfAnimal(Animal animal, double width, double height) {
        String path = animal.getGraphicRepresentation(animal.getOrientation());
        VBox box = new VBox();
        try {
            Image image = new Image(new FileInputStream(path), width, height, false, false);
            ImageView imageView = new ImageView(image);
            Label label = new Label(animal.toString());
            label.resize(width * 0.5, height * 0.5);
            box = new VBox(5.0, imageView, label);
            box.setAlignment(Pos.CENTER);
        } catch (FileNotFoundException fileNotFoundException){
            System.out.println(fileNotFoundException.getMessage());
        }
        return box;
    }

    public VBox getGraphicOfPlant(Plant plant, double width, double height) {
        String path = plant.getGraphicRepresentation();
        VBox box = new VBox();
        try {
            Image image = new Image(new FileInputStream(path), width, height, false, false);
            ImageView imageView = new ImageView(image);
            Label label = new Label(plant.getPosition().toString());
            label.resize(width * 0.5, height * 0.5);
            box = new VBox(5.0, imageView, label);
            box.setAlignment(Pos.CENTER);
        } catch (FileNotFoundException fileNotFoundException){
            System.out.println(fileNotFoundException.getMessage());
        }
        return box;
    }
}
