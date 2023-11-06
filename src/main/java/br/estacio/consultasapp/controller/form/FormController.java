package br.estacio.consultasapp.controller.form;

import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import br.estacio.consultasapp.user.dao.AdminDAO;
import br.estacio.consultasapp.user.dao.AppointmentDAO;
import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.enums.Status;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

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
    protected Label lbl_active_patient;

    @FXML
    protected TableColumn<AppointmentDAO, Integer> dashboard_appointment_id;

    @FXML
    protected BarChart<?, ?> dashboard_chart_appointment;

    @FXML
    protected AreaChart<?, ?> dashboard_chart_patient;

    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_date;


    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_patient;

    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_pro;

    @FXML
    protected TableColumn<AppointmentDAO, Status> dashboard_stats;

    @FXML
    protected TableView<AppointmentDAO> dashboard_table;

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

    protected void switchForm(ActionEvent event) {
        dashboard_form.setVisible(event.getSource().equals(btn_dashboard));
        appointment_form.setVisible(event.getSource().equals(btn_appointment));
        patient_form.setVisible(event.getSource().equals(btn_pacient));
    }

    protected void runTime() {
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

    protected String generateRandomID() {
        Random random = new Random();
        int min = 10000;
        int max = 99999;
        int randomID = random.nextInt(max - min + 1) + min;
        return String.format("%05d", randomID);
    }

    protected List<String> getDailyTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Calendar endOfDay = Calendar.getInstance();
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);

        while (calendar.before(endOfDay)) {
            timeSlots.add(timeFormat.format(calendar.getTime()));
            calendar.add(Calendar.MINUTE, 30);
        }

        return timeSlots;
    }

    protected ObservableList<String> genreList() {
        List<String> days = new ArrayList<>(Stream.of(Genders.values()).map(Genders::getName).toList());
        return FXCollections.observableList(days);
    }

    protected void dashboardDisplayData(AdminDAO adminDAO) {
        ObservableList<AppointmentDAO> dashboardGetData = FXCollections.observableList(adminDAO.getAppointments());
        dashboard_appointment_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        dashboard_patient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dashboard_pro.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        dashboard_date.setCellValueFactory(new PropertyValueFactory<>("consultationHour"));
        dashboard_stats.setCellValueFactory(new PropertyValueFactory<>("status"));
        dashboard_table.setItems(dashboardGetData);
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
