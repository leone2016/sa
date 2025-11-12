package edu.miu.cs.cs425.ownerservice.model;

public class Owner {

    private String licencePlate;
    private String firstName;
    private String lastName;
    private String address;

    public Owner() {
    }

    public Owner(String licencePlate, String firstName, String lastName, String address) {
        this.licencePlate = licencePlate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

