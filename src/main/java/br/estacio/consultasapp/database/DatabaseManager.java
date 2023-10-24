package br.estacio.consultasapp.database;

import br.com.timer.collectors.DBCollector;
import br.com.timer.interfaces.DAO;
import br.com.timer.objects.DBCollectors;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.dao.UserDAO;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class DatabaseManager extends Manager {

    private DBCollector<MySQL> mySQL;
    private final Set<Class<? extends DAO>> classesDAO = new HashSet<>();

    @Override
    public void start() {
        this.mySQL = DBCollectors.create(new MySQL("localhost", 3306, "root", "", "schedule_manager"));
        classesDAO.forEach(classDAO -> this.mySQL.getHandler().table(classDAO));

        UserDAO userDAO = new UserDAO(0, "admin", "JAm1l31@3");
        userDAO.save();
    }

    public void registerDAO(Class<? extends DAO> clazz) {
        classesDAO.add(clazz);
    }

}
