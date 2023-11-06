package br.estacio.consultasapp.utils;

import br.estacio.consultasapp.user.enums.Days;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class WeeklySchedule {

    private final Map<Days, SimpleStringProperty> dayColumns;

    public WeeklySchedule() {
        dayColumns = new HashMap<>();
        for (Days day : Days.values()) {
            dayColumns.put(day, new SimpleStringProperty());
        }
    }

    public SimpleStringProperty getDayColumn(Days day) {
        return dayColumns.get(day);
    }
}
