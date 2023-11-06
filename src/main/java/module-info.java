module br.estacio.consultasapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires static lombok;

    requires org.kordamp.bootstrapfx.core;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.jfoenix;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires br.com.timer;

    opens br.estacio.consultasapp to javafx.fxml;

    opens br.estacio.consultasapp.controller to javafx.fxml;
    opens br.estacio.consultasapp.controller.login to javafx.fxml;
    opens br.estacio.consultasapp.controller.login.types to javafx.fxml;
    opens br.estacio.consultasapp.controller.form to javafx.fxml;
    opens br.estacio.consultasapp.controller.form.types to javafx.fxml;

    opens br.estacio.consultasapp.user.dao to br.com.timer;

    exports br.estacio.consultasapp;

    exports br.estacio.consultasapp.controller;
    exports br.estacio.consultasapp.controller.login;
    exports br.estacio.consultasapp.controller.login.types;
    exports br.estacio.consultasapp.controller.form;
    exports br.estacio.consultasapp.controller.form.types;

    exports br.estacio.consultasapp.handler;
    exports br.estacio.consultasapp.database;
    //exports br.estacio.consultasapp.utils;

    exports br.estacio.consultasapp.user;
    exports br.estacio.consultasapp.user.dao;
    exports br.estacio.consultasapp.user.enums;
}