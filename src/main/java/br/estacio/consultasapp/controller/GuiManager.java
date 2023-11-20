package br.estacio.consultasapp.controller;

import br.estacio.consultasapp.Main;
import br.estacio.consultasapp.handler.Manager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;

import java.io.IOException;

@Setter
public class GuiManager extends Manager {

    private double x;
    private double y;

    @Override
    public void start() {
        try {
            openGui("adminLogin", StageStyle.TRANSPARENT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openGui(String sceneName) throws IOException {
        openGui(sceneName, StageStyle.TRANSPARENT);
    }

    public void openGui(String sceneName, StageStyle stageStyle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(sceneName + ".fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();

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

        stage.setTitle("Agenda de Consultas");
        stage.initStyle(stageStyle);
        stage.setScene(new Scene(root));
        stage.show();
        stage.setOnCloseRequest(event -> {
            System.exit(0);
            event.consume();
        });
    }


}
