package br.estacio.consultasapp.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    DISABLE("Desativado"), ACTIVE("Ativo");

    private final String name;
}
