package br.estacio.consultasapp.user;

import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.interfaces.IStatus;

public interface User extends IStatus {

    String getUsername();
    String getFullName();
    String getPassword();
    String getImage();
    String getEmail();
    Genders getGender();

    boolean isLoaded();
    void load();

}
