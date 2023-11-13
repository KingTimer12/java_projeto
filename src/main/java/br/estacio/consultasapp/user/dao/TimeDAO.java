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

@Getter
@Setter
@Builder
@TableName(name = "time")
public class TimeDAO extends HandlerDAO {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "doctor_id")
    private int doctorId;
    @ColumnRow
    private String monday;
    @ColumnRow
    private String tuesday;
    @ColumnRow
    private String wednesday;
    @ColumnRow
    private String thursday;
    @ColumnRow
    private String friday;

    public void save() {
        super.save(Rows.of("doctor_id", doctorId));
    }

    public void load() {
        super.load(Rows.of("id", id));
    }

    public void loadDoctorId() {
        super.load(Rows.of("doctor_id", doctorId));
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
