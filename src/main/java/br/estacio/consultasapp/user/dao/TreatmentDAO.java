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
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
@TableName(name = "treatment")
public class TreatmentDAO extends HandlerDAO {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "patient_id")
    private int patientId;
    @ColumnRow(isNull = true)
    private Date conclusionAt;
    @ColumnRow
    private String feedback;
    @ColumnRow
    private String progress;
    @ColumnRow
    private String plan;

    public void save() {
        super.save(Rows.of("patient_id", patientId));
    }

    public void load() {
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

}
