package br.estacio.consultasapp.controller.form.types;

import br.estacio.consultasapp.controller.form.FormController;
import br.estacio.consultasapp.user.dao.*;
import br.estacio.consultasapp.user.enums.Days;
import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.enums.Status;
import br.estacio.consultasapp.utils.AlertMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public class DoctorForm extends FormController {

    @FXML
    @Override
    protected void switchForm(ActionEvent event) {
        super.switchForm(event);
    }

    @Override
    protected void update(boolean verify) {
        DoctorDAO doctorDAO = getDoctor();
        updateDoctors(doctorDAO, true);
        updatePatients(doctorDAO, !verify || patient_form.isVisible() || dashboard_form.isVisible());
        updateAppointment(doctorDAO, !verify || appointment_form.isVisible() || dashboard_form.isVisible());

        lbl_num_pro.setText(String.valueOf(doctorDAO.getDoctors().size()));
        lbl_total_patient.setText(String.valueOf(doctorDAO.getPatients().size()));
        lbl_active_patient.setText(String.valueOf(doctorDAO.getPatients().stream()
                .filter(patient -> patient.getStatus().equals(Status.ACTIVE)).toList().size()));
        lbl_total_appointments.setText(String.valueOf(doctorDAO.getAppointments().size()));

        if (patient_form.isVisible()) {
            patientDisplayData(doctorDAO.getPatients()
                    .stream()
                    .filter(s -> s.getDoctorId() == doctorDAO.getId())
                    .toList(), doctorDAO
            );
            page_name.setText("Gerenciar");
        }
        if (appointment_form.isVisible()) {
            appointmentDisplayData(doctorDAO.getAppointments()
                    .stream()
                    .filter(s -> s.getDoctorId() == doctorDAO.getId())
                    .toList());
            page_name.setText("Gerenciar");
        }
        if (dashboard_form.isVisible()) {
            page_name.setText("Dashboard");
            dashboardDisplayData(doctorDAO);
            dashboardNOA();
            dashboardNOP();
        }
        if (profile_form.isVisible()) {
            page_name.setText("Configurar");
            profile_fullname_textfield.setText(doctorDAO.getFullName());
            profile_email_textfield.setText(doctorDAO.getEmail());
            profile_genre_select.setItems(genreList());
            profile_genre_select.getSelectionModel().select(doctorDAO.getGender().getName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            profile_created_account_lbl.setText(simpleDateFormat.format(doctorDAO.getCreatedAt()));

            profile_oldpassword_textfield.setDisable(true);
            profile_newpassword_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                profile_oldpassword_textfield.setDisable(newValue.isBlank());
            });

            updateProfileInfo();
        }
    }

    @FXML
    public void searchPage() {
        DoctorDAO doctorDAO = getDoctor();
        if (appointment_list_form.isVisible()) {
            String search = appointment_search_textfield.getText();
            List<AppointmentDAO> filter = doctorDAO.getAppointments()
                    .stream()
                    .filter(f -> f.getDoctorName().contains(search) ||
                            f.getPatientName().contains(search))
                    .toList();
            appointmentDisplayData(filter);
        } else if (patient_list_form.isVisible()) {
            String search = patient_search_textfield.getText().replace("PID-", "");
            List<PatientDAO> filter = doctorDAO.getPatients()
                    .stream()
                    .filter(f -> f.getFullName().contains(search) ||
                            f.getPatientId().replace("PID-", "").contains(search))
                    .toList();
            patientDisplayData(filter.stream()
                    .filter(s -> s.getDoctorId() == doctorDAO.getId())
                    .toList(), doctorDAO);
        }
    }

    @FXML
    public void refreshPage() {
        update(true);
    }

    @FXML
    public void hourProfile(ActionEvent event) {
        profile_config_normal.setVisible(event.getSource().equals(pro_back_new_btn));
        profile_config_hour.setVisible(event.getSource().equals(profile_hour_btn));

        if (profile_config_hour.isVisible()) {
            daysList();
            listHourDoctor(getDoctor());
        }
    }

    @FXML
    public void saveProfile() {
        String newPassword = profile_newpassword_textfield.getText();
        String oldPassword = profile_oldpassword_textfield.getText();
        DoctorDAO doctorDAO = getDoctor();
        if (!newPassword.isBlank()) {
            if (oldPassword.isBlank()) {
                AlertMessage.errorMessage("Em casos de alteração de senha, é necessário confirmação da atual.");
                return;
            }
            if (oldPassword.equals(newPassword)) {
                AlertMessage.errorMessage("Sua senha atual é igual a nova.");
                return;
            }
            if (!oldPassword.equals(doctorDAO.getPassword())) {
                AlertMessage.errorMessage("Senha atual incorreta.");
                return;
            }
            doctorDAO.setPassword(newPassword);
        }
        String fullName = profile_fullname_textfield.getText();
        String email = profile_email_textfield.getText();
        Genders gender = Genders.getGender(profile_genre_select.getSelectionModel().getSelectedItem());

        if (fullName.isBlank() || email.isBlank()) {
            AlertMessage.errorMessage("Alguns campos essenciais estão vazios.");
            return;
        }

        if (profile_address_textfield != null && profile_contact_textfield != null) {
            String address = profile_address_textfield.getText();
            String contact = profile_contact_textfield.getText();
            doctorDAO.setAddress(address);
            doctorDAO.setMobileNumber(contact);
        }

        doctorDAO.setFullName(fullName);
        doctorDAO.setEmail(email);
        doctorDAO.setGender(gender);
        doctorDAO.setUpdatedAt(new Date());

        doctorDAO.save();
        doctorDAO.saveTime();

        AlertMessage.successMessage("Conta atualizada com sucesso!");
        updateProfileInfo();
    }

    @FXML
    public void newHour() {
        DoctorDAO doctor = getDoctor();
        Days day = Arrays.stream(Days.values())
                .filter(d -> d.getDayInPT().equalsIgnoreCase(pro_day_combobox.getSelectionModel().getSelectedItem()))
                .findAny().orElse(Days.MONDAY);
        String hour = pro_hour_combobox.getSelectionModel().getSelectedItem();
        if (!doctor.getAvailable().get(day).contains(hour)) {
            List<String> availableHours = new ArrayList<>(doctor.getAvailable().get(day));
            availableHours.add(hour);
            doctor.getAvailable().put(day, availableHours);
        }

        listHourDoctor(doctor);
    }

    @FXML
    public void removeHour() {
        DoctorDAO doctor = getDoctor();
        Days day = Arrays.stream(Days.values())
                .filter(d -> d.getDayInPT().equalsIgnoreCase(pro_day_combobox.getSelectionModel().getSelectedItem()))
                .findAny().orElse(Days.MONDAY);
        String hour = pro_hour_combobox.getSelectionModel().getSelectedItem();
        if (!doctor.getAvailable().get(day).contains(hour)) {
            AlertMessage.errorMessage("Não há mais esse horário para esse dia");
            return;
        }
        List<String> availableHours = new ArrayList<>(doctor.getAvailable().get(day));
        availableHours.remove(hour);
        doctor.getAvailable().put(day, availableHours);

        listHourDoctor(doctor);
    }

    @FXML
    public void changeProfilePhoto() {
        DoctorDAO doctorDAO = getDoctor();
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Abrir Imagem", "*jpg", "*jpeg", "*png"));

        File file = open.showOpenDialog(profile_import_btn.getScene().getWindow());

        if (file != null) {
            doctorDAO.setImage(file.getAbsolutePath());
            profile_pfp_circle.setFill(new ImagePattern(getUserManager().getImage()));
        }
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

        DoctorDAO doctorDAO = getDoctor();
        String mobile_phone = patient_contact_textfield.getText();
        String address = patient_address_textfield.getText();
        PatientDAO patientDAO = PatientDAO.builder()
                .patientId(patientId)
                .fullName(fullName)
                .gender(gender)
                .mobileNumber(mobile_phone)
                .birthday(birthday)
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(Status.ACTIVE)
                .doctorId(doctorDAO.getId())
                .address(address).build();
        Button button = patient_add_btn;
        if (button.getText().equalsIgnoreCase("Editar")) {
            Optional<PatientDAO> patientDAOOptional = doctorDAO.getPatients()
                    .stream()
                    .filter(f -> f.getPatientId().equalsIgnoreCase(patientId)).findAny();
            if (patientDAOOptional.isPresent()) {
                patientDAO = patientDAOOptional.get();
                doctorDAO.getPatients().remove(patientDAO);
                patientDAO.setFullName(fullName);
                patientDAO.setGender(gender);
                patientDAO.setMobileNumber(mobile_phone);
                patientDAO.setBirthday(birthday);
                patientDAO.setDoctorId(doctorDAO.getId());
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

        if (patient_responsible_dropdown != null)
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

        doctorDAO.getPatients().add(patientDAO);

        doctorDAO.getPatients().sort(Comparator.comparingInt(PatientDAO::getId));

        patientDisplayData(doctorDAO.getPatients()
                .stream()
                .filter(s -> s.getDoctorId() == doctorDAO.getId())
                .toList(), doctorDAO);
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

        DoctorDAO doctorDAO = getDoctor();
        if (patient_add_btn.getText().equalsIgnoreCase("Adicionar"))
            patient_id_lbl.setText(generateId("PID", doctorDAO.getPatients()));
        patient_age_lbl.setText(age + " anos");
        patient_responsable_lbl.setText(doctorDAO.getFullName());
        patient_name_lbl.setText(fullName);
        patient_genre_lbl.setText(gender.getName());
        patient_contact_lbl.setText(mobile_phone.isBlank() ? "Não informado" : mobile_phone);
        patient_address_lbl.setText(address.isBlank() ? "Não informado" : address);
    }

    @FXML
    public void showAndHideList(ActionEvent event) {
        DoctorDAO doctorDAO = getDoctor();
        if (event.getSource().equals(patient_back_btn) || event.getSource().equals(patient_new_btn)) {
            if (!patient_list_form.isVisible()) {
                if (!patient_id_lbl.getText().equals("------------"))
                    if (!AlertMessage.confirmationMessage("As informações confirmadas não serão salvas se não clicar em Adicionar."))
                        return;
            }
            patient_list_form.setVisible(event.getSource().equals(patient_back_btn));
            patient_new_form.setVisible(event.getSource().equals(patient_new_btn));

            if (patient_list_form.isVisible()) {
                patientDisplayData(doctorDAO.getPatients()
                        .stream()
                        .filter(s -> s.getDoctorId() == doctorDAO.getId())
                        .toList(), doctorDAO);

                patient_name_textfield.clear();
                patient_address_textfield.clear();
                patient_contact_textfield.clear();

                patient_genre_dropdown.getSelectionModel().clearSelection();

                patient_birthday_date.setValue(null);

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

    private DoctorDAO getDoctor() {
        DoctorDAO doctorDAO = (DoctorDAO) getUserManager().getUser();
        if (doctorDAO == null)
            throw new NullPointerException("doctorDAO não pode ser null.");
        if (!doctorDAO.isLoaded())
            doctorDAO.load();
        return doctorDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        DoctorDAO doctorDAO = getDoctor();
        if (doctorDAO.getImage() != null && !doctorDAO.getImage().isBlank() && !doctorDAO.getImage().equalsIgnoreCase("null"))
            circle_pfp.setFill(new ImagePattern(getUserManager().getImage()));
    }
}
