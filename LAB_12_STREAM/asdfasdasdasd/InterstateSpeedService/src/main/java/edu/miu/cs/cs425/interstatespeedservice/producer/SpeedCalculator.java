package edu.miu.cs.cs425.interstatespeedservice.producer;

import edu.miu.cs.cs425.interstatespeedservice.domain.SensorRecord;
import edu.miu.cs.cs425.interstatespeedservice.SpeedRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SpeedCalculator {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpeedCalculator.class);

	@Autowired
	Sender sender;
	private static final double DISTANCE_MILES = 0.5;
	private static final double SECONDS_TO_HOURS = 3600.0;
	private static final double SPEED_THRESHOLD_MPH = 72.0;

	private Map<String, SensorRecord> recordstream = new HashMap<String, SensorRecord>();

	public void handleRecord(SensorRecord sensorRecord) {
//		int speed=0;
		int time=1000;
		if (sensorRecord.getCameraId()== 1) {
			LOGGER.debug("Captured first camera reading for {}", sensorRecord.getLicencePlate());

			recordstream.put(sensorRecord.getLicencePlate(), sensorRecord);
		}
		if (sensorRecord.getCameraId() != 2) {
			LOGGER.warn(" 🔴 Unexpected camera id {} for plate {}", sensorRecord.getCameraId(), sensorRecord.getLicencePlate());
			return;
		}
			LOGGER.debug("Captured Second camera reading for {}", sensorRecord.getLicencePlate());

			SensorRecord firstRecord = recordstream.get(sensorRecord.getLicencePlate());
			if (firstRecord != null) {
				LOGGER.warn(" 🔴 No matching record for camera 1 for {}", sensorRecord.getLicencePlate());
				return;
			}
				if (firstRecord.getMinute() == sensorRecord.getMinute()) {
					time = sensorRecord.getSecond()-firstRecord.getSecond();
				}
				else if ((sensorRecord.getMinute() - firstRecord.getMinute()) == 1) {
					time = (sensorRecord.getSecond()+60) -firstRecord.getSecond();
				}
		double speedMph = (DISTANCE_MILES / time) * SECONDS_TO_HOURS;

		LOGGER.info(" 👨🏻‍✈️Vehicle {} speed {} mph", sensorRecord.getLicencePlate(), speedMph);
		recordstream.remove(sensorRecord.getLicencePlate());
				if (speedMph > SPEED_THRESHOLD_MPH) {
					LOGGER.info(" 💨️Vehicle {} speeding {} mph", sensorRecord.getLicencePlate(), speedMph);
					sender.send(new SpeedRecordDTO(sensorRecord.getLicencePlate(), speedMph));
				}
	}



}