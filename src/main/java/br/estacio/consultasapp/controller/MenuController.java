package br.estacio.consultasapp.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Label welcomeText;

    @FXML
    private Label subtitleText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeInWelcome = new FadeTransition(Duration.millis(1500), welcomeText);
        fadeInWelcome.setFromValue(0.0);
        fadeInWelcome.setToValue(1.0);
        fadeInWelcome.play();

        FadeTransition fadeInSub = new FadeTransition(Duration.millis(2000), subtitleText);
        fadeInSub.setFromValue(0.0);
        fadeInSub.setToValue(1.0);
        fadeInSub.play();
    }

}
