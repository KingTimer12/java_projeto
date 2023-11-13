package br.estacio.consultasapp.controller.form;

import br.com.timer.types.MySQL;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.User;
import br.estacio.consultasapp.user.UserManager;
import br.estacio.consultasapp.user.dao.AdminDAO;
import br.estacio.consultasapp.user.dao.AppointmentDAO;
import br.estacio.consultasapp.user.dao.DoctorDAO;
import br.estacio.consultasapp.user.enums.Consults;
import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.enums.Status;
import br.estacio.consultasapp.user.interfaces.IdInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
    protected BarChart<String, Integer> dashboard_chart_appointment;

    @FXML
    protected AreaChart<String, Integer> dashboard_chart_patient;

    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_date;


    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_patient;

    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_pro;

    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_stats;

    @FXML
    protected TableColumn<AppointmentDAO, String> dashboard_day;

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

    protected ObservableList<String> typeList() {
        List<String> days = new ArrayList<>(Stream.of(Consults.values()).map(Consults::getName).toList());
        return FXCollections.observableList(days);
    }

    protected ObservableList<String> genreList() {
        List<String> days = new ArrayList<>(Stream.of(Genders.values()).map(Genders::getName).toList());
        return FXCollections.observableList(days);
    }

    protected String generateId(String idPrefix, List<? extends IdInterface> list) {
        String id = idPrefix+"-"+generateRandomID();
        while (true) {
            boolean found = false;
            for (IdInterface users : list) {
                if (users.getOtherId() == null) continue;
                if (users.getOtherId().equalsIgnoreCase(id)){
                    id = idPrefix+"-"+generateRandomID();
                    found = true;
                    break;
                }
            }
            if (!found) break;
        }
        return id;
    }

    protected void dashboardDisplayData(AdminDAO adminDAO) {
        ObservableList<AppointmentDAO> dashboardGetData = FXCollections.observableList(adminDAO.getAppointments());
        dashboard_appointment_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        dashboard_patient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dashboard_pro.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        dashboard_date.setCellValueFactory(new PropertyValueFactory<>("consultationHour"));
        dashboard_stats.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));
        dashboard_day.setCellValueFactory(param -> new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy").format(param.getValue().getConsultationDate())));
        dashboard_table.setItems(dashboardGetData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbl_user_id.setText(String.valueOf(getUserManager().getId()));
        lbl_user_name.setText(getUserManager().getName());

        dashboardNOA();
        dashboardNOP();

        runTime();
    }

    protected void dashboardNOP() {
        dashboard_chart_patient.getData().clear();

        UserManager userManager = getUserManager();
        StringBuilder sql = new StringBuilder("SELECT createdAt, COUNT(id) FROM patients ");
        if (userManager.getUser() instanceof DoctorDAO) {
            sql.append("doctor_id='").append(userManager.getUser().getId()).append("'");
        }
        sql.append(" GROUP BY TIMESTAMP(createdAt) ASC LIMIT 8");

        MySQL mySQL = Manager.getManager(DatabaseManager.class).getMySQL().getHandler();
        mySQL.openConnection();
        Connection connect = mySQL.getConnection();
        XYChart.Series<String, Integer> chart = new XYChart.Series<>();

        try (PreparedStatement prepare = connect.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }

            dashboard_chart_patient.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mySQL.closeConnection();
        }

    }

    protected void dashboardNOA() {
        dashboard_chart_appointment.getData().clear();

        UserManager userManager = getUserManager();
        StringBuilder sql = new StringBuilder("SELECT createdAt, COUNT(id) FROM appointments ");
        if (userManager.getUser() instanceof DoctorDAO) {
            sql.append("doctor_id='").append(userManager.getUser().getId()).append("'");
        }
        sql.append(" GROUP BY TIMESTAMP(createdAt) ASC LIMIT 7");
        MySQL mySQL = Manager.getManager(DatabaseManager.class).getMySQL().getHandler();
        mySQL.openConnection();
        Connection connect = mySQL.getConnection();
        XYChart.Series<String, Integer> chart = new XYChart.Series<>();

        try (PreparedStatement prepare = connect.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {
            ResultSet result = prepare.executeQuery();
            while (result.next()) {
                chart.getData().add(
                        new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }
            dashboard_chart_appointment.getData().add(chart);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mySQL.closeConnection();
        }

    }

    public UserManager getUserManager() {
        return Manager.getManager(UserManager.class);
    }

}
