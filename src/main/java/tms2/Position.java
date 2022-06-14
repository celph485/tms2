package tms2;

import lombok.Data;

@Data
class Position {
    private int id;
    private Attribute attributes;
    private int deviceId;
    private String protocol;
    private String serverTime;
    private String deviceTime;
    private String fixTime;
    private boolean outdated;
    private boolean valid;
    private float latitude;
    private float longitude;
    private int altitude;
    private float speed;
    private float course;
    private String address;
    private int accuracy;
}

@Data
class Attribute{
    private int status;
    private boolean ignition;
    private boolean charge;
    private boolean blocked;
    private int batteryLevel;
    private int rssi;
    private float distance;
    private float totalDistance;
    private boolean motion;
    private long hours;
}
