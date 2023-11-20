package br.estacio.consultasapp.user;

import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.dao.*;
import javafx.scene.image.Image;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserManager extends Manager {

    private UserDAO user;

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

    public String getFullName() {
        return this.user.getFullName();
    }

    public String getName() {
        if (this.user.getUsername() == null ||
                this.user.getUsername().isBlank() ||
                this.user.getUsername().equalsIgnoreCase("null"))
            return this.getFullName();
        return this.user.getUsername();
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public String getImagePath() {
        return this.user.getImage();
    }

    public Image getImage() {
        File file = new File(this.getImagePath());
        return new Image(file.toURI().toString(), 137, 95, false, true);
    }
}
