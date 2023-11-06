package br.estacio.consultasapp.user.dao;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Rows;
import br.com.timer.objects.rows.TypeField;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.User;
import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.enums.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@TableName(name = "patients")
public class PatientDAO extends HandlerDAO {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "full_name", typeField = TypeField.VARCHAR, size = 100)
    private String fullName;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 50)
    private Genders gender;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 50)
    private Status status;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 64)
    private String address;

    @ColumnRow(field = "mobile_number",typeField = TypeField.VARCHAR, size = 50)
    private String mobileNumber;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 500)
    private String image;

    @ColumnRow(field = "diagnosis_id")
    private int diagnosisId;

    @ColumnRow(field = "treatment_id")
    private int treatmentId;

    @ColumnRow(field = "doctor_id")
    private int doctorId;

    @ColumnRow
    private Date createdAt;

    @ColumnRow
    private Date updatedAt;

    private DiagnosisDAO diagnosis;
    private TreatmentDAO treatment;

    public DiagnosisDAO diagnosis(DiagnosisDAO diagnosis) {
        if (this.diagnosis == null && diagnosis != null) {
            return this.diagnosis = diagnosis;
        }
        this.diagnosis = DiagnosisDAO.builder().id(diagnosisId).build();
        this.diagnosis.load();
        return this.diagnosis;
    }

    public TreatmentDAO treatment(TreatmentDAO treatment) {
        if (this.treatment == null && treatment != null) {
            return this.treatment = treatment;
        }
        this.treatment = TreatmentDAO.builder().id(treatmentId).build();
        this.treatment.load();
        return this.treatment;
    }

    public void save() {
        super.save(Rows.of("full_name", this.fullName));
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
