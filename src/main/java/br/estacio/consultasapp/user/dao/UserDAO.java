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
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@TableName(name = "user")
public class UserDAO extends HandlerDAO {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "username", typeField = TypeField.TEXT)
    private String name;

    @ColumnRow(field = "password", typeField = TypeField.TEXT)
    private String password;

    public void save() {
        super.save(Rows.of("username", this.name));
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
