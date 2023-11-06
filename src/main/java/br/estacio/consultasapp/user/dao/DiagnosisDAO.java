package br.estacio.consultasapp.user.dao;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Rows;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@TableName(name = "diagnosis")
public class DiagnosisDAO extends HandlerDAO {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "patient_id")
    private int patientId;
    @ColumnRow
    private Date updatedAt;
    @ColumnRow
    private String comments;
    @ColumnRow
    private String exams;

    public void save() {
        super.save(Rows.of("patient_id", patientId));
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
