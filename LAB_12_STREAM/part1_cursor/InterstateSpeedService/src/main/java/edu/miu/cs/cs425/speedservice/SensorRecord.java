package edu.miu.cs.cs425.speedservice;

import java.time.LocalDateTime;
import java.util.Objects;

public class SensorRecord {

    private String licencePlate;
    private int minute;
    private int second;
    private int cameraId;
    private LocalDateTime receivedAt;

    public SensorRecord() {
    }

    public SensorRecord(String licencePlate, int minute, int second, int cameraId) {
        this.licencePlate = licencePlate;
        this.minute = minute;
        this.second = second;
        this.cameraId = cameraId;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    @Override
    public String toString() {
        return "SensorRecord{" +
                "licencePlate='" + licencePlate + '\'' +
                ", minute=" + minute +
                ", second=" + second +
                ", cameraId=" + cameraId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorRecord that)) return false;
        return minute == that.minute &&
                second == that.second &&
                cameraId == that.cameraId &&
                Objects.equals(licencePlate, that.licencePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licencePlate, minute, second, cameraId);
    }
}

