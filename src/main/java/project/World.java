package project;

import javafx.application.Application;

import gui.App;

public class World {
    public static void main(String[] args){
        try {
            Application.launch(App.class, args);
        }
        catch (IllegalArgumentException illegalArgument){
            System.out.println(illegalArgument.getMessage());
        }
        catch (RuntimeException runtimeException){
            System.out.println(runtimeException.getMessage());
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}
