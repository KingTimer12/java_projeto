package br.estacio.consultasapp.user;

import br.estacio.consultasapp.user.dao.AppointmentDAO;
import br.estacio.consultasapp.user.dao.DoctorDAO;
import br.estacio.consultasapp.user.dao.PatientDAO;
import br.estacio.consultasapp.user.dao.SecretaryDAO;
import br.estacio.consultasapp.user.interfaces.IStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class UserDAO extends ExtendedDAO implements User {

    private final List<DoctorDAO> doctors = new ArrayList<>();
    private final List<PatientDAO> patients = new ArrayList<>();
    private final List<SecretaryDAO> secretaries = new ArrayList<>();
    private final List<AppointmentDAO> appointments = new ArrayList<>();

}
