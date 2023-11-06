package br.estacio.consultasapp.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genders {
    NONE("Nenhum"), MALE("Masculino"), FEMALE("Feminino"), OTHERS("Outro");

    private final String name;

    public static Genders getGender(String name) {
        for (Genders gender : values()) {
            if (gender.getName().equalsIgnoreCase(name))
                return gender;
        }
        return Genders.NONE;
    }
}
