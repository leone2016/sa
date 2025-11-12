package edu.miu.cs.cs425.interstatespeedservice;


public class SpeedRecordDTO {
	public String licencePlate;
//	public int minute;
//	public int second;
	public double speedMph;

	public SpeedRecordDTO(String licencePlate, double speedMph) {
		super();
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
