package br.estacio.consultasapp;

import br.estacio.consultasapp.controller.GuiManager;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Manager.addManager(new DatabaseManager(), new UserManager(), new GuiManager());

        Manager.getManagers().stream()
                .filter(manager -> !manager.getClass().equals(DatabaseManager.class))
                .forEach(Manager::start);

        Manager.getManager(DatabaseManager.class).start();
    }

    public static void main(String[] args) {
        launch();
    }

}