package tms2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class AditiTrackingRequest {
    private String imeiNo;
    private String time;
    private String lattitude;
    private String longitude;
    @Builder.Default
    private String lattitudeDirection = "N";
    @Builder.Default
    private String longitudeDirection = "E";
    private String speed;
    @Builder.Default
    private String digitalPort1 = "0";
    @Builder.Default
    private String digitalPort2 = "0";
    @Builder.Default
    private String digitalPort3 = "0";
    @Builder.Default
    private String digitalPort4 = "0";
    @Builder.Default
    private String analogPort1 = "0";
    @Builder.Default
    private String analogPort2 = "0";
    @Builder.Default
    private String angle = "0";
    @Builder.Default
    private String satellite = "0";
    @Builder.Default
    private String batteryVoltage = "0";
    @Builder.Default
    private String gpsValidity = "A";
}
