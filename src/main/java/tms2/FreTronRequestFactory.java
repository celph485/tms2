package tms2;

import java.time.ZonedDateTime;

class FreTronRequestFactory {

    static FreTronRequest createRequest(final Position position, final String imeiNo, final String vendor){
        return FreTronRequest.builder()
            .imei(imeiNo)
            .vendor(vendor)
            .latitude(position.getLatitude())
            .longitude(position.getLongitude())
            .time(convertToIstDateTimeEpoch(position.getDeviceTime()))
            .build();
    }

    private static long convertToIstDateTimeEpoch(final String dateString){
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
