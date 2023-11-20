package br.estacio.consultasapp.database;

import br.com.timer.collectors.DBCollector;
import br.com.timer.interfaces.DAO;
import br.com.timer.objects.DBCollectors;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.dao.DoctorDAO;
import br.estacio.consultasapp.user.dao.AdminDAO;
import br.estacio.consultasapp.user.enums.Genders;
import lombok.Getter;

import java.util.Date;
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

        AdminDAO defaultUser = AdminDAO.builder().username("admin").build();
        defaultUser.load(Rows.of("username", defaultUser.getUsername()));
        System.out.println(defaultUser.getId());
        if (defaultUser.getId() == 0) {
            defaultUser = AdminDAO.builder()
                    .username("admin")
                    .fullName("Admin")
                    .email("admin@admin.com")
                    .gender(Genders.NONE)
                    .password("admin1234")
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();
            defaultUser.save();
        }
    }

    public void registerDAO(Class<? extends DAO> clazz) {
        classesDAO.add(clazz);
    }

}
