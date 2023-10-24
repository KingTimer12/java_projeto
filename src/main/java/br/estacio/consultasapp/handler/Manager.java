package br.estacio.consultasapp.handler;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Manager {

    @Getter
    private static final Set<Manager> managers = new HashSet<>();
    public static void addManager(Manager... manager) {
        managers.addAll(Arrays.asList(manager));
    }

    public abstract void start();

    @SneakyThrows
    public static <T extends Manager> T getManager(Class<T> manager) {
        return (T) managers.stream()
                .filter(m -> m.getClass().getName().equals(manager.getName()))
                .findFirst()
                .orElse(manager.getConstructor().newInstance());
    }

}
