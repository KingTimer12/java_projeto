package br.estacio.consultasapp.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Consults {

    START("Avaliação Inicial"),
    PLAN_GOALS("Plano/metas de Tratamento"),
    DIAGNOSIS("Diagnóstico"),
    REVALUATION("Reavaliação e Ajuste do Plano de Tratamento"),
    SPECIAL("Condição Especial");

    private final String name;

    public static Consults get(String name) {
        for (Consults consult : values()) {
            if (consult.getName().equalsIgnoreCase(name))
                return consult;
        }
        return Consults.SPECIAL;
    }

}
