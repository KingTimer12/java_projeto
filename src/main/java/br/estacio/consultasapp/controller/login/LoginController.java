package br.estacio.consultasapp.controller.login;

import br.estacio.consultasapp.controller.GuiManager;
import br.estacio.consultasapp.controller.IClose;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.utils.UsersPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class LoginController implements ILogin, IClose, Initializable {

    @FXML
    protected Button login_btn;

    @FXML
    protected CheckBox login_checkbox;

    @FXML
    protected AnchorPane login_form;

    @FXML
    private ComboBox<String> login_panel;

    @FXML
    protected PasswordField login_password;

    @FXML
    protected TextField login_showPassword;

    @FXML
    protected TextField login_username;

    @Override
    public void close() {
        System.exit(0);
    }

    @FXML
    public void loginShowPassword() {
        if (login_checkbox.isSelected())
            login_showPassword.setText(login_password.getText());
        else login_password.setText(login_showPassword.getText());

        login_showPassword.setVisible(!login_showPassword.isVisible());
        login_password.setVisible(!login_password.isVisible());
    }

    public void userList() {
        List<String> listU = new ArrayList<>(List.of(UsersPanel.USERS));
        ObservableList<String> listData = FXCollections.observableList(listU);
        login_panel.setItems(listData);
    }

    @FXML
    public void switchPanel() throws IOException {
        GuiManager guiManager = Manager.getManager(GuiManager.class);
        if (Objects.equals(login_panel.getSelectionModel().getSelectedItem(), "Portal do Admin")) {
            guiManager.openGui("adminLogin", StageStyle.TRANSPARENT);
        } else if (Objects.equals(login_panel.getSelectionModel().getSelectedItem(), "Portal do Profissional")) {
            guiManager.openGui("doctorLogin", StageStyle.TRANSPARENT);
        } else if (Objects.equals(login_panel.getSelectionModel().getSelectedItem(), "Portal da Secretária")) {
            guiManager.openGui("secretaryLogin", StageStyle.TRANSPARENT);
        }
        login_panel.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList();
    }

}
