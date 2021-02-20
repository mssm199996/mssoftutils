package mssoftutils.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CompteResponseDto {

    private int id;

    private String username;
    private String password;

    private String dateDerniereConnexion;
    private String heureDerniereConnexion;

    private boolean admin;
    private Map<Integer, String> droits = new HashMap<>();

    public void formatDateDerniereConnexion(LocalDate localDate) {
        this.dateDerniereConnexion = localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public void formatHeureDerniereConnexion(LocalTime localTime) {
        this.heureDerniereConnexion = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateDerniereConnexion() {
        return dateDerniereConnexion;
    }

    public void setDateDerniereConnexion(String dateDerniereConnexion) {
        this.dateDerniereConnexion = dateDerniereConnexion;
    }

    public String getHeureDerniereConnexion() {
        return heureDerniereConnexion;
    }

    public void setHeureDerniereConnexion(String heureDerniereConnexion) {
        this.heureDerniereConnexion = heureDerniereConnexion;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Map<Integer, String> getDroits() {
        return droits;
    }

    public void setDroits(Map<Integer, String> droits) {
        this.droits = droits;
    }
}
