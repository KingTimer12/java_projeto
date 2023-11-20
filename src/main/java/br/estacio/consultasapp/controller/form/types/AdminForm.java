package br.estacio.consultasapp.controller.form.types;

import br.estacio.consultasapp.controller.form.FormController;
import br.estacio.consultasapp.user.dao.*;
import br.estacio.consultasapp.user.enums.*;
import br.estacio.consultasapp.utils.AlertMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public class AdminForm extends FormController {

    @Override
    public void switchForm(ActionEvent event) {
        pro_form.setVisible(event.getSource().equals(btn_pro));
        secretary_form.setVisible(event.getSource().equals(btn_secretary));
        super.switchForm(event);
    }

    @FXML
    public void searchPage() {
        AdminDAO adminDAO = getAdmin();
        if (pro_list_form.isVisible()) {
            String search = pro_search_textfield.getText().replace("DID-", "");

            List<DoctorDAO> filter = adminDAO.getDoctors()
                    .stream()
                    .filter(f -> f.getFullName().contains(search) ||
                            f.getDoctorId().replace("DID-", "").contains(search)).toList();
            doctorsDisplayData(filter);
        } else if (appointment_list_form.isVisible()) {
            String search = appointment_search_textfield.getText();
            List<AppointmentDAO> filter = adminDAO.getAppointments()
                    .stream()
                    .filter(f -> f.getDoctorName().contains(search) ||
                            f.getPatientName().contains(search))
                    .toList();
            appointmentDisplayData(filter);
        } else if (secretary_list_form.isVisible()) {
            String search = secretary_search_textfield.getText().replace("SID-", "");
            List<SecretaryDAO> filter = adminDAO.getSecretaries()
                    .stream()
                    .filter(f -> f.getFullName().contains(search) ||
                            f.getSecretaryId().replace("SID-", "").contains(search))
                    .toList();
            secretaryDisplayData(filter);
        } else if (patient_list_form.isVisible()) {
            String search = patient_search_textfield.getText().replace("PID-", "");
            List<PatientDAO> filter = adminDAO.getPatients()
                    .stream()
                    .filter(f -> f.getFullName().contains(search) ||
                            f.getPatientId().replace("PID-", "").contains(search))
                    .toList();
            if (adminDAO.getDoctors().isEmpty())
                updateDoctors(adminDAO, true);
            patientDisplayData(filter, adminDAO.getDoctors());
        }
    }

    @FXML
    public void refreshPage() {
        update(true);
    }

    @Override
    protected void update(boolean verify) {
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
            doctorsDisplayData(adminDAO.getDoctors());
            page_name.setText("Gerenciar");
        }
        if (patient_form.isVisible()) {
            if (adminDAO.getDoctors().isEmpty())
                updateDoctors(adminDAO, true);
            patientDisplayData(adminDAO.getPatients(), adminDAO.getDoctors());
            page_name.setText("Gerenciar");
        }
        if (secretary_form.isVisible()) {
            secretaryDisplayData(adminDAO.getSecretaries());
            page_name.setText("Gerenciar");
        }
        if (appointment_form.isVisible()) {
            appointmentDisplayData(adminDAO.getAppointments());
            page_name.setText("Gerenciar");
        }
        if (dashboard_form.isVisible()) {
            page_name.setText("Dashboard");
            dashboardDisplayData(adminDAO);
            dashboardNOA();
            dashboardNOP();
        }
        if (profile_form.isVisible()) {
            page_name.setText("Configurar");
            profile_fullname_textfield.setText(adminDAO.getFullName());
            profile_email_textfield.setText(adminDAO.getEmail());
            profile_genre_select.setItems(genreList());
            profile_genre_select.getSelectionModel().select(adminDAO.getGender().getName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            profile_created_account_lbl.setText(simpleDateFormat.format(adminDAO.getCreatedAt()));

            profile_oldpassword_textfield.setDisable(true);
            profile_newpassword_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                profile_oldpassword_textfield.setDisable(newValue.isBlank());
            });

            updateProfileInfo();
        }
    }

    @FXML
    public void saveProfile() {
        String newPassword = profile_newpassword_textfield.getText();
        String oldPassword = profile_oldpassword_textfield.getText();
        AdminDAO adminDAO = getAdmin();
        if (!newPassword.isBlank()) {
            if (oldPassword.isBlank()) {
                AlertMessage.errorMessage("Em casos de alteração de senha, é necessário confirmação da atual.");
                return;
            }
            if (oldPassword.equals(newPassword)) {
                AlertMessage.errorMessage("Sua senha atual é igual a nova.");
                return;
            }
            if (!oldPassword.equals(adminDAO.getPassword())) {
                AlertMessage.errorMessage("Senha atual incorreta.");
                return;
            }
            adminDAO.setPassword(newPassword);
        }
        String fullName = profile_fullname_textfield.getText();
        String email = profile_email_textfield.getText();
        Genders gender = Genders.getGender(profile_genre_select.getSelectionModel().getSelectedItem());

        if (fullName.isBlank() || email.isBlank()) {
            AlertMessage.errorMessage("Alguns campos essenciais estão vazios.");
            return;
        }

        adminDAO.setFullName(fullName);
        adminDAO.setEmail(email);
        adminDAO.setGender(gender);
        adminDAO.setUpdatedAt(new Date());
        adminDAO.save();
        AlertMessage.successMessage("Conta atualizada com sucesso!");
        updateProfileInfo();
    }

    @FXML
    public void changeProfilePhoto() {
        AdminDAO adminDAO = getAdmin();
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Abrir Imagem", "*jpg", "*jpeg", "*png"));

        File file = open.showOpenDialog(profile_import_btn.getScene().getWindow());

        if (file != null) {
            adminDAO.setImage(file.getAbsolutePath());
            profile_pfp_circle.setFill(new ImagePattern(getUserManager().getImage()));
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
        if (!doctor.getAvailable().get(day).contains(hour)) {
            List<String> availableHours = new ArrayList<>(doctor.getAvailable().get(day));
            availableHours.add(hour);
            doctor.getAvailable().put(day, availableHours);
        }

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
        List<String> availableHours = new ArrayList<>(doctor.getAvailable().get(day));
        availableHours.remove(hour);
        doctor.getAvailable().put(day, availableHours);

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

        String responsible = patient_responsible_dropdown.getSelectionModel().getSelectedItem();
        String mobile_phone = patient_contact_textfield.getText();
        String address = patient_address_textfield.getText();

        Period period = Period.between(birthLocalDate, currentDate);
        int age = period.getYears();

        LocalDate last_update_localdate = patient_last_update_date.getValue();
        if (last_update_localdate == null) patient_last_lbl.setText("------------");
        else {
            Date last_update_date = Date.from(last_update_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            patient_last_lbl.setText(simpleDateFormat.format(last_update_date));
        }

        if (patient_add_btn.getText().equalsIgnoreCase("Adicionar"))
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
        Button button = patient_add_btn;
        if (button.getText().equalsIgnoreCase("Editar")) {
            Optional<PatientDAO> patientDAOOptional = adminDAO.getPatients()
                    .stream()
                    .filter(f -> f.getPatientId().equalsIgnoreCase(patientId)).findAny();
            if (patientDAOOptional.isPresent()) {
                patientDAO = patientDAOOptional.get();
                adminDAO.getPatients().remove(patientDAO);
                patientDAO.setFullName(fullName);
                patientDAO.setGender(gender);
                patientDAO.setMobileNumber(mobile_phone);
                patientDAO.setBirthday(birthday);
                patientDAO.setDoctorId(doctorId.orElse(0));
                patientDAO.setAddress(address);
                patientDAO.setUpdatedAt(new Date());
            }
        }
        patientDAO.save();

        DiagnosisDAO diagnosisDAO = patientDAO.saveDiagnosis(patient_observations_textfield.getText(),
                patient_tests_textfield.getText(), last_update_date);
        diagnosisDAO.load();
        int diagnosisId = diagnosisDAO.getId();
        patientDAO.setDiagnosisId(diagnosisId);
        patientDAO.setDiagnosis(diagnosisDAO);

        TreatmentDAO treatmentDAO = patientDAO.saveTreatment(patient_plan_textfield.getText(), patient_feedback_textfield.getText(),
                patient_progress_textfield.getText(), conclusion_date);
        treatmentDAO.load();
        int treatmentId = treatmentDAO.getId();
        patientDAO.setTreatmentId(treatmentId);
        patientDAO.setTreatment(treatmentDAO);

        patientDAO.save();
        patientDAO.loadPatientId();

        String actionStatus = !button.getText().equalsIgnoreCase("Editar") ? "cadastrado" : "atualizado";
        AlertMessage.successMessage("Paciente " + fullName + " ("+patientId+") "+actionStatus+" com sucesso.");

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

        adminDAO.getPatients().sort(Comparator.comparingInt(PatientDAO::getId));

        patientDisplayData(adminDAO.getPatients(), adminDAO.getDoctors());
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

        Button button = secretary_add_btn;
        if (button.getText().equalsIgnoreCase("Editar")) {
            Optional<SecretaryDAO> secretaryDAOOptional = adminDAO.getSecretaries()
                    .stream()
                    .filter(f -> f.getSecretaryId().equalsIgnoreCase(secretaryId)).findAny();
            if (secretaryDAOOptional.isPresent()) {
                secretaryDAO = secretaryDAOOptional.get();
                adminDAO.getSecretaries().remove(secretaryDAO);
                secretaryDAO.setEmail(email);
                secretaryDAO.setPassword(password);
                secretaryDAO.setFullName(fullName);
                secretaryDAO.setGender(gender);
                secretaryDAO.setUpdatedAt(new Date());
            }
        }

        secretaryDAO.save();
        secretaryDAO.loadRegisterId();

        adminDAO.getSecretaries().add(secretaryDAO);

        String actionStatus = !button.getText().equalsIgnoreCase("Editar") ? "criada" : "atualizada";
        AlertMessage.successMessage("Conta da secretária " + fullName + " ("+secretaryId+") "+actionStatus+" com sucesso.");
        secretary_list_form.setVisible(true);
        secretary_new_form.setVisible(false);
        secretary_fullname_textfield.clear();
        secretary_email_textfield.clear();
        secretary_password_textfield.clear();
        secretary_genre_combobox.getSelectionModel().clearSelection();

        adminDAO.getSecretaries().sort(Comparator.comparingInt(SecretaryDAO::getId));

        secretaryDisplayData(adminDAO.getSecretaries());
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
        Button button = pro_add_btn;
        if (!button.getText().equalsIgnoreCase("Editar"))
            doctor.setCreatedAt(new Date());
        doctor.setUpdatedAt(new Date());
        doctor.save();

        TimeDAO timeDAO = doctor.saveTime();
        timeDAO.loadDoctorId();
        int timeId = timeDAO.getId();
        doctor.setTimeId(timeId);
        doctor.save();
        doctor.loadDoctorId();

        String actionStatus = !button.getText().equalsIgnoreCase("Editar") ? "criada" : "atualizada";
        AlertMessage.successMessage("Conta do doutor " + fullName + " ("+doctorId+") "+actionStatus+" com sucesso.");

        adminDAO.getDoctors().add(doctor);

        pro_list_form.setVisible(true);
        pro_new_form.setVisible(false);
        pro_fullname_textfield.clear();
        pro_email_textfield.clear();
        pro_password_textfield.clear();
        pro_hour_combobox.getSelectionModel().clearSelection();
        pro_day_combobox.getSelectionModel().clearSelection();
        pro_genre_combobox.getSelectionModel().clearSelection();

        adminDAO.getDoctors().sort(Comparator.comparingInt(DoctorDAO::getId));

        doctorsDisplayData(adminDAO.getDoctors());
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

        appointmentDisplayData(adminDAO.getAppointments());
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
                    if (doctorDAO.getId() == 0)
                        adminDAO.getDoctors().remove(doctorDAO);
                }

                doctorsDisplayData(adminDAO.getDoctors());
                pro_register_id_textfield.clear();
                pro_fullname_textfield.clear();
                pro_email_textfield.clear();
                pro_password_textfield.clear();
                pro_hour_combobox.getSelectionModel().clearSelection();
                pro_day_combobox.getSelectionModel().clearSelection();
                pro_genre_combobox.getSelectionModel().clearSelection();
            } else if (pro_new_form.isVisible()) {
                newOrEditDoctor(null);
            }
        } else if (event.getSource().equals(secretary_back_btn) || event.getSource().equals(secretary_new_btn)) {
            secretary_list_form.setVisible(event.getSource().equals(secretary_back_btn));
            secretary_new_form.setVisible(event.getSource().equals(secretary_new_btn));

            if (secretary_list_form.isVisible()) {
                secretaryDisplayData(adminDAO.getSecretaries());
                secretary_password_textfield.clear();
                secretary_fullname_textfield.clear();
                secretary_email_textfield.clear();
                secretary_register_id_textfield.clear();
                secretary_genre_combobox.getSelectionModel().clearSelection();
            } else if (secretary_new_form.isVisible()) {
                newOrEditSecretary(null);
            }
        } else if (event.getSource().equals(appointment_back_btn) || event.getSource().equals(appointment_new_btn)) {
            appointment_list_form.setVisible(event.getSource().equals(appointment_back_btn));
            appointment_new_form.setVisible(event.getSource().equals(appointment_new_btn));

            if (appointment_list_form.isVisible()) {
                appointmentDisplayData(adminDAO.getAppointments());

                appointment_patient_textfield.clear();
                appointment_pro_textfield.clear();
                appointment_schedule_date.setValue(null);
                appointment_hour_dropdown.getSelectionModel().clearSelection();
                appointment_type_dropdown.getSelectionModel().clearSelection();
            } else if (appointment_new_form.isVisible()) {
                newOrEditAppointment();
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
                if (adminDAO.getDoctors().isEmpty())
                    updateDoctors(adminDAO, true);
                patientDisplayData(adminDAO.getPatients(), adminDAO.getDoctors());

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
                newOrEditPatient(null);
            }
        }
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
        AdminDAO adminDAO = getAdmin();
        if (adminDAO.getImage() != null && !adminDAO.getImage().isBlank() && !adminDAO.getImage().equalsIgnoreCase("null"))
            circle_pfp.setFill(new ImagePattern(getUserManager().getImage()));
    }
}
