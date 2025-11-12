package edu.miu.cs.cs425.speedservice;

public class SpeedRecord {

    private String licencePlate;
    private double speedMph;

    public SpeedRecord() {
    }

    public SpeedRecord(String licencePlate, double speedMph) {
        this.licencePlate = licencePlate;
        this.speedMph = speedMph;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public double getSpeedMph() {
        return speedMph;
    }

    public void setSpeedMph(double speedMph) {
        this.speedMph = speedMph;
    }
}

