package edu.miu.cs.cs425.feecalculatorservice.messaging;

public class FeeRecord {

    private String licencePlate;
    private String firstName;
    private String lastName;
    private String address;
    private double speedMph;

    public FeeRecord() {
    }

    public FeeRecord(String licencePlate, String firstName, String lastName, String address, double speedMph) {
        this.licencePlate = licencePlate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.speedMph = speedMph;
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

    public double getSpeedMph() {
        return speedMph;
    }

    public void setSpeedMph(double speedMph) {
        this.speedMph = speedMph;
    }

    @Override
    public String toString() {
        return "FeeRecord{" +
                "licencePlate='" + licencePlate + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", speedMph=" + speedMph +
                '}';
    }
}

