package model;

public class UserState {
    private String shelterType;
    private int stage;

    private String shelter;
    public static final int SELECT_SHELTER = 1;
    public static final int SELECT_ACTION_CAT = 2;
    public static final int SELECT_ACTION_DOG = 3;
    public static final int SHELTER_INFO = 4;
    public static final int HOW_TO_ADOPT = 5;
    public static final int PET_REPORT = 6;
    public static final int CALL_VOLUNTEER = 7;

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
