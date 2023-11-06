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
import lombok.*;

import java.util.Date;

@Getter
@Builder
@TableName(name = "doctors")
public class DoctorDAO extends HandlerDAO implements User {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "doctor_id", typeField = TypeField.VARCHAR, size = 100)
    private String doctorId;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String email;

    @ColumnRow(field = "full_name", typeField = TypeField.VARCHAR, size = 100)
    private String fullName;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String username;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String password;

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

    @ColumnRow
    private Date createdAt;

    @ColumnRow
    private Date updatedAt;

    @ColumnRow(field = "time_id")
    private int timeId;



    public void save() {
        super.save(Rows.of("doctor_id", this.doctorId));
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
