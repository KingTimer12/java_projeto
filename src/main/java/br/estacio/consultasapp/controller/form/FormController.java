package br.estacio.consultasapp.controller.form;

import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

public abstract class FormController implements Initializable {

    @FXML
    protected AnchorPane main_form;

    @FXML
    protected AnchorPane dashboard_form;

    @FXML
    protected Label lbl_user_id;

    @FXML
    protected Label lbl_user_name;

    @FXML
    protected Label lbl_date_now;

    @FXML
    protected Label lbl_num_pro;

    @FXML
    protected Label lbl_total_appointments;

    @FXML
    protected Label lbl_total_patient;

    @FXML
    protected Label btn_active_patient;

    @FXML
    protected TableColumn<?, ?> dashboard_patient;

    @FXML
    protected TableColumn<?, ?> dashboard_pro;

    @FXML
    protected TableColumn<?, ?> dashboard_stats;

    @FXML
    protected TableView<?> dashboard_table;

    @FXML
    protected Button btn_appointment;

    @FXML
    protected Button btn_config;

    @FXML
    protected Button btn_dashboard;

    @FXML
    protected Button btn_pacient;

    @FXML
    protected Button btn_report;

    @FXML
    protected Circle circle_pfp;

    //Appointment Form - START
    @FXML
    protected AnchorPane appointment_form;

    @FXML
    protected TableColumn<?, ?> appointment_action;

    @FXML
    protected TableColumn<?, ?> appointment_date;

    @FXML
    protected TableColumn<?, ?> appointment_diagnosis;

    @FXML
    protected TableColumn<?, ?> appointment_id;

    @FXML
    protected TableColumn<?, ?> appointment_patient;

    @FXML
    protected TableColumn<?, ?> appointment_profissional;

    @FXML
    protected TableColumn<?, ?> appointment_stats;

    @FXML
    protected TableView<?> appointment_table;

    @FXML
    protected TableColumn<?, ?> appointment_treatment;
    //Appointment Form - END

    //Patient Form -- START
    @FXML
    protected AnchorPane patient_form;

    @FXML
    protected TableColumn<?, ?> patient_action;

    @FXML
    protected TableColumn<?, ?> patient_address;

    @FXML
    protected TableColumn<?, ?> patient_diagnosis;

    @FXML
    protected TableColumn<?, ?> patient_fullname;

    @FXML
    protected TableColumn<?, ?> patient_genre;

    @FXML
    protected TableColumn<?, ?> patient_id;

    @FXML
    protected TableColumn<?, ?> patient_phone;

    @FXML
    protected TableColumn<?, ?> patient_profissional;

    @FXML
    protected TableColumn<?, ?> patient_status;

    @FXML
    protected TableView<?> patient_table;

    @FXML
    protected TableColumn<?, ?> patient_treatment;
    //Patient Form - END

    public void switchForm(ActionEvent event) {
        dashboard_form.setVisible(event.getSource().equals(btn_dashboard));
        appointment_form.setVisible(event.getSource().equals(btn_appointment));
        patient_form.setVisible(event.getSource().equals(btn_pacient));
    }

    public void runTime() {
        new Thread() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
                while (true) {
                    try {
                        Platform.runLater(() -> lbl_date_now.setText(simpleDateFormat.format(new Date())));
                        Thread.sleep(1000);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_user_id.setText(String.valueOf(getUserManager().getId()));
        lbl_user_name.setText(getUserManager().getName());

        runTime();
    }

    public UserManager getUserManager() {
        return Manager.getManager(UserManager.class);
    }

}
