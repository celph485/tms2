package tms2;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
class NicerGlobeRequest {
    private Map<String, DataElement> dataElements;
    private String vehicleNo;
    private String gpsProviderKey;
}

@Data
@Builder
class DataElement{
    private String latitude;
    private String longitude;
    private String speed;
    private String heading;
    private String datetime;
    private String ignStatus;
    private String location;
}
