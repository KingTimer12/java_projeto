package br.estacio.consultasapp.user;

import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.dao.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserManager extends Manager {

    private User user;

    @Override
    public void start() {
        getManager(DatabaseManager.class).registerDAO(AdminDAO.class);
        getManager(DatabaseManager.class).registerDAO(DoctorDAO.class);
        getManager(DatabaseManager.class).registerDAO(SecretaryDAO.class);
        getManager(DatabaseManager.class).registerDAO(PatientDAO.class);
        getManager(DatabaseManager.class).registerDAO(DiagnosisDAO.class);
        getManager(DatabaseManager.class).registerDAO(TreatmentDAO.class);
        getManager(DatabaseManager.class).registerDAO(AppointmentDAO.class);
        getManager(DatabaseManager.class).registerDAO(TimeDAO.class);
    }

    public int getId() {
        return this.user.getId();
    }

    public String getName() {
        return this.user.getUsername().substring(0, 1).toUpperCase() + this.user.getUsername().substring(1);
    }

    public String getPassword() {
        return this.user.getPassword();
    }
}
