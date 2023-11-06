package br.estacio.consultasapp.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    CONFIRM("Confirmar"), ACTIVE("Ativo");

    private final String name;
}
