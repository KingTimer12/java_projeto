package br.estacio.consultasapp.controller.login.types;

import br.com.timer.objects.data.DataHandler;
import br.com.timer.objects.data.types.FetchData;
import br.com.timer.objects.rows.Rows;
import br.estacio.consultasapp.controller.GuiManager;
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

import java.io.IOException;

public class DoctorLogin extends LoginController {

    @FXML
    @Override
    public void login() throws IOException {
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
                .fetch().from(DoctorDAO.class)
                .where(Rows.of("doctor_id", login_username.getText()), Rows.of("password", login_password.getText()))
                .builder();
        if (!dataHandler.isNext()) {
            AlertMessage.errorMessage(error_message_password);
            return;
        }

        UserManager userManager = Manager.getManager(UserManager.class);

        dataHandler.get("id").ifPresent(row -> {
            DoctorDAO user = DoctorDAO.builder().id(row.asInt()).build();
            user.load();
            if (!user.getStatus().equals(Status.DELETED))
                userManager.setUser(user);
        });

        if (userManager.getUser() == null) {
            AlertMessage.errorMessage("Conta deletada ou inexistente.");
            return;
        }

        AlertMessage.successMessage("Login efetuado com sucesso.");
        Manager.getManager(GuiManager.class).openGui("doctorMainForm");
        login_btn.getScene().getWindow().hide();
    }

}
