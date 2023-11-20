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

public class SecretaryForm extends FormController {

    @FXML
    @Override
    protected void switchForm(ActionEvent event) {
        super.switchForm(event);
    }

    @Override
    protected void update(boolean verify) {
        SecretaryDAO secretaryDAO = getSecretary();
        updatePatients(secretaryDAO, !verify || patient_form.isVisible() || dashboard_form.isVisible());
        updateAppointment(secretaryDAO, !verify || appointment_form.isVisible() || dashboard_form.isVisible());

        lbl_num_pro.setText(String.valueOf(secretaryDAO.getDoctors().size()));
        lbl_total_patient.setText(String.valueOf(secretaryDAO.getPatients().size()));
        lbl_active_patient.setText(String.valueOf(secretaryDAO.getPatients().stream()
                .filter(patient -> patient.getStatus().equals(Status.ACTIVE)).toList().size()));
        lbl_total_appointments.setText(String.valueOf(secretaryDAO.getAppointments().size()));

        if (patient_form.isVisible()) {
            if (secretaryDAO.getDoctors().isEmpty())
                updateDoctors(secretaryDAO, true);
            patientDisplayData(secretaryDAO.getPatients(), secretaryDAO.getDoctors());
            page_name.setText("Gerenciar");
        }
        if (appointment_form.isVisible()) {
            appointmentDisplayData(secretaryDAO.getAppointments());
            page_name.setText("Gerenciar");
        }
        if (dashboard_form.isVisible()) {
            page_name.setText("Dashboard");
            dashboardDisplayData(secretaryDAO);
            dashboardNOA();
            dashboardNOP();
        }
        if (profile_form.isVisible()) {
            page_name.setText("Configurar");
            profile_fullname_textfield.setText(secretaryDAO.getFullName());
            profile_email_textfield.setText(secretaryDAO.getEmail());
            profile_genre_select.setItems(genreList());
            profile_genre_select.getSelectionModel().select(secretaryDAO.getGender().getName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            profile_created_account_lbl.setText(simpleDateFormat.format(secretaryDAO.getCreatedAt()));

            profile_oldpassword_textfield.setDisable(true);
            profile_newpassword_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                profile_oldpassword_textfield.setDisable(newValue.isBlank());
            });

            updateProfileInfo();
        }
    }

    @FXML
    public void searchPage() {
        SecretaryDAO secretaryDAO = getSecretary();
        if (appointment_list_form.isVisible()) {
            String search = appointment_search_textfield.getText();
            List<AppointmentDAO> filter = secretaryDAO.getAppointments()
                    .stream()
                    .filter(f -> f.getDoctorName().contains(search) ||
                            f.getPatientName().contains(search))
                    .toList();
            appointmentDisplayData(filter);
        } else if (patient_list_form.isVisible()) {
            String search = patient_search_textfield.getText().replace("PID-", "");
            List<PatientDAO> filter = secretaryDAO.getPatients()
                    .stream()
                    .filter(f -> f.getFullName().contains(search) ||
                            f.getPatientId().replace("PID-", "").contains(search))
                    .toList();
            if (secretaryDAO.getDoctors().isEmpty())
                updateDoctors(secretaryDAO, true);
            patientDisplayData(filter, secretaryDAO.getDoctors());
        }
    }

    @FXML
    public void refreshPage() {
        update(true);
    }

    @FXML
    public void saveProfile() {
        String newPassword = profile_newpassword_textfield.getText();
        String oldPassword = profile_oldpassword_textfield.getText();
        SecretaryDAO secretaryDAO = getSecretary();
        if (!newPassword.isBlank()) {
            if (oldPassword.isBlank()) {
                AlertMessage.errorMessage("Em casos de alteração de senha, é necessário confirmação da atual.");
                return;
            }
            if (oldPassword.equals(newPassword)) {
                AlertMessage.errorMessage("Sua senha atual é igual a nova.");
                return;
            }
            if (!oldPassword.equals(secretaryDAO.getPassword())) {
                AlertMessage.errorMessage("Senha atual incorreta.");
                return;
            }
            secretaryDAO.setPassword(newPassword);
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
            secretaryDAO.setAddress(address);
            secretaryDAO.setMobileNumber(contact);
        }

        secretaryDAO.setFullName(fullName);
        secretaryDAO.setEmail(email);
        secretaryDAO.setGender(gender);
        secretaryDAO.setUpdatedAt(new Date());
        secretaryDAO.save();
        AlertMessage.successMessage("Conta atualizada com sucesso!");
        updateProfileInfo();
    }

    @FXML
    public void changeProfilePhoto() {
        SecretaryDAO secretaryDAO = getSecretary();
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Abrir Imagem", "*jpg", "*jpeg", "*png"));

        File file = open.showOpenDialog(profile_import_btn.getScene().getWindow());

        if (file != null) {
            secretaryDAO.setImage(file.getAbsolutePath());
            profile_pfp_circle.setFill(new ImagePattern(getUserManager().getImage()));
        }
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

        if (patient_add_btn.getText().equalsIgnoreCase("Adicionar"))
            patient_id_lbl.setText(generateId("PID", getSecretary().getPatients()));
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

        String fullName = patient_name_textfield.getText();
        LocalDate birth_localdate = patient_birthday_date.getValue();
        Date birthday = Date.from(birth_localdate.atStartOfDay(ZoneId.of("GMT-3")).toInstant());
        Genders gender = Genders.getGender(patient_genre_dropdown.getSelectionModel().getSelectedItem());
        SecretaryDAO secretaryDAO = getSecretary();
        Optional<Integer> doctorId = secretaryDAO.getDoctors().stream()
                .filter(doctorDAO -> doctorDAO.getFullName().equalsIgnoreCase(patient_responsable_lbl.getText()))
                .findAny().map(DoctorDAO::getId);
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
                .doctorId(doctorId.orElse(0))
                .address(address).build();
        Button button = patient_add_btn;
        if (button.getText().equalsIgnoreCase("Editar")) {
            Optional<PatientDAO> patientDAOOptional = secretaryDAO.getPatients()
                    .stream()
                    .filter(f -> f.getPatientId().equalsIgnoreCase(patientId)).findAny();
            if (patientDAOOptional.isPresent()) {
                patientDAO = patientDAOOptional.get();
                secretaryDAO.getPatients().remove(patientDAO);
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

        DiagnosisDAO diagnosisDAO = patientDAO.saveDiagnosis("", "", null);
        diagnosisDAO.load();
        int diagnosisId = diagnosisDAO.getId();
        patientDAO.setDiagnosisId(diagnosisId);
        patientDAO.setDiagnosis(diagnosisDAO);

        TreatmentDAO treatmentDAO = patientDAO.saveTreatment("", "", "", null);
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

        secretaryDAO.getPatients().add(patientDAO);

        secretaryDAO.getPatients().sort(Comparator.comparingInt(PatientDAO::getId));

        patientDisplayData(secretaryDAO.getPatients(), secretaryDAO.getDoctors());
    }

    @FXML
    public void markAppointment() {
        String patientNameOrId = appointment_patient_textfield.getText();
        SecretaryDAO secretaryDAO = getSecretary();
        if (secretaryDAO.getPatients().isEmpty())
            updatePatients(secretaryDAO, true);
        Optional<PatientDAO> patientDAOOptional = secretaryDAO.getPatients().stream()
                .filter(patientDAO -> patientDAO.getPatientId()
                        .replace("PID-", "")
                        .equalsIgnoreCase(patientNameOrId
                                .replace("PID-", "")) ||
                        patientDAO.getFullName().equalsIgnoreCase(patientNameOrId)).findAny();
        if (patientDAOOptional.isEmpty()) {
            return;
        }
        PatientDAO patientDAO = patientDAOOptional.get();
        Optional<DoctorDAO> doctorDAOOptional = secretaryDAO.getDoctors().stream()
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

        secretaryDAO.getAppointments().add(appointment);

        appointmentDisplayData(secretaryDAO.getAppointments());
    }

    @FXML
    public void appointmentSearchConfirm() {
        String patientNameOrId = appointment_patient_textfield.getText();
        if (patientNameOrId.isBlank()) {
            AlertMessage.errorMessage("Coloque o nome ou id de registro do paciente!");
            return;
        }
        SecretaryDAO secretaryDAO = getSecretary();
        if (secretaryDAO.getPatients().isEmpty())
            updatePatients(secretaryDAO, true);
        Optional<PatientDAO> patientDAOOptional = secretaryDAO.getPatients().stream()
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
        updateDoctors(secretaryDAO, true);
        appointment_pro_textfield.setText(patientDAO.getDoctorName(secretaryDAO.getDoctors()));
        Optional<DoctorDAO> doctorDAOOptional = secretaryDAO.getDoctors().stream()
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

    @FXML
    public void showAndHideList(ActionEvent event) {
        SecretaryDAO secretaryDAO = getSecretary();
        if (event.getSource().equals(appointment_back_btn) || event.getSource().equals(appointment_new_btn)) {
            appointment_list_form.setVisible(event.getSource().equals(appointment_back_btn));
            appointment_new_form.setVisible(event.getSource().equals(appointment_new_btn));

            if (appointment_list_form.isVisible()) {
                appointmentDisplayData(secretaryDAO.getAppointments());

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
                if (secretaryDAO.getDoctors().isEmpty())
                    updateDoctors(secretaryDAO, true);
                patientDisplayData(secretaryDAO.getPatients(), secretaryDAO.getDoctors());

                patient_name_textfield.clear();
                patient_responsible_dropdown.getSelectionModel().clearSelection();
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

    private SecretaryDAO getSecretary() {
        SecretaryDAO secretaryDAO = (SecretaryDAO) getUserManager().getUser();
        if (secretaryDAO == null)
            throw new NullPointerException("SecretaryDAO não pode ser null.");
        if (!secretaryDAO.isLoaded())
            secretaryDAO.load();
        return secretaryDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        SecretaryDAO secretaryDAO = getSecretary();
        if (secretaryDAO.getImage() != null && !secretaryDAO.getImage().isBlank() && !secretaryDAO.getImage().equalsIgnoreCase("null"))
            circle_pfp.setFill(new ImagePattern(getUserManager().getImage()));
    }
}
