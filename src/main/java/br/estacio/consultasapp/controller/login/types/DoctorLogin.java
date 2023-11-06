package br.estacio.consultasapp.controller.login.types;

import br.com.timer.objects.data.DataHandler;
import br.com.timer.objects.data.types.FetchData;
import br.com.timer.objects.rows.Rows;
import br.estacio.consultasapp.controller.login.LoginController;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import br.estacio.consultasapp.user.dao.DoctorDAO;
import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.enums.Status;
import br.estacio.consultasapp.utils.AlertMessage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class DoctorLogin extends LoginController {

    @FXML
    private Button register_btn;

    @FXML
    private CheckBox register_checkbox;

    @FXML
    private TextField register_email;

    @FXML
    private AnchorPane register_form;

    @FXML
    private TextField register_fullname;

    @FXML
    private Hyperlink register_loginBtn;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_showPassword;

    @FXML
    private TextField register_username;

    @FXML
    @Override
    public void login() {
        if (!login_showPassword.getText().equals(login_password.getText()))
            if (!login_showPassword.isVisible())
                login_showPassword.setText(login_password.getText());
            else
                login_password.setText(login_showPassword.getText());

        String error_message_password = "Usuário/Senha incorretos.";

        if (login_username.getText().isBlank() || login_password.getText().isBlank()) {
            AlertMessage.errorMessage(error_message_password);
            return;
        }

        FetchData dataHandler = Manager.getManager(DatabaseManager.class).getMySQL()
                .getHandler()
                .fetch().from("doctors")
                .where(Rows.of("username", login_username.getText()), Rows.of("password", login_password.getText()))
                .builder();
        if (!dataHandler.isNext()) {
            AlertMessage.errorMessage(error_message_password);
            return;
        }

        UserManager userManager = Manager.getManager(UserManager.class);

        dataHandler.get("id").ifPresent(row -> {
            DoctorDAO user = DoctorDAO.builder().id(row.asInt()).build();
            user.load();
            userManager.setUser(user);
        });

        AlertMessage.successMessage("Login efetuado com sucesso.");

        login_btn.getScene().getWindow().hide();
    }

    @FXML
    public void register() {
        if (!register_showPassword.getText().equals(register_password.getText()))
            if (!register_showPassword.isVisible())
                register_showPassword.setText(register_password.getText());
            else
                register_password.setText(register_showPassword.getText());

        if (register_username.getText().isBlank() || register_password.getText().isBlank()
                || register_email.getText().isBlank() || register_fullname.getText().isBlank()) {
            AlertMessage.errorMessage("Preencha todos os campos.");
            return;
        }

        DataHandler dataHandler = Manager.getManager(DatabaseManager.class).getMySQL()
                .getHandler()
                .fetch().from("doctors")
                .where(Rows.of("username", register_username.getText())).builder();
        if (dataHandler.isNext()) {
            AlertMessage.errorMessage("Usuário já existe.");
            return;
        }

        if (register_password.getText().length() < 8) {
            AlertMessage.errorMessage("Senha fraca. Tenha pelo menos 8 caracteres.");
            return;
        }

        DoctorDAO doctor = DoctorDAO.builder()
                .email(register_email.getText())
                .username(register_username.getText())
                .fullName(register_fullname.getText())
                .password(register_password.getText())
                .gender(Genders.NONE)
                .status(Status.CONFIRM)
                .build();
        doctor.save();
        AlertMessage.successMessage("Conta registrada, agora espere a confirmação do administrador.");
    }

    @Override
    public void clearFields() {
        super.clearFields();
        register_fullname.clear();
        register_password.clear();
        register_email.clear();
        register_username.clear();
        register_showPassword.clear();
    }

    @FXML
    public void registerShowPassword() {
        if (register_checkbox.isSelected())
            register_showPassword.setText(register_password.getText());
        else register_password.setText(register_showPassword.getText());

        register_showPassword.setVisible(!register_showPassword.isVisible());
        register_password.setVisible(!register_password.isVisible());
    }

    @FXML
    public void switchForm() {
        clearFields();
        login_form.setVisible(!login_form.isVisible());
        register_form.setVisible(!register_form.isVisible());
    }
}
