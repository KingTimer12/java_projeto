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
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@TableName(name = "administrators")
public class AdminDAO extends HandlerDAO implements User {

    private boolean loaded;

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String email;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String username;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String password;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 50)
    private Genders gender;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 500)
    private String image;

    @ColumnRow
    private Date createdAt;

    @ColumnRow
    private Date updatedAt;

    private final List<DoctorDAO> doctors = new ArrayList<>();
    private final List<PatientDAO> patients = new ArrayList<>();
    private final List<SecretaryDAO> secretaries = new ArrayList<>();
    private final List<AppointmentDAO> appointments = new ArrayList<>();

    public void save() {
        super.save(Rows.of("username", this.username));
    }

    public void load() {
        super.load(Rows.of("id", this.id));
        loaded = true;
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
