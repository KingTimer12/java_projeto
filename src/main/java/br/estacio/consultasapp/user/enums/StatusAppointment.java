package br.estacio.consultasapp.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusAppointment {
    CANCELED("Desmarcado"), PRESENT("Compareceu"), ACTIVE("Ativo");

    private final String name;
}
