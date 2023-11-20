package br.estacio.consultasapp.user.dao;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Rows;
import br.com.timer.objects.rows.TypeField;
import br.estacio.consultasapp.database.DatabaseManager;
import br.estacio.consultasapp.handler.Manager;
import br.estacio.consultasapp.user.ExtendedDAO;
import br.estacio.consultasapp.user.User;
import br.estacio.consultasapp.user.UserDAO;
import br.estacio.consultasapp.user.enums.Days;
import br.estacio.consultasapp.user.enums.Genders;
import br.estacio.consultasapp.user.enums.Status;
import br.estacio.consultasapp.user.interfaces.IdInterface;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@TableName(name = "doctors")
public class DoctorDAO extends UserDAO implements User {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(field = "doctor_id", typeField = TypeField.VARCHAR, size = 100)
    private String doctorId;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String email;

    @ColumnRow(field = "full_name", typeField = TypeField.VARCHAR, size = 100)
    private String fullName;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String username;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 100)
    private String password;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 50)
    private Genders gender;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 50)
    private Status status;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 64)
    private String address;

    @ColumnRow(field = "mobile_number",typeField = TypeField.VARCHAR, size = 50)
    private String mobileNumber;

    @ColumnRow(typeField = TypeField.VARCHAR, size = 500)
    private String image;

    @ColumnRow
    private Date createdAt;

    @ColumnRow
    private Date updatedAt;

    @ColumnRow(field = "time_id")
    private int timeId;

    private boolean loaded;

    private final Map<Days, List<String>> available = new HashMap<>();

    public TimeDAO saveTime() {
        if (id == 0) {
            loadDoctorId();
        }
        Set<Days> days = available.keySet();
        TimeDAO timeDAO = TimeDAO.builder().doctorId(id).build();
        for (Days day : days) {
            List<String> hours = available.get(day);
            String hourString = String.join(";", hours);
            switch (day) {
                case MONDAY -> timeDAO.setMonday(hourString);
                case TUESDAY -> timeDAO.setTuesday(hourString);
                case WEDNESDAY -> timeDAO.setWednesday(hourString);
                case THURSDAY -> timeDAO.setThursday(hourString);
                case FRIDAY -> timeDAO.setFriday(hourString);
            }
        }
        timeDAO.save();
        System.out.println("TimeDAO saved");
        return timeDAO;
    }

    public void loadTime() {
        if (available.isEmpty()) {
            for (Days day : Days.values()) {
                available.put(day, new ArrayList<>());
            }
        }
        System.out.println(timeId);
        TimeDAO timeDAO = TimeDAO.builder().id(timeId).build();
        timeDAO.load();
        if (timeDAO.getMonday() != null && this.available.get(Days.MONDAY).isEmpty()) {
            this.available.put(Days.MONDAY, List.of(timeDAO.getMonday().split(";")));
        }
        if (timeDAO.getTuesday() != null && this.available.get(Days.TUESDAY).isEmpty()) {
            this.available.put(Days.TUESDAY, List.of(timeDAO.getTuesday().split(";")));
        }
        if (timeDAO.getWednesday() != null && this.available.get(Days.WEDNESDAY).isEmpty()) {
            this.available.put(Days.WEDNESDAY, List.of(timeDAO.getWednesday().split(";")));
        }
        if (timeDAO.getThursday() != null && this.available.get(Days.THURSDAY).isEmpty()) {
            this.available.put(Days.THURSDAY, List.of(timeDAO.getThursday().split(";")));
        }
        if (timeDAO.getFriday() != null && this.available.get(Days.FRIDAY).isEmpty()) {
            this.available.put(Days.FRIDAY, List.of(timeDAO.getFriday().split(";")));
        }
    }

    public void save() {
        super.save(Rows.of("doctor_id", this.doctorId));
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public void load() {
        this.loaded = true;
        super.load(Rows.of("id", this.id));
        loadTime();
    }

    public void loadDoctorId() {
        this.loaded = true;
        super.load(Rows.of("doctor_id", this.doctorId));
        loadTime();
    }

    @Override
    public DBCollector<?> getDatabase() {
        return Manager.getManager(DatabaseManager.class).getMySQL();
    }

    @Override
    public HandlerDAO getHandle() {
        return this;
    }

    @Override
    public String getOtherId() {
        return doctorId;
    }
}
