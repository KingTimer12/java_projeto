package br.estacio.consultasapp.user;

import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.dao.UserDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserManager extends Manager {

    private UserDAO user;

    @Override
    public void start() {
        getManager(DatabaseManager.class).registerDAO(UserDAO.class);
    }

    public void load(int id, String name, String password) {
        setUser(new UserDAO(id, name, password));
    }

    public String getName() {
        return this.user.getName();
    }

    public String getPassword() {
        return this.user.getPassword();
    }
}
