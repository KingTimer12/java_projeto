package br.estacio.consultasapp.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    ACTIVE("Ativo"), DISABLE("Desativado"), DELETED("Deletado");

    private final String name;

    public static Status getStatus(String name) {
        for (Status status : values()) {
            if (status.getName().equalsIgnoreCase(name))
                return status;
        }
        return Status.ACTIVE;
    }
}
