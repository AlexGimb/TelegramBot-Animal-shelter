package model;

public class UserState {
    private String shelterType;
    private int stage;

    private String shelter;
    public static final int SELECT_SHELTER = 1;
    public static final int SELECT_ACTION = 2;

    public UserState() {
        this.stage = 0;
    }

    public String getShelter() {
        return shelter;
    }
    public int getStage() {
        return stage;
    }
    public void setStage(int stage) {
        this.stage = stage;
    }
    public void setShelter(String shelter) {
        this.shelter = shelter;
    }
    public String getShelterType() {
        return shelterType;
    }
    public void setShelterType(String shelterType) {
        this.shelterType = shelterType;
    }
}
