package edu.miu.cs.cs425.ownerservice.messaging;

public class SpeedingEvent {

    private String licencePlate;
    private double speedMph;

    public SpeedingEvent() {
    }

    public SpeedingEvent(String licencePlate, double speedMph) {
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

    @Override
    public String toString() {
        return "SpeedingEvent{" +
                "licencePlate='" + licencePlate + '\'' +
                ", speedMph=" + speedMph +
                '}';
    }
}

