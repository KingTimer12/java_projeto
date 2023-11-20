package br.estacio.consultasapp.controller.form;

import br.estacio.consultasapp.user.dao.AppointmentDAO;
import br.estacio.consultasapp.user.dao.DoctorDAO;
import br.estacio.consultasapp.user.dao.PatientDAO;
import br.estacio.consultasapp.user.dao.SecretaryDAO;
import br.estacio.consultasapp.utils.WeeklySchedule;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class GeneralVariables {

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
    protected TableColumn<AppointmentDAO, String> appointment_action;

    @FXML
    protected TableColumn<?, ?> appointment_date;

    @FXML
    protected TableColumn<?, ?> appointment_diagnosis;

    @FXML
    protected TableColumn<AppointmentDAO, String> appointment_id;

    @FXML
    protected TableColumn<AppointmentDAO, String> appointment_patient;

    @FXML
    protected TableColumn<AppointmentDAO, String> appointment_profissional;

    @FXML
    protected TableColumn<AppointmentDAO, String> appointment_stats;

    @FXML
    protected TableView<AppointmentDAO> appointment_table;

    @FXML
    protected TableColumn<?, ?> appointment_treatment;
    //Appointment Form - END

    //Patient Form -- START
    @FXML
    protected AnchorPane patient_form;

    @FXML
    protected TableColumn<PatientDAO, String> patient_action;

    @FXML
    protected TableColumn<PatientDAO, String> patient_address;


    @FXML
    protected TableColumn<PatientDAO, String> patient_fullname;

    @FXML
    protected TableColumn<PatientDAO, String> patient_genre;

    @FXML
    protected TableColumn<PatientDAO, String> patient_id;

    @FXML
    protected TableColumn<PatientDAO, String> patient_phone;

    @FXML
    protected TableColumn<PatientDAO, String> patient_profissional;

    @FXML
    protected TableColumn<PatientDAO, String> patient_status;

    @FXML
    protected TableView<PatientDAO> patient_table;

    //Patient Form - END

    @FXML
    protected Button pro_add_btn;

    @FXML
    protected Button pro_back_btn;

    @FXML
    protected TextField pro_email_textfield;

    @FXML
    protected AnchorPane pro_form;

    @FXML
    protected TextField pro_fullname_textfield;

    @FXML
    protected ComboBox<String> pro_genre_combobox;

    @FXML
    protected AnchorPane pro_list_form;

    @FXML
    protected Button pro_new_btn;

    @FXML
    protected AnchorPane pro_new_form;

    @FXML
    protected TextField pro_password_textfield;

    @FXML
    protected Button pro_refresh_btn;

    @FXML
    protected TextField pro_register_id_textfield;

    @FXML
    protected Button pro_search_btn;

    @FXML
    protected TextField pro_search_textfield;

    @FXML
    protected TableView<DoctorDAO> pro_table;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_action;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_address;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_email;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_fullname;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_genre;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_id;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_phone;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_register_id;

    @FXML
    protected TableColumn<DoctorDAO, String> pro_table_status;

    @FXML
    protected Button pro_day_add_btn;

    @FXML
    protected ComboBox<String> pro_hour_combobox;

    @FXML
    protected ComboBox<String> pro_day_combobox;

    @FXML
    protected Button pro_day_remove_btn;

    @FXML
    protected TableView<WeeklySchedule> pro_days_table;

    @FXML
    protected TableColumn<WeeklySchedule, String> pro_monday_table;

    @FXML
    protected TableColumn<WeeklySchedule, String> pro_tuesday_table;

    @FXML
    protected TableColumn<WeeklySchedule, String> pro_wednesday_table;

    @FXML
    protected TableColumn<WeeklySchedule, String> pro_thursday_table;

    @FXML
    protected TableColumn<WeeklySchedule, String> pro_friday_table;

    @FXML
    protected Button btn_pro;

    //Secretary -- START
    @FXML
    protected Button btn_secretary;

    @FXML
    protected Button secretary_add_btn;

    @FXML
    protected Button secretary_back_btn;

    @FXML
    protected TextField secretary_email_textfield;

    @FXML
    protected AnchorPane secretary_form;

    @FXML
    protected TextField secretary_fullname_textfield;

    @FXML
    protected ComboBox<String> secretary_genre_combobox;

    @FXML
    protected AnchorPane secretary_list_form;

    @FXML
    protected Button secretary_new_btn;

    @FXML
    protected AnchorPane secretary_new_form;

    @FXML
    protected TextField secretary_password_textfield;

    @FXML
    protected Button secretary_refresh_btn;

    @FXML
    protected TextField secretary_register_id_textfield;

    @FXML
    protected Button secretary_search_btn;

    @FXML
    protected TextField secretary_search_textfield;

    @FXML
    protected TableView<SecretaryDAO> secretary_table;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_action;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_address;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_email;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_fullname;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_genre;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_id;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_phone;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_register_id;

    @FXML
    protected TableColumn<SecretaryDAO, String> secretary_table_status;
    //Secretary -- END

    //Appointment -- START
    @FXML
    protected Button appointment_back_btn;

    @FXML
    protected TableColumn<AppointmentDAO, String> appointment_hour;

    @FXML
    protected ComboBox<String> appointment_hour_dropdown;

    @FXML
    protected AnchorPane appointment_list_form;

    @FXML
    protected Button appointment_mark_btn;

    @FXML
    protected Button appointment_confirm_btn;

    @FXML
    protected Button appointment_new_btn;

    @FXML
    protected AnchorPane appointment_new_form;

    @FXML
    protected TextField appointment_patient_textfield;

    @FXML
    protected TextField appointment_pro_textfield;

    @FXML
    protected Button appointment_refresh_btn;

    @FXML
    protected DatePicker appointment_schedule_date;

    @FXML
    protected Button appointment_search_btn;

    @FXML
    protected TextField appointment_search_textfield;


    @FXML
    protected TableColumn<AppointmentDAO, String> appointment_schedule;

    @FXML
    protected TableColumn<AppointmentDAO, String> appointment_consult;


    @FXML
    protected ComboBox<String> appointment_type_dropdown;


    @FXML
    protected Button patient_add_btn;

    @FXML
    protected AnchorPane patient_new_form;


    @FXML
    protected Label patient_address_lbl;

    @FXML
    protected TextField patient_address_textfield;

    @FXML
    protected Label patient_age_lbl;

    @FXML
    protected Button patient_back_btn;

    @FXML
    protected DatePicker patient_birthday_date;

    @FXML
    protected DatePicker patient_conclusion_date;

    @FXML
    protected Button patient_confirm_btn;

    @FXML
    protected Label patient_contact_lbl;

    @FXML
    protected TextField patient_contact_textfield;


    @FXML
    protected TextArea patient_feedback_textfield;


    @FXML
    protected ComboBox<String> patient_genre_dropdown;

    @FXML
    protected ComboBox<String> patient_status_dropdown;

    @FXML
    protected Label patient_genre_lbl;


    @FXML
    protected TableColumn<PatientDAO, String> patient_register_id;

    @FXML
    protected Label patient_id_lbl;

    @FXML
    protected Label patient_last_lbl;

    @FXML
    protected DatePicker patient_last_update_date;

    @FXML
    protected AnchorPane patient_list_form;

    @FXML
    protected Label patient_name_lbl;

    @FXML
    protected TextField patient_name_textfield;

    @FXML
    protected Button patient_new_btn;

    @FXML
    protected TextArea patient_observations_textfield;


    @FXML
    protected TextArea patient_plan_textfield;


    @FXML
    protected TextArea patient_progress_textfield;

    @FXML
    protected Button patient_refresh_btn;

    @FXML
    protected Label patient_responsable_lbl;

    @FXML
    protected ComboBox<String> patient_responsible_dropdown;

    @FXML
    protected Button patient_search_btn;

    @FXML
    protected TextField patient_search_textfield;


    @FXML
    protected TextArea patient_tests_textfield;

    @FXML
    protected Label lbl_username;

    @FXML
    protected Label profile_created_account_lbl;

    @FXML
    protected Label profile_email_lbl;

    @FXML
    protected TextField profile_email_textfield;

    @FXML
    protected TextField profile_contact_textfield;

    @FXML
    protected TextField profile_address_textfield;

    @FXML
    protected AnchorPane profile_form;

    @FXML
    protected Label profile_fullname_lbl;

    @FXML
    protected Label profile_genre_lbl;

    @FXML
    protected TextField profile_fullname_textfield;

    @FXML
    protected ComboBox<String> profile_genre_select;

    @FXML
    protected Button profile_import_btn;

    @FXML
    protected TextField profile_newpassword_textfield;

    @FXML
    protected PasswordField profile_oldpassword_textfield;

    @FXML
    protected Circle profile_pfp_circle;

    @FXML
    protected Button profile_save_btn;

    @FXML
    protected Button logout_btn;

    @FXML
    protected Label page_name;

    @FXML
    protected AnchorPane profile_config_normal;

    @FXML
    protected AnchorPane profile_config_hour;

    @FXML
    protected Button pro_back_new_btn;

    @FXML
    protected Button profile_hour_btn;

}
