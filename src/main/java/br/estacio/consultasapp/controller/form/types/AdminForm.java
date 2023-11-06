package br.estacio.consultasapp.controller.form.types;

import br.com.timer.objects.data.types.ListData;
import br.estacio.consultasapp.controller.form.AdminFormController;
import br.estacio.consultasapp.controller.form.FormController;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.UserManager;
import br.estacio.consultasapp.user.dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminForm extends FormController {

    @FXML
    private Button pro_add_btn;

    @FXML
    private Button pro_back_btn;

    @FXML
    private TextField pro_email_textfield;

    @FXML
    private AnchorPane pro_form;

    @FXML
    private TextField pro_fullname_textfield;

    @FXML
    private ComboBox<?> pro_genre_combobox;

    @FXML
    private AnchorPane pro_list_form;

    @FXML
    private Button pro_new_btn;

    @FXML
    private AnchorPane pro_new_form;

    @FXML
    private TextField pro_password_textfield;

    @FXML
    private Button pro_refresh_btn;

    @FXML
    private TextField pro_register_id_textfield;

    @FXML
    private Button pro_search_btn;

    @FXML
    private TextField pro_search_textfield;

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
    private Button pro_day_add_btn;

    @FXML
    private ComboBox<?> pro_day_combobox;

    @FXML
    private Button pro_day_remove_btn;

    @FXML
    private TableView<?> pro_days_table;

    @FXML
    private TableColumn<?, ?> pro_monday_table;

    @FXML
    private TableColumn<?, ?> pro_tuesday_table;

    @FXML
    private TableColumn<?, ?> pro_wednesday_table;

    @FXML
    private TableColumn<?, ?> pro_thursday_table;

    @FXML
    private TableColumn<?, ?> pro_friday_table;

    @FXML
    private Button btn_pro;

    //Secretary -- START
    @FXML
    private Button btn_secretary;

    @FXML
    private Button secretary_add_btn;

    @FXML
    private Button secretary_back_btn;

    @FXML
    private TextField secretary_email_textfield;

    @FXML
    private AnchorPane secretary_form;

    @FXML
    private TextField secretary_fullname_textfield;

    @FXML
    private ComboBox<?> secretary_genre_combobox;

    @FXML
    private AnchorPane secretary_list_form;

    @FXML
    private Button secretary_new_btn;

    @FXML
    private AnchorPane secretary_new_form;

    @FXML
    private TextField secretary_password_textfield;

    @FXML
    private Button secretary_refresh_btn;

    @FXML
    private TextField secretary_register_id_textfield;

    @FXML
    private Button secretary_search_btn;

    @FXML
    private TextField secretary_search_textfield;

    @FXML
    private TableView<?> secretary_table;

    @FXML
    private TableColumn<?, ?> secretary_table_action;

    @FXML
    private TableColumn<?, ?> secretary_table_address;

    @FXML
    private TableColumn<?, ?> secretary_table_email;

    @FXML
    private TableColumn<?, ?> secretary_table_fullname;

    @FXML
    private TableColumn<?, ?> secretary_table_genre;

    @FXML
    private TableColumn<?, ?> secretary_table_id;

    @FXML
    private TableColumn<?, ?> secretary_table_phone;

    @FXML
    private TableColumn<?, ?> secretary_table_register_id;

    @FXML
    private TableColumn<?, ?> secretary_table_status;
    //Secretary -- END

    //Appointment -- START
    @FXML
    private TableColumn<?, ?> appointment_action;

    @FXML
    private Button appointment_back_btn;

    @FXML
    private TableColumn<?, ?> appointment_date;

    @FXML
    private ComboBox<?> appointment_hour_dropdown;

    @FXML
    private TableColumn<?, ?> appointment_id;

    @FXML
    private AnchorPane appointment_list_form;

    @FXML
    private Button appointment_mark_btn;

    @FXML
    private Button appointment_new_btn;

    @FXML
    private AnchorPane appointment_new_form;

    @FXML
    private TableColumn<?, ?> appointment_patient;

    @FXML
    private TextField appointment_patient_textfield;

    @FXML
    private ComboBox<?> appointment_pro_dropdown;

    @FXML
    private TableColumn<?, ?> appointment_profissional;

    @FXML
    private Button appointment_refresh_btn;

    @FXML
    private DatePicker appointment_schedule_date;

    @FXML
    private Button appointment_search_btn;

    @FXML
    private TextField appointment_search_textfield;

    @FXML
    private TableColumn<?, ?> appointment_stats;

    @FXML
    private TableView<?> appointment_table;

    @FXML
    private ComboBox<?> appointment_type_dropdown;

    @FXML
    private TableColumn<?, ?> patient_action;

    @FXML
    private Button patient_add_btn;

    @FXML
    private AnchorPane patient_new_form;

    @FXML
    private TableColumn<?, ?> patient_address;

    @FXML
    private Label patient_address_lbl;

    @FXML
    private TextField patient_address_textfield;

    @FXML
    private Label patient_age_lbl;

    @FXML
    private Button patient_back_btn;

    @FXML
    private DatePicker patient_birthday_date;

    @FXML
    private DatePicker patient_conclusion_date;

    @FXML
    private Button patient_confirm_btn;

    @FXML
    private Label patient_contact_lbl;

    @FXML
    private TextField patient_contact_textfield;

    @FXML
    private TableColumn<?, ?> patient_diagnosis;

    @FXML
    private TextArea patient_feedback_textfield;

    @FXML
    private AnchorPane patient_form;

    @FXML
    private TableColumn<?, ?> patient_fullname;

    @FXML
    private TableColumn<?, ?> patient_genre;

    @FXML
    private ComboBox<?> patient_genre_dropdown;

    @FXML
    private Label patient_genre_lbl;

    @FXML
    private TableColumn<?, ?> patient_id;

    @FXML
    private Label patient_id_lbl;

    @FXML
    private Label patient_last_lbl;

    @FXML
    private DatePicker patient_last_update_date;

    @FXML
    private AnchorPane patient_list_form;

    @FXML
    private Label patient_name_lbl;

    @FXML
    private TextField patient_name_textfield;

    @FXML
    private Button patient_new_btn;

    @FXML
    private TextArea patient_observations_textfield;

    @FXML
    private TableColumn<?, ?> patient_phone;

    @FXML
    private TextArea patient_plan_textfield;

    @FXML
    private TableColumn<?, ?> patient_profissional;

    @FXML
    private TextArea patient_progress_textfield;

    @FXML
    private Button patient_refresh_btn;

    @FXML
    private Label patient_responsable_lbl;

    @FXML
    private TextField patient_responsible_textfield;

    @FXML
    private Button patient_search_btn;

    @FXML
    private TextField patient_search_textfield;

    @FXML
    private TableColumn<?, ?> patient_status;

    @FXML
    private TableView<?> patient_table;

    @FXML
    private TextArea patient_tests_textfield;

    @FXML
    private TableColumn<?, ?> patient_treatment;


    @FXML
    private Label lbl_username;

    @Override
    public void switchForm(ActionEvent event) {
        super.switchForm(event);
        pro_form.setVisible(event.getSource().equals(btn_pro));
        secretary_form.setVisible(event.getSource().equals(btn_secretary));
        update(true);
    }

    private void update(boolean verify) {
        AdminDAO adminDAO = (AdminDAO) Manager.getManager(UserManager.class).getUser();
        if (adminDAO == null)
            throw new NullPointerException("AdminDAO não pode ser null.");
        if (!adminDAO.isLoaded())
            adminDAO.load();
        updateDoctors(adminDAO, !verify || pro_form.isVisible() || dashboard_form.isVisible());
        updateSecretaries(adminDAO, !verify || secretary_form.isVisible() || dashboard_form.isVisible());
        updatePatients(adminDAO, !verify || patient_form.isVisible() || dashboard_form.isVisible());
        updateAppointment(adminDAO, !verify || appointment_form.isVisible() || dashboard_form.isVisible());
    }

    private void updateSecretaries(AdminDAO adminDAO, boolean secretary_form) {
        if (!adminDAO.getSecretaries().isEmpty())
            adminDAO.getSecretaries().clear();
        if (!secretary_form) return;
        ListData listData = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().list()
                .from(SecretaryDAO.class).builder();
        final List<SecretaryDAO> secretaries = new ArrayList<>();
        listData.get("id").ifPresent(list -> secretaries.addAll(list.stream().map(data -> {
            SecretaryDAO secretary = SecretaryDAO.builder().id(data.asInt()).build();
            secretary.load();
            return secretary;
        }).toList()));
        adminDAO.getSecretaries().addAll(secretaries);
        secretaries.clear();
    }

    private void updateDoctors(AdminDAO adminDAO, boolean doctor_form) {
        if (!adminDAO.getDoctors().isEmpty())
            adminDAO.getDoctors().clear();
        if (!doctor_form)
            return;
        ListData listData = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().list()
                .from(DoctorDAO.class).builder();
        final List<DoctorDAO> doctors = new ArrayList<>();
        listData.get("id").ifPresent(list -> doctors.addAll(list.stream().map(data -> {
            DoctorDAO doctor = DoctorDAO.builder().id(data.asInt()).build();
            doctor.load();
            return doctor;
        }).toList()));
        adminDAO.getDoctors().addAll(doctors);
        doctors.clear();
    }

    private void updatePatients(AdminDAO adminDAO, boolean patient_form) {
        if (!adminDAO.getPatients().isEmpty())
            adminDAO.getPatients().clear();
        if (!patient_form) return;
        ListData listData = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().list()
                .from(PatientDAO.class).builder();
        final List<PatientDAO> patients = new ArrayList<>();
        listData.get("id").ifPresent(list -> patients.addAll(list.stream().map(data -> {
            PatientDAO patient = PatientDAO.builder().id(data.asInt()).build();
            patient.load();
            return patient;
        }).toList()));
        adminDAO.getPatients().addAll(patients);
        patients.clear();
    }

    private void updateAppointment(AdminDAO adminDAO, boolean appointment_form) {
        if (!adminDAO.getAppointments().isEmpty())
            adminDAO.getAppointments().clear();
        if (!appointment_form) return;
        ListData listData = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().list()
                .from(AppointmentDAO.class).builder();
        final List<AppointmentDAO> appointments = new ArrayList<>();
        listData.get("id").ifPresent(list -> appointments.addAll(list.stream().map(data -> {
            AppointmentDAO appointment = AppointmentDAO.builder().id(data.asInt()).build();
            appointment.load();
            return appointment;
        }).toList()));
        adminDAO.getAppointments().addAll(appointments);
        appointments.clear();
    }

    public void showAndHideList(ActionEvent event) {
        if (event.getSource().equals(pro_back_btn) || event.getSource().equals(pro_new_btn)) {
            pro_list_form.setVisible(event.getSource().equals(pro_back_btn));
            pro_new_form.setVisible(event.getSource().equals(pro_new_btn));
        } else if (event.getSource().equals(secretary_back_btn) || event.getSource().equals(secretary_new_btn)) {
            secretary_list_form.setVisible(event.getSource().equals(secretary_back_btn));
            secretary_new_form.setVisible(event.getSource().equals(secretary_new_btn));
        } else if (event.getSource().equals(appointment_back_btn) || event.getSource().equals(appointment_new_btn)) {
            appointment_list_form.setVisible(event.getSource().equals(appointment_back_btn));
            appointment_new_form.setVisible(event.getSource().equals(appointment_new_btn));
        } else if (event.getSource().equals(patient_back_btn) || event.getSource().equals(patient_new_btn)) {
            patient_list_form.setVisible(event.getSource().equals(patient_back_btn));
            patient_new_form.setVisible(event.getSource().equals(patient_new_btn));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        lbl_username.setText(getUserManager().getName());
        update(false);
    }
}
