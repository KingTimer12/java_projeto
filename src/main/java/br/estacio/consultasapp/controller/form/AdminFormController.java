package br.estacio.consultasapp.controller.form;

import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class AdminFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> appointment_action;

    @FXML
    private TableColumn<?, ?> appointment_date;

    @FXML
    private TableColumn<?, ?> appointment_diagnosis;

    @FXML
    private AnchorPane appointment_form;

    @FXML
    private TableColumn<?, ?> appointment_id;

    @FXML
    private TableColumn<?, ?> appointment_patient;

    @FXML
    private TableColumn<?, ?> appointment_profissional;

    @FXML
    private TableColumn<?, ?> appointment_stats;

    @FXML
    private TableView<?> appointment_table;

    @FXML
    private TableColumn<?, ?> appointment_treatment;

    @FXML
    private Label btn_active_patient;

    @FXML
    private Button btn_appointment;

    @FXML
    private Button btn_config;

    @FXML
    private Button btn_dashboard;

    @FXML
    private Button btn_pacient;

    @FXML
    private Button btn_pro;

    @FXML
    private Button btn_report;

    @FXML
    private Circle circle_pfp;

    @FXML
    private TableColumn<?, ?> dashboard_appointment_id;

    @FXML
    private BarChart<?, ?> dashboard_chart_appointment;

    @FXML
    private AreaChart<?, ?> dashboard_chart_patient;

    @FXML
    private TableColumn<?, ?> dashboard_date;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private TableColumn<?, ?> dashboard_patient;

    @FXML
    private TableColumn<?, ?> dashboard_pro;

    @FXML
    private TableColumn<?, ?> dashboard_stats;

    @FXML
    private TableView<?> dashboard_table;

    @FXML
    private Label lbl_user_id;

    @FXML
    private Label lbl_admin_name;

    @FXML
    private Label lbl_date_now;

    @FXML
    private Label lbl_num_pro;

    @FXML
    private Label lbl_total_appointments;

    @FXML
    private Label lbl_total_patient;

    @FXML
    private Label lbl_username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableColumn<?, ?> patient_action;

    @FXML
    private TableColumn<?, ?> patient_address;

    @FXML
    private TableColumn<?, ?> patient_diagnosis;

    @FXML
    private AnchorPane patient_form;

    @FXML
    private TableColumn<?, ?> patient_fullname;

    @FXML
    private TableColumn<?, ?> patient_genre;

    @FXML
    private TableColumn<?, ?> patient_id;

    @FXML
    private TableColumn<?, ?> patient_phone;

    @FXML
    private TableColumn<?, ?> patient_profissional;

    @FXML
    private TableColumn<?, ?> patient_status;

    @FXML
    private TableView<?> patient_table;

    @FXML
    private TableColumn<?, ?> patient_treatment;

    @FXML
    private AnchorPane pro_form;

    @FXML
    private TableView<?> pro_table;

    @FXML
    private TableColumn<?, ?> pro_table_action;

    @FXML
    private TableColumn<?, ?> pro_table_address;

    @FXML
    private TableColumn<?, ?> pro_table_email;

    @FXML
    private TableColumn<?, ?> pro_table_fullname;

    @FXML
    private TableColumn<?, ?> pro_table_genre;

    @FXML
    private TableColumn<?, ?> pro_table_id;

    @FXML
    private TableColumn<?, ?> pro_table_phone;

    @FXML
    private TableColumn<?, ?> pro_table_register_id;

    @FXML
    private TableColumn<?, ?> pro_table_status;

    @FXML
    private Label page_name;

    @FXML
    public void switchForm(ActionEvent event) {
        dashboard_form.setVisible(event.getSource().equals(btn_dashboard));
        pro_form.setVisible(event.getSource().equals(btn_pro));
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

        UserManager userManager = Manager.getManager(UserManager.class);
        lbl_username.setText(userManager.getName());
        lbl_admin_name.setText(userManager.getName());
        lbl_user_id.setText(String.valueOf(userManager.getId()));

        runTime();

    }

}
