package br.estacio.consultasapp.controller.login;

import javafx.fxml.FXML;

import java.io.IOException;

public interface ILogin {

    void close();

    void login() throws IOException;

    void loginShowPassword();


}
