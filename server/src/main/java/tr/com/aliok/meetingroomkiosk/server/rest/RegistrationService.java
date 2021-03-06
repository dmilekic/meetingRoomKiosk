package tr.com.aliok.meetingroomkiosk.server.rest;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.aliok.meetingroomkiosk.server.manager.DisplayManager;
import tr.com.aliok.meetingroomkiosk.server.manager.RoomManager;
import tr.com.aliok.meetingroomkiosk.server.manager.SensorManager;
import tr.com.aliok.meetingroomkiosk.server.model.Display;
import tr.com.aliok.meetingroomkiosk.server.model.Room;
import tr.com.aliok.meetingroomkiosk.server.model.Sensor;
import tr.com.aliok.meetingroomkiosk.server.model.dto.SensorSettings;

/**
 * Provides non-application-logic related functions for client devices.
 *
 * @author Ali Ok (ali.ok@apache.org)
 */
@RestController
public class RegistrationService {

    @Autowired
    DisplayManager displayManager;

    @Autowired
    SensorManager sensorManager;

    @Autowired
    RoomManager roomManager;

    @RequestMapping("/registerDisplay")
    public String registerDisplay(@RequestParam(value = "displayKey", required = true) String displayKey,
                                  @RequestParam(value = "password", required = true) String password,
                                  @RequestParam(value = "gcmRegistrationId", required = true) String gcmRegistrationId,
                                  @RequestParam(value = "roomKey", required = true) String roomKey) {

        // check if roomId is valid
        final Room room = roomManager.findByKey(roomKey);
        Validate.notNull(room);

        final Display display = displayManager.findByDisplayKeyAndPassword(displayKey, password);
        Validate.notNull(display);

        // return the token
        return displayManager.registerDisplay(display, gcmRegistrationId, room);
    }

    @RequestMapping("/isDisplayTokenValid")
    public boolean isDisplayTokenValid(@RequestParam(value = "token", required = true) String token) {
        final Display display = displayManager.findByToken(token);
        return display != null;
    }

    @RequestMapping("/getDisplayInformation")
    public Display getDisplayInformation(@RequestParam(value = "token", required = true) String token) {
        final Display display = displayManager.findByToken(token);
        Validate.notNull(display);

        return display;
    }

    @RequestMapping("/registerSensor")
    public SensorSettings registerSensor(@RequestParam(value = "sensorKey", required = true) String sensorKey,
                                         @RequestParam(value = "password", required = true) String password,
                                         @RequestParam(value = "roomKey", required = true) String roomKey) {
        // check if roomId is valid
        final Room room = roomManager.findByKey(roomKey);
        Validate.notNull(room);

        final Sensor sensor = sensorManager.findBySensorKeyAndPassword(sensorKey, password);
        Validate.notNull(sensor);

        // return the token
        final String token = sensorManager.registerSensor(sensor, room);
        return new SensorSettings(token, sensor);
    }

    @RequestMapping("/isSensorTokenValid")
    public boolean isSensorTokenValid(@RequestParam(value = "token", required = true) String token) {
        final Sensor sensor = sensorManager.findByToken(token);
        return sensor != null;
    }

    @RequestMapping("/getSensorInformation")
    public Sensor getSensorInformation(@RequestParam(value = "token", required = true) String token) {
        final Sensor sensor = sensorManager.findByToken(token);
        Validate.notNull(sensor);

        return sensor;
    }

    // TODO: implement with go-live
    // use heartbeats from the sensor
//    public boolean isSensorForDisplayOnline(){
//
//    }
}
