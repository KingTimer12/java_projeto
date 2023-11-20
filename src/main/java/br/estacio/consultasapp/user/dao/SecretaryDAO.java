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
import br.estacio.consultasapp.user.ExtendedDAO;
import br.estacio.consultasapp.user.User;
import br.estacio.consultasapp.user.UserDAO;
import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.enums.Status;
import br.estacio.consultasapp.user.interfaces.IdInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@TableName(name = "secretaries")
public class SecretaryDAO extends UserDAO implements User {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "secretary_id", typeField = TypeField.VARCHAR, size = 100)
    private String secretaryId;

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
    private boolean loaded;

    public void save() {
        super.save(Rows.of("secretary_id", this.secretaryId));
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public void load() {
        this.loaded = true;
        super.load(Rows.of("id", this.id));
    }

    public void loadRegisterId() {
        this.loaded = true;
        super.load(Rows.of("secretary_id", this.secretaryId));
    }

    @Override
    public String getOtherId() {
        return secretaryId;
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
