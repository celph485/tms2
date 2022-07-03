package tms2;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class AditiTrackingRequestFactory {

    static AditiTrackingRequest createRequest(final Position position, final String imeiNo){
        final String latitude = String.format("%.4f", position.getLatitude());
        final String longitude = String.format("%.4f", position.getLongitude());
        return AditiTrackingRequest.builder()
            .imeiNo(imeiNo)
            .time(convertToIstDateTimeString(position.getDeviceTime()))
            .lattitude(latitude)
            .longitude(longitude)
            .speed(String.valueOf(position.getSpeed()))
            .build();
    }

    private static String convertToIstDateTimeString(final String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);
        ZonedDateTime gmtDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return formatter.format(gmtDateTime);
    }

    static String createPayload(final AditiTrackingRequest request){
        return new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            //.setPrettyPrinting()
            .create().toJson(request);
    }
}
