package model;

import java.util.Objects;

public class Shelter {
    private String name;
    private String address;
    private String phone;
    private String description;

    public Shelter(String name, String address, String phone, String description) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return Objects.equals(name, shelter.name) && Objects.equals(address, shelter.address) && Objects.equals(phone, shelter.phone) && Objects.equals(description, shelter.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, phone, description);
    }
}
