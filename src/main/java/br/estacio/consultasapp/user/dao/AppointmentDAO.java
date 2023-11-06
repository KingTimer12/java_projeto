package br.estacio.consultasapp.user.dao;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Rows;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.enums.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@TableName(name = "appointments")
public class AppointmentDAO extends HandlerDAO {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "patient_id")
    private int patientId;
    @ColumnRow(field = "doctor_id")
    private int doctorId;
    @ColumnRow(field = "consultation_date")
    private Date consultationDate;
    @ColumnRow(field = "consultation_hour")
    private String consultationHour;
    @ColumnRow
    private Status status;

    private String patientName;
    private String doctorName;

    public void loadPatientName() {
        PatientDAO patient = PatientDAO.builder().id(patientId).build();
        patient.load();
        this.patientName = patient.getFullName();
    }

    public void loadDoctorName() {
        DoctorDAO doctor = DoctorDAO.builder().id(doctorId).build();
        doctor.load();
        this.doctorName = doctor.getFullName();
    }

    public void save() {
        super.save(Rows.of("patient_id", patientId), Rows.of("doctor_id", doctorId));
    }

    public void load() {
        super.load(Rows.of("id", this.id));
    }

    @Override
    public DBCollector<?> getDatabase() {
        return Manager.getManager(DatabaseManager.class).getMySQL();
    }

    @Override
    public HandlerDAO getHandle() {
        return this;
    }

}
