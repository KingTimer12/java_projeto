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
import br.estacio.consultasapp.user.interfaces.IdInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Builder
@TableName(name = "patients")
public class PatientDAO extends HandlerDAO implements IdInterface {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "full_name", typeField = TypeField.VARCHAR, size = 100)
    private String fullName;

    @ColumnRow(field = "patient_id", typeField = TypeField.VARCHAR, size = 100)
    private String patientId;

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
    private Date birthday;

    @ColumnRow
    private Date createdAt;

    @ColumnRow
    private Date updatedAt;

    private DiagnosisDAO diagnosis;
    private TreatmentDAO treatment;

    public String getDoctorName(List<DoctorDAO> list) {
        Optional<String> doctorName = list.stream()
                .filter(doctorDAO -> doctorDAO.getId() == this.doctorId)
                .findAny().map(DoctorDAO::getFullName);
        return doctorName.orElse("Desconhecido");
    }

    public DiagnosisDAO saveDiagnosis(String comments, String exams, Date updatedAt) {
        if (id == 0) {
            loadPatientId();
        }
        this.diagnosis = this.diagnosis == null ? DiagnosisDAO.builder().patientId(id).build() : this.diagnosis;
        this.diagnosis.setComments(comments);
        this.diagnosis.setExams(exams);
        this.diagnosis.setUpdatedAt(updatedAt);
        this.diagnosis.save();
        return this.diagnosis;
    }

    public TreatmentDAO saveTreatment(String plan, String feedback, String progress, Date conclusionAt) {
        if (id == 0) {
            loadPatientId();
        }
        this.treatment = this.treatment == null ? TreatmentDAO.builder().patientId(id).build() : this.treatment;
        this.treatment.setPlan(plan);
        this.treatment.setFeedback(feedback);
        this.treatment.setProgress(progress);
        this.treatment.setConclusionAt(conclusionAt);
        this.treatment.save();
        return this.treatment;
    }

    public void save() {
        super.save(Rows.of("patient_id", this.patientId));
    }

    public void load() {
        super.load(Rows.of("id", this.id));
    }

    public void loadPatientId() {
        super.load(Rows.of("patient_id", this.patientId));
    }

    @Override
    public DBCollector<?> getDatabase() {
        return Manager.getManager(DatabaseManager.class).getMySQL();
    }

    @Override
    public HandlerDAO getHandle() {
        return this;
    }

    @Override
    public String getOtherId() {
        return patientId;
    }
}
