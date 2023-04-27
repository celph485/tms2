package tms2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class FreTronRequest {
    private String imei;
    private float latitude;
    private float longitude;
    private String vendor;
    private long time;
}
