package br.estacio.consultasapp;

import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    private double x;
    private double y;

    @Override
    public void start(Stage stage) throws IOException {
        Manager.addManager(new DatabaseManager(), new UserManager());

        Manager.getManagers().stream()
                .filter(manager -> !manager.getClass().equals(DatabaseManager.class))
                .forEach(Manager::start);
        Manager.getManager(DatabaseManager.class).start();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        root.setOnMousePressed((MouseEvent event) -> {
            setX(event.getSceneX());
            setY(event.getSceneY());
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

            stage.setOpacity(.8);
        });

        root.setOnMouseReleased((MouseEvent event) -> {
            stage.setOpacity(1);
        });

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Agenda de Consultas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}