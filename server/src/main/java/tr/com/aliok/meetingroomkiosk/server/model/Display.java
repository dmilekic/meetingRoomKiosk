package tr.com.aliok.meetingroomkiosk.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tr.com.aliok.meetingroomkiosk.model.api.DisplayModel;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class Display implements DisplayModel {

    private String displayKey;
    private Room room;
    private Sensor sensor;
    private String gcmRegistrationId;

    @Override
    public String getDisplayKey() {
        return displayKey;
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public Sensor getSensor() {
        return sensor;
    }

    @JsonIgnore
    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public static class Builder {
        private String displayKey;
        private Room room;
        private Sensor sensor;
        private String gcmRegistrationId;

        public Builder setDisplayKey(String displayKey) {
            this.displayKey = displayKey;
            return this;
        }

        public Builder setRoom(Room room) {
            this.room = room;
            return this;
        }

        public Builder setSensor(Sensor sensor) {
            this.sensor = sensor;
            return this;
        }

        public Builder setGcmRegistrationId(String gcmRegistrationId) {
            this.gcmRegistrationId = gcmRegistrationId;
            return this;
        }

        public Display create() {
            final Display display = new Display();
            display.displayKey = this.displayKey;
            display.room = this.room;
            display.sensor = this.sensor;
            display.gcmRegistrationId = this.gcmRegistrationId;
            return display;
        }
    }

}
