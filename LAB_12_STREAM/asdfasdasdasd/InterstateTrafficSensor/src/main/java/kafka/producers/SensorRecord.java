package kafka.producers;

public class SensorRecord {
	public String licencePlate;
	public int minute;
	public int second;
	public int cameraId;



	public SensorRecord(String licencePlate, int minute, int second, int cameraId) {
		super();
		this.licencePlate = licencePlate;
		this.minute = minute;
		this.second = second;
		this.cameraId = cameraId;
	}

	public SensorRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLicencePlate() {
		return licencePlate;
	}

	public void setLicencePlate(String licencePlate) {
		this.licencePlate = licencePlate;
	}


	public int getCameraId() {
		return cameraId;
	}

	public void setCameraId(int cameraId) {
		this.cameraId = cameraId;
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


	@Override
	public String toString() {
		return "SensorRecord{" +
				"licencePlate='" + licencePlate + '\'' +
				", minute=" + minute +
				", second=" + second +
				", cameraId=" + cameraId +
				'}';
	}
}
