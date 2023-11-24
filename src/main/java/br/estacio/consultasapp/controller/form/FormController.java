package br.estacio.consultasapp.controller.form;

import br.com.timer.objects.data.types.ListData;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;
import br.estacio.consultasapp.controller.*;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.*;
import br.estacio.consultasapp.user.dao.*;
import br.estacio.consultasapp.user.enums.*;
import br.estacio.consultasapp.user.interfaces.IdInterface;
import br.estacio.consultasapp.utils.AlertMessage;
import br.estacio.consultasapp.utils.WeeklySchedule;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public abstract class FormController extends GeneralVariables implements Initializable, IMinus, IClose {

    protected void switchForm(ActionEvent event) {
        dashboard_form.setVisible(event.getSource().equals(btn_dashboard));
        appointment_form.setVisible(event.getSource().equals(btn_appointment));
        patient_form.setVisible(event.getSource().equals(btn_pacient));
        profile_form.setVisible(event.getSource().equals(btn_config));
        update(true);
    }

    @FXML
    protected void logout() throws IOException {
        Manager.getManager(GuiManager.class).openGui("adminLogin", StageStyle.TRANSPARENT);
        main_form.getScene().getWindow().hide();
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

    @Override
    public void close() {
        System.exit(0);
    }

    @Override
    public void minimize() {
        Stage stage = (Stage) btn_appointment.getScene().getWindow();
        stage.setIconified(true);
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
            calendar.add(Calendar.MINUTE, 15);
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

    protected void dashboardDisplayData(UserDAO adminDAO) {
        List<AppointmentDAO> appointments = adminDAO.getAppointments();
        if (adminDAO instanceof DoctorDAO) {
            appointments = adminDAO.getAppointments()
                    .stream()
                    .filter(f -> (f.getDoctorId() == adminDAO.getId()))
                    .toList();
        }
        ObservableList<AppointmentDAO> dashboardGetData = FXCollections.observableList(appointments);
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
        lbl_user_name.setText(getUserManager().getFullName());
        lbl_username.setText(getUserManager().getName());

        dashboardNOA();
        dashboardNOP();

        runTime();

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
        if (patient_last_update_date != null)
            patient_last_update_date.setConverter(converter);
        if (patient_conclusion_date != null)
            patient_conclusion_date.setConverter(converter);
        if (appointment_schedule_date != null)
            appointment_schedule_date.setConverter(converter);

        lbl_username.setText(getUserManager().getName());
        update(false);
    }

    protected void dashboardNOP() {
        dashboard_chart_patient.getData().clear();

        UserManager userManager = getUserManager();
        StringBuilder sql = new StringBuilder("SELECT createdAt, COUNT(id) FROM patients ");
        if (userManager.getUser() instanceof DoctorDAO) {
            sql.append(" WHERE doctor_id='").append(userManager.getUser().getId()).append("'");
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

    protected void updateSecretaries(UserDAO extendedDAO, boolean secretary_form) {
        if (!extendedDAO.getSecretaries().isEmpty())
            extendedDAO.getSecretaries().clear();
        if (!secretary_form) return;
        ListData listData = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().list()
                .from(SecretaryDAO.class).builder();
        final List<SecretaryDAO> secretaries = new ArrayList<>();
        listData.get("id").ifPresent(list -> secretaries.addAll(list.stream().map(data -> {
            SecretaryDAO secretary = SecretaryDAO.builder().id(data.asInt()).build();
            secretary.load();
            return secretary;
        }).filter(f -> !f.getStatus().equals(Status.DELETED)).toList()));
        extendedDAO.getSecretaries().addAll(secretaries);
        secretaries.clear();
    }

    protected void updateDoctors(UserDAO extendedDAO, boolean doctor_form) {
        if (!extendedDAO.getDoctors().isEmpty())
            extendedDAO.getDoctors().clear();
        if (!doctor_form)
            return;
        ListData listData = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().list()
                .from(DoctorDAO.class).builder();
        final List<DoctorDAO> doctors = new ArrayList<>();
        listData.get("id").ifPresent(list -> doctors.addAll(list.stream().map(data -> {
            DoctorDAO doctor = DoctorDAO.builder().id(data.asInt()).build();
            doctor.load();
            System.out.println(doctor.getFullName());
            System.out.println(doctor.getStatus().getName());
            return doctor;
        }).filter(f -> !f.getStatus().equals(Status.DELETED)).toList()));
        extendedDAO.getDoctors().addAll(doctors);
        doctors.clear();
    }

    protected void updatePatients(UserDAO extendedDAO, boolean patient_form) {
        if (!extendedDAO.getPatients().isEmpty())
            extendedDAO.getPatients().clear();
        if (!patient_form) return;
        ListData listData = Manager.getManager(DatabaseManager.class).getMySQL().getHandler().list()
                .from(PatientDAO.class).builder();
        final List<PatientDAO> patients = new ArrayList<>();
        listData.get("id").ifPresent(list -> patients.addAll(list.stream().map(data -> {
            PatientDAO patient = PatientDAO.builder().id(data.asInt()).build();
            patient.load();
            return patient;
        }).filter(f -> !f.getStatus().equals(Status.DELETED)).toList()));
        extendedDAO.getPatients().addAll(patients);
        patients.clear();
    }

    protected void updateAppointment(UserDAO extendedDAO, boolean appointment_form) {
        if (!extendedDAO.getAppointments().isEmpty())
            extendedDAO.getAppointments().clear();
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
        extendedDAO.getAppointments().addAll(appointments);
        appointments.clear();
    }

    protected void dashboardNOA() {
        dashboard_chart_appointment.getData().clear();

        UserManager userManager = getUserManager();
        StringBuilder sql = new StringBuilder("SELECT createdAt, COUNT(id) FROM appointments ");
        if (userManager.getUser() instanceof DoctorDAO) {
            sql.append(" WHERE doctor_id='").append(userManager.getUser().getId()).append("'");
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

    protected void doctorsDisplayData(List<DoctorDAO> doctors) {
        ObservableList<DoctorDAO> dashboardGetData = FXCollections.observableList(doctors);
        pro_table_id.setCellValueFactory(param -> new SimpleStringProperty(""+param.getValue().getId()));
        pro_table_register_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDoctorId()));
        pro_table_fullname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        pro_table_genre.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGender().getName()));
        pro_table_phone.setCellValueFactory(param -> new SimpleStringProperty(!(Objects.equals(param.getValue().getMobileNumber(), "null") || param.getValue().getMobileNumber().isBlank()) ? param.getValue().getMobileNumber() : "Não informado"));
        pro_table_email.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getEmail(), "null") ? param.getValue().getEmail() : "Não informado"));
        pro_table_status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));
        pro_table_address.setCellValueFactory(param -> new SimpleStringProperty(!(Objects.equals(param.getValue().getAddress(), "null") || param.getValue().getAddress().isBlank()) ? param.getValue().getAddress() : "Não informado"));

        Callback<TableColumn<DoctorDAO, String>, TableCell<DoctorDAO, String>> cellFactory = (TableColumn<DoctorDAO, String> param) -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Button editButton = createButton("Editar");
                    Button removeButton = createButton("Remover");

                    editButton.setOnAction((ActionEvent event) -> {
                        DoctorDAO doctor = pro_table.getSelectionModel().getSelectedItem();
                        int num = pro_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        newOrEditDoctor(doctor);
                    });

                    removeButton.setOnAction((ActionEvent event) -> {
                        UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
                        DoctorDAO doctor = pro_table.getSelectionModel().getSelectedItem();
                        int num = pro_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        deleteInfo(doctor);
                        doctorsDisplayData(userDAO.getDoctors());
                    });

                    HBox manageBtn = new HBox(editButton, removeButton);
                    manageBtn.setAlignment(Pos.CENTER);
                    manageBtn.setSpacing(5);
                    setGraphic(manageBtn);
                    setText(null);
                }

            }
        };

        pro_table_action.setCellFactory(cellFactory);
        pro_table.setItems(dashboardGetData);
    }

    protected void patientDisplayData(List<PatientDAO> patients, List<DoctorDAO> doctors) {
        ObservableList<PatientDAO> dashboardGetData = FXCollections.observableList(patients);

        patient_id.setCellValueFactory(param -> new SimpleStringProperty(""+param.getValue().getId()));
        patient_register_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatientId()));
        patient_fullname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        patient_genre.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGender().getName()));
        patient_phone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMobileNumber().isBlank() ? "Não informado":param.getValue().getMobileNumber()));
        patient_address.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAddress().isBlank() ? "Não informado" : param.getValue().getAddress()));
        patient_profissional.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDoctorName(doctors)));
        patient_status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));

        Callback<TableColumn<PatientDAO, String>, TableCell<PatientDAO, String>> cellFactory = (TableColumn<PatientDAO, String> param) -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Button editButton = createButton("Editar");
                    Button removeButton = createButton("Remover");

                    editButton.setOnAction((ActionEvent event) -> {
                        PatientDAO patient = patient_table.getSelectionModel().getSelectedItem();
                        int num = patient_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        newOrEditPatient(patient);
                    });

                    removeButton.setOnAction((ActionEvent event) -> {
                        PatientDAO patient = patient_table.getSelectionModel().getSelectedItem();
                        int num = patient_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        deleteInfo(patient);
                    });

                    HBox manageBtn = new HBox(editButton, removeButton);
                    manageBtn.setAlignment(Pos.CENTER);
                    manageBtn.setSpacing(5);
                    setGraphic(manageBtn);
                    setText(null);
                }

            }
        };

        patient_action.setCellFactory(cellFactory);
        patient_table.setItems(dashboardGetData);
    }

    protected void patientDisplayData(List<PatientDAO> patients, DoctorDAO doctor) {
        ObservableList<PatientDAO> dashboardGetData = FXCollections.observableList(patients);

        patient_id.setCellValueFactory(param -> new SimpleStringProperty(""+param.getValue().getId()));
        patient_register_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatientId()));
        patient_fullname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        patient_genre.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGender().getName()));
        patient_phone.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMobileNumber().isBlank() ? "Não informado":param.getValue().getMobileNumber()));
        patient_address.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAddress().isBlank() ? "Não informado" : param.getValue().getAddress()));
        patient_profissional.setCellValueFactory(param -> new SimpleStringProperty(doctor.getFullName()));
        patient_status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));

        Callback<TableColumn<PatientDAO, String>, TableCell<PatientDAO, String>> cellFactory = (TableColumn<PatientDAO, String> param) -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Button editButton = createButton("Editar");
                    Button removeButton = createButton("Remover");

                    editButton.setOnAction((ActionEvent event) -> {
                        PatientDAO patient = patient_table.getSelectionModel().getSelectedItem();
                        int num = patient_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        newOrEditPatient(patient);
                    });

                    removeButton.setOnAction((ActionEvent event) -> {
                        PatientDAO patient = patient_table.getSelectionModel().getSelectedItem();
                        int num = patient_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        deleteInfo(patient);
                    });

                    HBox manageBtn = new HBox(editButton, removeButton);
                    manageBtn.setAlignment(Pos.CENTER);
                    manageBtn.setSpacing(5);
                    setGraphic(manageBtn);
                    setText(null);
                }

            }
        };

        patient_action.setCellFactory(cellFactory);
        patient_table.setItems(dashboardGetData);
    }

    protected void secretaryDisplayData(List<SecretaryDAO> secretaries) {
        ObservableList<SecretaryDAO> dashboardGetData = FXCollections.observableList(secretaries);
        secretary_table_id.setCellValueFactory(param -> new SimpleStringProperty(""+param.getValue().getId()));
        secretary_table_register_id.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSecretaryId()));
        secretary_table_fullname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        secretary_table_genre.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGender().getName()));
        secretary_table_phone.setCellValueFactory(param -> new SimpleStringProperty(!(Objects.equals(param.getValue().getMobileNumber(), "null") || param.getValue().getMobileNumber().isBlank()) ? param.getValue().getMobileNumber() : "Não informado"));
        secretary_table_email.setCellValueFactory(param -> new SimpleStringProperty(!Objects.equals(param.getValue().getEmail(), "null") ? param.getValue().getEmail() : "Não informado"));
        secretary_table_status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));
        secretary_table_address.setCellValueFactory(param -> new SimpleStringProperty(!(Objects.equals(param.getValue().getAddress(), "null") || param.getValue().getAddress().isBlank()) ? param.getValue().getAddress() : "Não informado"));

        Callback<TableColumn<SecretaryDAO, String>, TableCell<SecretaryDAO, String>> cellFactory = (TableColumn<SecretaryDAO, String> param) -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Button editButton = createButton("Editar");
                    Button removeButton = createButton("Remover");

                    editButton.setOnAction((ActionEvent event) -> {
                        SecretaryDAO secretary = secretary_table.getSelectionModel().getSelectedItem();
                        int num = secretary_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        newOrEditSecretary(secretary);
                    });

                    removeButton.setOnAction((ActionEvent event) -> {
                        UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
                        SecretaryDAO secretary = secretary_table.getSelectionModel().getSelectedItem();
                        int num = secretary_table.getSelectionModel().getSelectedIndex();

                        if ((num - 1) < -1) {
                            AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                            return;
                        }

                        deleteInfo(secretary);
                        secretaryDisplayData(userDAO.getSecretaries());
                    });

                    HBox manageBtn = new HBox(editButton, removeButton);
                    manageBtn.setAlignment(Pos.CENTER);
                    manageBtn.setSpacing(5);
                    setGraphic(manageBtn);
                    setText(null);
                }

            }
        };

        secretary_table_action.setCellFactory(cellFactory);
        secretary_table.setItems(dashboardGetData);
    }

    protected void appointmentDisplayData(List<AppointmentDAO> appointments) {
        ObservableList<AppointmentDAO> dashboardGetData = FXCollections.observableList(appointments);
        appointment_id.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getId())));
        appointment_patient.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatientName()));
        appointment_profissional.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDoctorName()));
        appointment_schedule.setCellValueFactory(param -> new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy").format(param.getValue().getConsultationDate())));
        appointment_hour.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsultationHour()));
        appointment_consult.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsultType().getName()));
        appointment_stats.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus().getName()));

        if (appointment_action != null) {
            Callback<TableColumn<AppointmentDAO, String>, TableCell<AppointmentDAO, String>> cellFactory = (TableColumn<AppointmentDAO, String> param) -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button editButton = createButton("Compareceu");
                        Button removeButton = createButton("Desmarcar");

                        editButton.setOnAction((ActionEvent event) -> {
                            UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
                            AppointmentDAO appointment = appointment_table.getSelectionModel().getSelectedItem();
                            int num = appointment_table.getSelectionModel().getSelectedIndex();

                            if ((num - 1) < -1) {
                                AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                                return;
                            }

                            setAppointmentStatus(appointment, StatusAppointment.PRESENT);
                            appointmentDisplayData(userDAO.getAppointments());
                        });

                        removeButton.setOnAction((ActionEvent event) -> {
                            UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
                            AppointmentDAO appointment = appointment_table.getSelectionModel().getSelectedItem();
                            int num = appointment_table.getSelectionModel().getSelectedIndex();

                            if ((num - 1) < -1) {
                                AlertMessage.errorMessage("Selecione um item primeiro, por favor");
                                return;
                            }

                            setAppointmentStatus(appointment, StatusAppointment.CANCELED);
                            appointmentDisplayData(userDAO.getAppointments());
                        });

                        HBox manageBtn = new HBox(editButton, removeButton);
                        manageBtn.setAlignment(Pos.CENTER);
                        manageBtn.setSpacing(5);
                        setGraphic(manageBtn);
                        setText(null);
                    }

                }
            };

            appointment_action.setCellFactory(cellFactory);
        }
        appointment_table.setItems(dashboardGetData);
    }

    private ObservableList<String> doctors(UserDAO userDAO) {
        List<String> doctorsName = userDAO.getDoctors().stream().map(DoctorDAO::getFullName).toList();
        return FXCollections.observableList(doctorsName);
    }

    protected void newOrEditSecretary(SecretaryDAO secretaryDAO) {
        UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
        if (secretaryDAO == null) {
            secretary_register_id_textfield.setText(generateId("SID", userDAO.getSecretaries()));
            secretary_genre_combobox.setItems(genreList());
            secretary_genre_combobox.getSelectionModel().selectFirst();

            secretary_add_btn.setText("Cadastrar");
            secretary_add_btn.setDisable(false);
            return;
        }

        secretary_list_form.setVisible(false);
        secretary_new_form.setVisible(true);

        secretary_register_id_textfield.setText(secretaryDAO.getSecretaryId());

        secretary_email_textfield.setText(secretaryDAO.getEmail());
        secretary_password_textfield.setText(secretaryDAO.getPassword());
        secretary_fullname_textfield.setText(secretaryDAO.getFullName());

        secretary_genre_combobox.setItems(genreList());
        secretary_genre_combobox.getSelectionModel().select(secretaryDAO.getGender().getName());

        secretary_add_btn.setText("Editar");
        secretary_add_btn.setDisable(false);
    }

    protected void newOrEditDoctor(DoctorDAO doctorDAO) {
        UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
        if (doctorDAO == null) {
            pro_register_id_textfield.setText(generateId("DID", userDAO.getDoctors()));
            daysList();
            listHourDoctor(userDAO);
            pro_genre_combobox.setItems(genreList());
            pro_genre_combobox.getSelectionModel().selectFirst();

            pro_add_btn.setText("Cadastrar");
            pro_add_btn.setDisable(false);
            return;
        }

        pro_list_form.setVisible(false);
        pro_new_form.setVisible(true);

        pro_register_id_textfield.setText(doctorDAO.getDoctorId());

        daysList();
        listHourDoctor(userDAO);

        pro_email_textfield.setText(doctorDAO.getEmail());
        pro_password_textfield.setText(doctorDAO.getPassword());
        pro_fullname_textfield.setText(doctorDAO.getFullName());

        pro_genre_combobox.setItems(genreList());
        pro_genre_combobox.getSelectionModel().select(doctorDAO.getGender().getName());

        pro_add_btn.setText("Editar");
        pro_add_btn.setDisable(false);
    }

    protected void newOrEditPatient(PatientDAO patientDAO) {
        UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
        updateDoctors(userDAO, true);
        patient_genre_dropdown.setItems(genreList());
        if (patientDAO == null) {
            if (patient_responsible_dropdown != null) {
                patient_responsible_dropdown.setItems(doctors(userDAO));
                patient_responsible_dropdown.getSelectionModel().selectFirst();
            }
            patient_genre_dropdown.getSelectionModel().selectFirst();
            patient_add_btn.setText("Adicionar");
            return;
        }

        patient_list_form.setVisible(false);
        patient_new_form.setVisible(true);

        String fullName = patientDAO.getFullName();
        String address = patientDAO.getAddress();
        String contact = patientDAO.getMobileNumber();
        Genders genders = patientDAO.getGender();

        String doctorName = patientDAO.getDoctorName(userDAO.getDoctors());
        if (patient_responsible_dropdown != null) {
            patient_responsible_dropdown.setItems(doctors(userDAO));
            patient_responsible_dropdown.getSelectionModel().select(doctorName);
        }

        patient_name_textfield.setText(fullName);
        patient_address_textfield.setText(address);
        patient_contact_textfield.setText(contact);

        patient_genre_dropdown.getSelectionModel().select(genders.getName());


        if (patient_progress_textfield != null) {
            TreatmentDAO treatmentDAO = patientDAO.getTreatment();
            patient_feedback_textfield.setText(treatmentDAO.getFeedback());
            patient_plan_textfield.setText(treatmentDAO.getPlan());
            patient_progress_textfield.setText(treatmentDAO.getProgress());
            LocalDate conclusion = treatmentDAO.getConclusionAt() != null ?
                    new Date(treatmentDAO.getConclusionAt().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate() :
                    null;
            patient_conclusion_date.setValue(conclusion);
        }

        if (patient_observations_textfield != null) {
            DiagnosisDAO diagnosisDAO = patientDAO.getDiagnosis();
            patient_observations_textfield.setText(diagnosisDAO.getComments());
            patient_tests_textfield.setText(diagnosisDAO.getExams());
            LocalDate lastUpdate = diagnosisDAO.getUpdatedAt() != null ?
                    new Date(diagnosisDAO.getUpdatedAt().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate() :
                    null;
            patient_last_update_date.setValue(lastUpdate);

            if (lastUpdate == null) patient_last_lbl.setText("------------");
            else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
                patient_last_lbl.setText(simpleDateFormat.format(diagnosisDAO.getUpdatedAt()));
            }
        }

        LocalDate birthday = new Date(patientDAO.getBirthday().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate();

        patient_birthday_date.setValue(birthday);

        Period period = Period.between(birthday, LocalDate.now());
        int age = period.getYears();

        patient_id_lbl.setText(patientDAO.getPatientId());
        patient_age_lbl.setText(age + " anos");
        patient_responsable_lbl.setText(doctorName);
        patient_name_lbl.setText(fullName);
        patient_genre_lbl.setText(genders.getName());
        patient_contact_lbl.setText(contact.isBlank() ? "Não informado" : contact);
        patient_address_lbl.setText(address.isBlank() ? "Não informado" : address);

        patient_add_btn.setText("Editar");
        patient_add_btn.setDisable(false);
    }

    protected void newOrEditAppointment() {
        appointment_patient_textfield.setDisable(false);
        appointment_pro_textfield.setDisable(true);
        appointment_schedule_date.setDisable(true);
        appointment_hour_dropdown.setDisable(true);
        appointment_type_dropdown.setDisable(true);

        appointment_mark_btn.setText("Marcar");
        appointment_mark_btn.setDisable(true);

        appointment_type_dropdown.setItems(typeList());
        appointment_type_dropdown.getSelectionModel().selectFirst();
    }

    protected void deleteInfo(ExtendedDAO extendedDAO) {
        UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
        if (extendedDAO instanceof DoctorDAO) {
            userDAO.getDoctors().remove(extendedDAO);
        } else if (extendedDAO instanceof PatientDAO) {
            userDAO.getPatients().remove(extendedDAO);
        } else if (extendedDAO instanceof SecretaryDAO) {
            userDAO.getSecretaries().remove(extendedDAO);
        }
        extendedDAO.setStatus(Status.DELETED);
        extendedDAO.save(Rows.of("id", extendedDAO.getId()));
    }

    protected void setAppointmentStatus(AppointmentDAO appointment, StatusAppointment statusAppointment) {
        UserDAO userDAO = Manager.getManager(UserManager.class).getUser();
        userDAO.getAppointments()
                .stream()
                .filter(f -> f.getId() == appointment.getId())
                .forEach(f -> f.setStatus(statusAppointment));
        appointment.setStatus(statusAppointment);
        appointment.save(Rows.of("id", appointment.getId()));
    }

    protected void daysList() {
        List<String> days = new ArrayList<>(Stream.of(Days.values()).map(Days::getDayInPT).toList());
        ObservableList<String> listData = FXCollections.observableList(days);
        pro_day_combobox.setItems(listData);

        List<String> hours = getDailyTimeSlots();
        listData = FXCollections.observableList(hours);
        pro_hour_combobox.setItems(listData);
        pro_hour_combobox.getSelectionModel().selectFirst();
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

    protected void listHourDoctor(UserDAO adminDAO) {
        if (adminDAO instanceof DoctorDAO) {
            DoctorDAO doctor = (DoctorDAO) adminDAO;
            Map<Days, List<String>> dataMap = doctor.getAvailable();
            pro_monday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.MONDAY));
            pro_tuesday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.TUESDAY));
            pro_wednesday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.WEDNESDAY));
            pro_thursday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.THURSDAY));
            pro_friday_table.setCellValueFactory(param -> param.getValue().getDayColumn(Days.FRIDAY));
            pro_days_table.setItems(FXCollections.observableArrayList(createGroupedSchedules(dataMap)));
            return;
        }
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

    protected abstract void update(boolean verify);

    protected Button createButton(String name) {
        Button button = new Button(name);
        button.setStyle("-fx-background-color: #99cc66;\n"
                + "    -fx-cursor: hand;\n"
                + "    -fx-text-fill: #fff;\n"
                + "    -fx-font-size: 14px;\n"
                + "    -fx-font-family: Arial;");
        return button;
    }

    protected void updateProfileInfo() {
        UserDAO adminDAO = getUserManager().getUser();
        profile_fullname_lbl.setText(adminDAO.getFullName());
        profile_email_lbl.setText(adminDAO.getEmail());
        profile_genre_lbl.setText(adminDAO.getGender().getName());

        if (adminDAO.getImage() != null && !adminDAO.getImage().isBlank() && !adminDAO.getImage().equalsIgnoreCase("null")) {
            circle_pfp.setFill(new ImagePattern(getUserManager().getImage()));
            profile_pfp_circle.setFill(new ImagePattern(getUserManager().getImage()));
        }
    }

    public UserManager getUserManager() {
        return Manager.getManager(UserManager.class);
    }

}
