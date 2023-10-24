package br.estacio.consultasapp.controller;

import br.com.timer.objects.DataHandler;
import br.com.timer.objects.rows.Rows;
import br.estacio.consultasapp.Main;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button closeBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    public void close() {
        System.exit(0);
    }

    public void loginAdmin() throws IOException {
        Alert alert;
        if (username.getText().isBlank() || password.getText().isBlank()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Usuário ou senha inválido!");
            alert.setHeaderText(null);
            alert.setContentText("Os campos não podem estar vazios.");
            alert.showAndWait();
            return;
        }

        DataHandler dataHandler = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().fetch().from("user")
                .where(Rows.of("username", username.getText()), Rows.of("password", password.getText())).builder();
        if (!dataHandler.isNext()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Usuário ou senha inválido!");
            alert.setHeaderText(null);
            alert.setContentText("Usuário/senha está incorreto.");
            alert.showAndWait();
            return;
        }
        dataHandler.get("id").ifPresent(row ->
                Manager.getManager(UserManager.class).load(row.asInt(), username.getText(), password.getText()));
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mensagem de Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Login efetuado com sucesso.");
        alert.showAndWait();

        loginBtn.getScene().getWindow().hide();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Agenda de Consultas");
        stage.setScene(scene);
        stage.show();


    }

}
