package br.estacio.consultasapp.controller.form.types;

import br.com.timer.objects.data.types.ListData;
import br.estacio.consultasapp.controller.form.FormController;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.dao.*;
import br.estacio.consultasapp.user.enums.*;
import br.estacio.consultasapp.utils.AlertMessage;
import br.estacio.consultasapp.utils.WeeklySchedule;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

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
    private ComboBox<String> pro_genre_combobox;

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
    private TableView<DoctorDAO> pro_table;

    @FXML
    private TableColumn<?, ?> pro_table_action;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_address;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_email;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_fullname;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_genre;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_id;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_phone;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_register_id;

    @FXML
    private TableColumn<DoctorDAO, String> pro_table_status;

    @FXML
    private Button pro_day_add_btn;

    @FXML
    private ComboBox<String> pro_hour_combobox;

    @FXML
    private ComboBox<String> pro_day_combobox;

    @FXML
    private Button pro_day_remove_btn;

    @FXML
    private TableView<WeeklySchedule> pro_days_table;

    @FXML
    private TableColumn<WeeklySchedule, String> pro_monday_table;

    @FXML
    private TableColumn<WeeklySchedule, String> pro_tuesday_table;

    @FXML
    private TableColumn<WeeklySchedule, String> pro_wednesday_table;

    @FXML
    private TableColumn<WeeklySchedule, String> pro_thursday_table;

    @FXML
    private TableColumn<WeeklySchedule, String> pro_friday_table;

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
    private ComboBox<String> secretary_genre_combobox;

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
    private TableView<SecretaryDAO> secretary_table;

    @FXML
    private TableColumn<?, ?> secretary_table_action;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_address;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_email;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_fullname;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_genre;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_id;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_phone;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_register_id;

    @FXML
    private TableColumn<SecretaryDAO, String> secretary_table_status;
    //Secretary -- END

    //Appointment -- START
    @FXML
    private TableColumn<?, ?> appointment_action;

    @FXML
    private Button appointment_back_btn;

    @FXML
    private TableColumn<AppointmentDAO, String> appointment_hour;

    @FXML
    private ComboBox<String> appointment_hour_dropdown;

    @FXML
    private TableColumn<AppointmentDAO, String> appointment_id;

    @FXML
    private AnchorPane appointment_list_form;

    @FXML
    private Button appointment_mark_btn;

    @FXML
    private Button appointment_confirm_btn;

    @FXML
    private Button appointment_new_btn;

    @FXML
    private AnchorPane appointment_new_form;

    @FXML
    private TableColumn<AppointmentDAO, String> appointment_patient;

    @FXML
    private TextField appointment_patient_textfield;

    @FXML
    private TextField appointment_pro_textfield;

    @FXML
    private TableColumn<AppointmentDAO, String> appointment_profissional;

    @FXML
    private Button appointment_refresh_btn;

    @FXML
    private DatePicker appointment_schedule_date;

    @FXML
    private Button appointment_search_btn;

    @FXML
    private TextField appointment_search_textfield;

    @FXML
    private TableColumn<AppointmentDAO, String> appointment_stats;

    @FXML
    private TableColumn<AppointmentDAO, String> appointment_schedule;

    @FXML
    private TableColumn<AppointmentDAO, String> appointment_consult;

    @FXML
    private TableView<AppointmentDAO> appointment_table;

    @FXML
    private ComboBox<String> appointment_type_dropdown;

    @FXML
    private TableColumn<?, ?> patient_action;

    @FXML
    private Button patient_add_btn;

    @FXML
    private AnchorPane patient_new_form;

    @FXML
    private TableColumn<PatientDAO, String> patient_address;

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
    private TableColumn<PatientDAO, String> patient_diagnosis;

    @FXML
    private TextArea patient_feedback_textfield;

    @FXML
    private AnchorPane patient_form;

    @FXML
    private TableColumn<PatientDAO, String> patient_fullname;

    @FXML
    private TableColumn<PatientDAO, String> patient_genre;

    @FXML
    private ComboBox<String> patient_genre_dropdown;

    @FXML
    private Label patient_genre_lbl;

    @FXML
    private TableColumn<PatientDAO, String> patient_id;

    @FXML
    private TableColumn<PatientDAO, String> patient_register_id;

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
    private TableColumn<PatientDAO, String> patient_phone;

    @FXML
    private TextArea patient_plan_textfield;

    @FXML
    private TableColumn<PatientDAO, String> patient_profissional;

    @FXML
    private TextArea patient_progress_textfield;

    @FXML
    private Button patient_refresh_btn;

    @FXML
    private Label patient_responsable_lbl;

    @FXML
    private ComboBox<String> patient_responsible_dropdown;

    @FXML
    private Button patient_search_btn;

    @FXML
    private TextField patient_search_textfield;

    @FXML
    private TableColumn<PatientDAO, String> patient_status;

    @FXML
    private TableView<PatientDAO> patient_table;

    @FXML
    private TextArea patient_tests_textfield;

    @FXML
    private TableColumn<PatientDAO, String> patient_treatment;


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
        AdminDAO adminDAO = getAdmin();
        updateDoctors(adminDAO, !verify || pro_form.isVisible() || dashboard_form.isVisible());
        updateSecretaries(adminDAO, !verify || secretary_form.isVisible() || dashboard_form.isVisible());
        updatePatients(adminDAO, !verify || patient_form.isVisible() || dashboard_form.isVisible());
        updateAppointment(adminDAO, !verify || appointment_form.isVisible() || dashboard_form.isVisible());

        lbl_num_pro.setText(String.valueOf(adminDAO.getDoctors().size()));
        lbl_total_patient.setText(String.valueOf(adminDAO.getPatients().size()));
        lbl_active_patient.setText(String.valueOf(adminDAO.getPatients().stream()
                .filter(patient -> patient.getStatus().equals(Status.ACTIVE)).toList().size()));
        lbl_total_appointments.setText(String.valueOf(adminDAO.getAppointments().size()));

        if (pro_form.isVisible()) {
            doctorsDisplayData(adminDAO);
        }
        if (patient_form.isVisible()) {
            patientDisplayData(adminDAO);
        }
        if (secretary_form.isVisible()) {
            secretaryDisplayData(adminDAO);
        }
        if (appointment_form.isVisible()) {
            appointmentDisplayData(adminDAO);
        }
        if (dashboard_form.isVisible()) {
            dashboardDisplayData(adminDAO);
            dashboardNOA();
            dashboardNOP();
        }
    }

    @FXML
    public void newHour() {
        AdminDAO adminDAO = getAdmin();
        String doctorId = pro_register_id_textfield.getText();
        DoctorDAO doctor = adminDAO.getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getDoctorId().equalsIgnoreCase(doctorId))
                .findAny().orElse(DoctorDAO.builder().doctorId(doctorId).build());

        adminDAO.getDoctors().remove(doctor);

        doctor.loadDoctorId();

        Days day = Arrays.stream(Days.values())
                .filter(d -> d.getDayInPT().equalsIgnoreCase(pro_day_combobox.getSelectionModel().getSelectedItem()))
                .findAny().orElse(Days.MONDAY);
        String hour = pro_hour_combobox.getSelectionModel().getSelectedItem();
        if (!doctor.getAvailable().get(day).contains(hour))
            doctor.getAvailable().get(day).add(hour);

        adminDAO.getDoctors().add(doctor);
        listHourDoctor(adminDAO);
    }

    @FXML
    public void removeHour() {
        AdminDAO adminDAO = getAdmin();
        String doctorId = pro_register_id_textfield.getText();
        DoctorDAO doctor = adminDAO.getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getDoctorId().equalsIgnoreCase(doctorId))
                .findAny().orElse(DoctorDAO.builder().doctorId(doctorId).build());

        adminDAO.getDoctors().remove(doctor);

        doctor.loadDoctorId();

        Days day = Arrays.stream(Days.values())
                .filter(d -> d.getDayInPT().equalsIgnoreCase(pro_day_combobox.getSelectionModel().getSelectedItem()))
                .findAny().orElse(Days.MONDAY);
        String hour = pro_hour_combobox.getSelectionModel().getSelectedItem();
        if (!doctor.getAvailable().get(day).contains(hour)) {
            AlertMessage.errorMessage("Não há mais esse horário para esse dia");
            adminDAO.getDoctors().add(doctor);
            return;
        }
        doctor.getAvailable().get(day).remove(hour);

        adminDAO.getDoctors().add(doctor);
        listHourDoctor(adminDAO);
    }

    @FXML
    public void confirmPatientAdd() {
        String fullName = patient_name_textfield.getText();
        LocalDate birth_localdate = patient_birthday_date.getValue();
        Genders gender = Genders.getGender(patient_genre_dropdown.getSelectionModel().getSelectedItem());

        if (fullName.isBlank() || birth_localdate == null || gender.equals(Genders.NONE)) {
            AlertMessage.errorMessage("Preencha todos os campos obrigatórios.");
            return;
        }

        Date birth_date = Date.from(birth_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant());

        LocalDate currentDate = LocalDate.now();
        LocalDate birthLocalDate = birth_date.toInstant().atZone(ZoneId.of("GMT-3")).toLocalDate();
        Period period = Period.between(birthLocalDate, currentDate);
        int age = period.getYears();

        String responsible = patient_responsible_dropdown.getSelectionModel().getSelectedItem();
        String mobile_phone = patient_contact_textfield.getText();
        String address = patient_address_textfield.getText();

        LocalDate last_update_localdate = patient_last_update_date.getValue();
        if (last_update_localdate == null) patient_last_lbl.setText("------------");
        else {
            Date last_update_date = Date.from(last_update_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            patient_last_lbl.setText(simpleDateFormat.format(last_update_date));
        }

        patient_id_lbl.setText(generateId("PID", getAdmin().getPatients()));
        patient_age_lbl.setText(age + " anos");
        patient_responsable_lbl.setText((responsible == null || responsible.isBlank()) ? "------------" : responsible);
        patient_name_lbl.setText(fullName);
        patient_genre_lbl.setText(gender.getName());
        patient_contact_lbl.setText(mobile_phone.isBlank() ? "Não informado" : mobile_phone);
        patient_address_lbl.setText(address.isBlank() ? "Não informado" : address);
    }

    @FXML
    public void addPatientButton() {
        final String patientId = patient_id_lbl.getText();
        if (patientId.equals("------------")) {
            AlertMessage.errorMessage("Você precisa confirmar as informações antes de adicionar.");
            return;
        }

        LocalDate last_update_localdate = patient_last_update_date.getValue();
        Date last_update_date = last_update_localdate != null ? Date.from(last_update_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant()) : null;

        LocalDate conclusion_localdate = patient_conclusion_date.getValue();
        Date conclusion_date = conclusion_localdate != null ? Date.from(conclusion_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant()) : null;

        String fullName = patient_name_textfield.getText();
        LocalDate birth_localdate = patient_birthday_date.getValue();
        Date birthday = Date.from(birth_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant());
        Genders gender = Genders.getGender(patient_genre_dropdown.getSelectionModel().getSelectedItem());
        Optional<Integer> doctorId = getAdmin().getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getFullName().equalsIgnoreCase(patient_responsable_lbl.getText()))
                .findAny().map(DoctorDAO::getId);
        String mobile_phone = patient_contact_textfield.getText();
        String address = patient_address_textfield.getText();
        AdminDAO adminDAO = getAdmin();
        PatientDAO patientDAO = PatientDAO.builder()
                .patientId(patientId)
                .fullName(fullName)
                .gender(gender)
                .mobileNumber(mobile_phone)
                .birthday(birthday)
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(Status.ACTIVE)
                .doctorId(doctorId.orElse(0))
                .address(address).build();
        patientDAO.save();

        DiagnosisDAO diagnosisDAO = patientDAO.saveDiagnosis(patient_observations_textfield.getText(),
                patient_tests_textfield.getText(), last_update_date);
        diagnosisDAO.load();
        int diagnosisId = diagnosisDAO.getId();
        System.out.println(diagnosisId);
        patientDAO.setDiagnosisId(diagnosisId);
        System.out.println(patientDAO.getDiagnosisId());
        patientDAO.setDiagnosis(diagnosisDAO);

        TreatmentDAO treatmentDAO = patientDAO.saveTreatment(patient_plan_textfield.getText(), patient_feedback_textfield.getText(),
                patient_progress_textfield.getText(), conclusion_date);
        treatmentDAO.load();
        int treatmentId = treatmentDAO.getId();
        patientDAO.setTreatmentId(treatmentId);
        System.out.println(patientDAO.getTreatmentId());
        patientDAO.setTreatment(treatmentDAO);

        patientDAO.save();
        patientDAO.loadPatientId();

        AlertMessage.successMessage("Paciente " + fullName + " ("+patientId+") cadastrado com sucesso.");

        patient_list_form.setVisible(true);
        patient_new_form.setVisible(false);

        patient_name_textfield.clear();
        patient_responsible_dropdown.getSelectionModel().clearSelection();
        patient_address_textfield.clear();
        patient_contact_textfield.clear();
        patient_feedback_textfield.clear();
        patient_observations_textfield.clear();
        patient_plan_textfield.clear();
        patient_progress_textfield.clear();
        patient_tests_textfield.clear();

        patient_genre_dropdown.getSelectionModel().clearSelection();

        patient_conclusion_date.setValue(null);
        patient_birthday_date.setValue(null);
        patient_last_update_date.setValue(null);

        final String resetLbl = "------------";
        patient_id_lbl.setText(resetLbl);
        patient_age_lbl.setText(resetLbl);
        patient_responsable_lbl.setText(resetLbl);
        patient_last_lbl.setText(resetLbl);
        patient_name_lbl.setText(resetLbl);
        patient_genre_lbl.setText(resetLbl);
        patient_contact_lbl.setText(resetLbl);
        patient_address_lbl.setText(resetLbl);

        adminDAO.getPatients().add(patientDAO);
        patientDisplayData(adminDAO);
    }

    @FXML
    public void addSecretaryButton() {
        String fullName = secretary_fullname_textfield.getText();
        String password = secretary_password_textfield.getText();
        String email = secretary_email_textfield.getText();
        Genders gender = Genders.getGender(secretary_genre_combobox.getSelectionModel().getSelectedItem());
        if (fullName.isBlank() || password.isBlank() || email.isBlank()) {
            AlertMessage.errorMessage("Preencha todos os campos.");
            return;
        }
        AdminDAO adminDAO = getAdmin();
        String secretaryId = secretary_register_id_textfield.getText();
        SecretaryDAO secretaryDAO = SecretaryDAO.builder()
                .secretaryId(secretaryId)
                .email(email)
                .password(password)
                .fullName(fullName)
                .gender(gender)
                .status(Status.ACTIVE)
                .updatedAt(new Date())
                .createdAt(new Date())
                .build();
        secretaryDAO.save();
        secretaryDAO.loadRegisterId();

        adminDAO.getSecretaries().add(secretaryDAO);

        AlertMessage.successMessage("Conta da secretária " + fullName + " ("+secretaryId+") criada com sucesso.");
        secretary_list_form.setVisible(true);
        secretary_new_form.setVisible(false);
        secretary_fullname_textfield.clear();
        secretary_email_textfield.clear();
        secretary_password_textfield.clear();
        secretary_genre_combobox.getSelectionModel().clearSelection();
        secretaryDisplayData(adminDAO);
    }

    @FXML
    public void markAppointment() {
        String patientNameOrId = appointment_patient_textfield.getText();
        AdminDAO adminDAO = getAdmin();
        if (adminDAO.getPatients().isEmpty())
            updatePatients(adminDAO, true);
        Optional<PatientDAO> patientDAOOptional = adminDAO.getPatients().stream()
                .filter(patientDAO -> patientDAO.getPatientId()
                        .replace("PID-", "")
                        .equalsIgnoreCase(patientNameOrId
                                .replace("PID-", "")) ||
                        patientDAO.getFullName().equalsIgnoreCase(patientNameOrId)).findAny();
        if (patientDAOOptional.isEmpty()) {
            return;
        }
        PatientDAO patientDAO = patientDAOOptional.get();
        Optional<DoctorDAO> doctorDAOOptional = adminDAO.getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getId() == patientDAO.getDoctorId())
                .findAny();
        if (doctorDAOOptional.isEmpty()) {
            return;
        }
        DoctorDAO doctorDAO = doctorDAOOptional.get();
        LocalDate schedule_localdate = appointment_schedule_date.getValue();
        Date schedule = Date.from(schedule_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant());
        String hour = appointment_hour_dropdown.getSelectionModel().getSelectedItem();
        Consults consults = Consults.get(appointment_type_dropdown.getSelectionModel().getSelectedItem());

        AppointmentDAO appointment = AppointmentDAO.builder()
                .patientId(patientDAO.getId())
                .doctorId(doctorDAO.getId())
                .consultationDate(schedule)
                .consultationHour(hour)
                .consultType(consults)
                .createdAt(new Date())
                .status(StatusAppointment.ACTIVE)
                .build();
        appointment.save();
        appointment.loads();

        AlertMessage.successMessage("Consulta de " + doctorDAO.getFullName() + " com "+ patientDAO.getFullName() +" foi marcada com sucesso.");

        appointment_patient_textfield.clear();
        appointment_pro_textfield.clear();
        appointment_schedule_date.setValue(null);
        appointment_hour_dropdown.getSelectionModel().clearSelection();
        appointment_type_dropdown.getSelectionModel().clearSelection();
        appointment_new_form.setVisible(false);
        appointment_list_form.setVisible(true);

        appointment.loadPatientName();
        appointment.loadDoctorName();

        adminDAO.getAppointments().add(appointment);

        appointmentDisplayData(adminDAO);
    }

    @FXML
    public void appointmentSearchConfirm() {
        String patientNameOrId = appointment_patient_textfield.getText();
        if (patientNameOrId.isBlank()) {
            AlertMessage.errorMessage("Coloque o nome ou id de registro do paciente!");
            return;
        }
        AdminDAO adminDAO = getAdmin();
        if (adminDAO.getPatients().isEmpty())
            updatePatients(adminDAO, true);
        Optional<PatientDAO> patientDAOOptional = adminDAO.getPatients().stream()
                .filter(patientDAO -> patientDAO.getPatientId()
                        .replace("PID-", "")
                        .equalsIgnoreCase(patientNameOrId
                        .replace("PID-", "")) ||
                        patientDAO.getFullName().equalsIgnoreCase(patientNameOrId)).findAny();
        if (patientDAOOptional.isEmpty()) {
            AlertMessage.errorMessage("Esse paciente não está cadastrado!");
            return;
        }
        PatientDAO patientDAO = patientDAOOptional.get();
        updateDoctors(adminDAO, true);
        appointment_pro_textfield.setText(patientDAO.getDoctorName(adminDAO.getDoctors()));
        Optional<DoctorDAO> doctorDAOOptional = adminDAO.getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getId() == patientDAO.getDoctorId())
                .findAny();
        if (doctorDAOOptional.isEmpty()) {
            AlertMessage.errorMessage("Esse doutor não está mais cadastrado!");
            return;
        }

        DoctorDAO doctorDAO = doctorDAOOptional.get();
        System.out.println(doctorDAO.getFullName());
        System.out.println(doctorDAO.getId());
        doctorDAO.load();
        System.out.println(doctorDAO.getTimeId());

        appointment_schedule_date.setDisable(false);
        appointment_schedule_date.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                DayOfWeek dayOfWeek = newValue.getDayOfWeek();
                try {
                    List<String> hours = doctorDAO.getAvailable().get(Days.valueOf(dayOfWeek.name()));
                    System.out.println(hours);
                    if (hours.isEmpty()) {
                        appointment_hour_dropdown.setDisable(true);
                        return;
                    }
                    ObservableList<String> h = FXCollections.observableArrayList(hours);
                    System.out.println("ObservableList: "+h);
                    appointment_hour_dropdown.setItems(h);
                    appointment_hour_dropdown.setDisable(false);
                    appointment_hour_dropdown.getSelectionModel().selectFirst();
                } catch (Exception ex) {
                    appointment_hour_dropdown.setDisable(true);
                }
            } else if (newValue == null) {
                appointment_hour_dropdown.setDisable(true);
            }
        });

        appointment_type_dropdown.setDisable(false);
        appointment_hour_dropdown.getSelectionModel().selectFirst();

        appointment_mark_btn.setDisable(false);
    }

    @FXML
    public void addDoctorButton() {
        String fullName = pro_fullname_textfield.getText();
        String password = pro_password_textfield.getText();
        String email = pro_email_textfield.getText();
        Genders gender = Genders.getGender(pro_genre_combobox.getSelectionModel().getSelectedItem());
        if (fullName.isBlank() || password.isBlank() || email.isBlank()) {
            AlertMessage.errorMessage("Preencha todos os campos.");
            return;
        }
        AdminDAO adminDAO = getAdmin();
        String doctorId = pro_register_id_textfield.getText();
        DoctorDAO doctor = adminDAO.getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getDoctorId().equalsIgnoreCase(doctorId))
                .findAny().orElse(
                        DoctorDAO.builder().fullName(fullName)
                                .password(password)
                                .email(email)
                                .gender(gender)
                                .doctorId(doctorId)
                                .build()
                );
        adminDAO.getDoctors().remove(doctor);
        doctor.setPassword(password);
        doctor.setEmail(email);
        doctor.setFullName(fullName);
        doctor.setGender(gender);
        doctor.setStatus(Status.ACTIVE);
        doctor.setCreatedAt(new Date());
        doctor.setUpdatedAt(new Date());
        doctor.save();

        TimeDAO timeDAO = doctor.saveTime();
        timeDAO.loadDoctorId();
        int timeId = timeDAO.getId();
        doctor.setTimeId(timeId);
        doctor.save();
        doctor.loadDoctorId();

        AlertMessage.successMessage("Conta do doutor " + fullName + " ("+doctorId+") criada com sucesso.");

        adminDAO.getDoctors().add(doctor);

        pro_list_form.setVisible(true);
        pro_new_form.setVisible(false);
        pro_fullname_textfield.clear();
        pro_email_textfield.clear();
        pro_password_textfield.clear();
        pro_hour_combobox.getSelectionModel().clearSelection();
        pro_day_combobox.getSelectionModel().clearSelection();
        pro_genre_combobox.getSelectionModel().clearSelection();
        doctorsDisplayData(adminDAO);
    }

    protected void listHourDoctor(AdminDAO adminDAO) {
        String doctorId = pro_register_id_textfield.getText();
        Optional<DoctorDAO> doctorDAOOptional = adminDAO.getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getDoctorId().equalsIgnoreCase(doctorId))
                .findAny();
        if (doctorDAOOptional.isPresent()) {
            DoctorDAO doctor = doctorDAOOptional.get();
            Map<Days, List<String>> dataMap = doctor.getAvailable();
            pro_monday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.MONDAY));
            pro_tuesday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.TUESDAY));
            pro_wednesday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.WEDNESDAY));
            pro_thursday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.THURSDAY));
            pro_friday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.FRIDAY));
            pro_days_table.setItems(FXCollections.observableArrayList(createGroupedSchedules(dataMap)));
        }
    }

    private List<WeeklySchedule> createGroupedSchedules(Map<Days, List<String>> dataMap) {
        List<WeeklySchedule> groupedSchedules = new LinkedList<>();

        int maxTimes = dataMap.values().stream().mapToInt(List::size).max().orElse(0);
        for (int i = 0; i < maxTimes; i++) {
            WeeklySchedule groupedSchedule = new WeeklySchedule();
            for (Days day : Days.values()) {
                List<String> times = dataMap.get(day);
                if (i < times.size()) {
                    groupedSchedule.getDayColumn(day).set(times.get(i));
                } else {
                    groupedSchedule.getDayColumn(day).set("");
                }
            }
            groupedSchedules.add(groupedSchedule);
        }

        return groupedSchedules;
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
            appointment.loadDoctorName();
            appointment.loadPatientName();
            return appointment;
        }).toList()));
        adminDAO.getAppointments().addAll(appointments);
        appointments.clear();
    }

    public void showAndHideList(ActionEvent event) {
        AdminDAO adminDAO = getAdmin();
        if (event.getSource().equals(pro_back_btn) || event.getSource().equals(pro_new_btn)) {
            pro_list_form.setVisible(event.getSource().equals(pro_back_btn));
            pro_new_form.setVisible(event.getSource().equals(pro_new_btn));

            if (pro_list_form.isVisible()) {
                String doctorId = pro_register_id_textfield.getText();
                Optional<DoctorDAO> doctorDAOOptional = adminDAO.getDoctors().stream()
                        .filter(doctorDAO -> doctorDAO.getDoctorId().equalsIgnoreCase(doctorId))
                        .findAny();
                if (doctorDAOOptional.isPresent()) {
                    DoctorDAO doctorDAO = doctorDAOOptional.get();
                    adminDAO.getDoctors().remove(doctorDAO);
                }

                doctorsDisplayData(adminDAO);
                pro_register_id_textfield.clear();
                pro_fullname_textfield.clear();
                pro_email_textfield.clear();
                pro_password_textfield.clear();
                pro_hour_combobox.getSelectionModel().clearSelection();
                pro_day_combobox.getSelectionModel().clearSelection();
                pro_genre_combobox.getSelectionModel().clearSelection();
            } else if (pro_new_form.isVisible()) {
                pro_register_id_textfield.setText(generateId("DID", adminDAO.getDoctors()));
                daysList();
                listHourDoctor(adminDAO);
                pro_genre_combobox.setItems(genreList());
                pro_genre_combobox.getSelectionModel().selectFirst();
            }
        } else if (event.getSource().equals(secretary_back_btn) || event.getSource().equals(secretary_new_btn)) {
            secretary_list_form.setVisible(event.getSource().equals(secretary_back_btn));
            secretary_new_form.setVisible(event.getSource().equals(secretary_new_btn));

            if (secretary_list_form.isVisible()) {
                secretaryDisplayData(adminDAO);
                secretary_password_textfield.clear();
                secretary_fullname_textfield.clear();
                secretary_email_textfield.clear();
                secretary_register_id_textfield.clear();
                secretary_genre_combobox.getSelectionModel().clearSelection();
            } else if (secretary_new_form.isVisible()) {
                secretary_register_id_textfield.setText(generateId("SEC", adminDAO.getSecretaries()));
                secretary_genre_combobox.setItems(genreList());
                secretary_genre_combobox.getSelectionModel().selectFirst();
            }
        } else if (event.getSource().equals(appointment_back_btn) || event.getSource().equals(appointment_new_btn)) {
            appointment_list_form.setVisible(event.getSource().equals(appointment_back_btn));
            appointment_new_form.setVisible(event.getSource().equals(appointment_new_btn));

            if (appointment_list_form.isVisible()) {
                appointmentDisplayData(adminDAO);

                appointment_patient_textfield.clear();
                appointment_pro_textfield.clear();
                appointment_schedule_date.setValue(null);
                appointment_hour_dropdown.getSelectionModel().clearSelection();
                appointment_type_dropdown.getSelectionModel().clearSelection();
            } else if (appointment_new_form.isVisible()) {
                appointment_patient_textfield.setDisable(false);
                appointment_pro_textfield.setDisable(true);
                appointment_schedule_date.setDisable(true);
                appointment_hour_dropdown.setDisable(true);
                appointment_type_dropdown.setDisable(true);
                appointment_mark_btn.setDisable(true);

                appointment_type_dropdown.setItems(typeList());
                appointment_type_dropdown.getSelectionModel().clearSelection();
            }

        } else if (event.getSource().equals(patient_back_btn) || event.getSource().equals(patient_new_btn)) {
            if (!patient_list_form.isVisible()) {
                if (!patient_id_lbl.getText().equals("------------"))
                    if (!AlertMessage.confirmationMessage("As informações confirmadas não serão salvas se não clicar em Adicionar."))
                        return;
            }
            patient_list_form.setVisible(event.getSource().equals(patient_back_btn));
            patient_new_form.setVisible(event.getSource().equals(patient_new_btn));

            if (patient_list_form.isVisible()) {
                patientDisplayData(adminDAO);

                patient_name_textfield.clear();
                patient_responsible_dropdown.getSelectionModel().clearSelection();
                patient_address_textfield.clear();
                patient_contact_textfield.clear();
                patient_feedback_textfield.clear();
                patient_observations_textfield.clear();
                patient_plan_textfield.clear();
                patient_progress_textfield.clear();
                patient_tests_textfield.clear();

                patient_genre_dropdown.getSelectionModel().clearSelection();

                patient_conclusion_date.setValue(null);
                patient_birthday_date.setValue(null);
                patient_last_update_date.setValue(null);

                final String resetLbl = "------------";
                patient_id_lbl.setText(resetLbl);
                patient_age_lbl.setText(resetLbl);
                patient_responsable_lbl.setText(resetLbl);
                patient_last_lbl.setText(resetLbl);
                patient_name_lbl.setText(resetLbl);
                patient_genre_lbl.setText(resetLbl);
                patient_contact_lbl.setText(resetLbl);
                patient_address_lbl.setText(resetLbl);
            } else if (patient_new_form.isVisible()) {
                updateDoctors(adminDAO, true);
                patient_responsible_dropdown.setItems(doctors(adminDAO));
                patient_responsible_dropdown.getSelectionModel().selectFirst();

                patient_genre_dropdown.setItems(genreList());
                patient_genre_dropdown.getSelectionModel().selectFirst();
            }
        }
    }

    private ObservableList<String> doctors(AdminDAO adminDAO) {
        List<String> doctorsName = adminDAO.getDoctors().stream().map(DoctorDAO::getFullName).toList();
        return FXCollections.observableList(doctorsName);
    }

    public void patientDisplayData(AdminDAO adminDAO) {
        ObservableList<PatientDAO> dashboardGetData = FXCollections.observableList(adminDAO.getPatients());

        patient_id.setCellValueFactory(param -> new SimpleStringProperty(""+param.getValue().getId()));
        patient_register_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatientId()));
        patient_fullname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        patient_genre.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGender().getName()));
        patient_phone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMobileNumber().isBlank() ? "Não informado":param.getValue().getMobileNumber()));
        patient_address.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAddress().isBlank() ? "Não informado" : param.getValue().getAddress()));
        patient_diagnosis.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getDiagnosisId())));
        patient_treatment.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getTreatmentId())));
        patient_profissional.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDoctorName(adminDAO.getDoctors())));
        patient_status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));

        patient_table.setItems(dashboardGetData);
    }

    public void secretaryDisplayData(AdminDAO adminDAO) {
        ObservableList<SecretaryDAO> dashboardGetData = FXCollections.observableList(adminDAO.getSecretaries());
        secretary_table_id.setCellValueFactory(param -> new SimpleStringProperty(""+param.getValue().getId()));
        secretary_table_register_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSecretaryId()));
        secretary_table_fullname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        secretary_table_genre.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGender().getName()));
        secretary_table_phone.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getMobileNumber(), "null") ? param.getValue().getAddress() : "Não informado"));
        secretary_table_email.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getEmail(), "null") ? param.getValue().getEmail() : "Não informado"));
        secretary_table_status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));
        secretary_table_address.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getAddress(), "null") ? param.getValue().getAddress() : "Não informado"));
        secretary_table.setItems(dashboardGetData);
    }

    public void appointmentDisplayData(AdminDAO adminDAO) {
        ObservableList<AppointmentDAO> dashboardGetData = FXCollections.observableList(adminDAO.getAppointments());
        appointment_id.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getId())));
        appointment_patient.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatientName()));
        appointment_profissional.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDoctorName()));
        appointment_schedule.setCellValueFactory(param -> new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy").format(param.getValue().getConsultationDate())));
        appointment_hour.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsultationHour()));
        appointment_consult.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsultType().getName()));
        appointment_stats.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));
        appointment_table.setItems(dashboardGetData);
    }

    public void doctorsDisplayData(AdminDAO adminDAO) {
        ObservableList<DoctorDAO> dashboardGetData = FXCollections.observableList(adminDAO.getDoctors());
        pro_table_id.setCellValueFactory(param -> new SimpleStringProperty(""+param.getValue().getId()));
        pro_table_register_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDoctorId()));
        pro_table_fullname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        pro_table_genre.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGender().getName()));
        pro_table_phone.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getMobileNumber(), "null") ? param.getValue().getAddress() : "Não informado"));
        pro_table_email.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getEmail(), "null") ? param.getValue().getEmail() : "Não informado"));
        pro_table_status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));
        pro_table_address.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getAddress(), "null") ? param.getValue().getAddress() : "Não informado"));
        pro_table.setItems(dashboardGetData);
    }

    public void daysList() {
        List<String> days = new ArrayList<>(Stream.of(Days.values()).map(Days::getDayInPT).toList());
        ObservableList<String> listData = FXCollections.observableList(days);
        pro_day_combobox.setItems(listData);

        List<String> hours = getDailyTimeSlots();
        listData = FXCollections.observableList(hours);
        pro_hour_combobox.setItems(listData);
    }

    private AdminDAO getAdmin() {
        AdminDAO adminDAO = (AdminDAO) getUserManager().getUser();
        if (adminDAO == null)
            throw new NullPointerException("AdminDAO não pode ser null.");
        if (!adminDAO.isLoaded())
            adminDAO.load();
        return adminDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        final StringConverter<LocalDate> converter = new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        patient_birthday_date.setConverter(converter);
        patient_last_update_date.setConverter(converter);
        patient_conclusion_date.setConverter(converter);
        appointment_schedule_date.setConverter(converter);

        lbl_username.setText(getUserManager().getName());
        update(false);
    }
}
