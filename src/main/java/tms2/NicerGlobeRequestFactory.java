package tms2;

import com.google.gson.GsonBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

class NicerGlobeRequestFactory {
    static NicerGlobeRequest createRequest(
        final Position position,
        final String vehicleRegistration,
        final String gpsProviderKey
    ){

        final String latitude = String.valueOf(position.getLatitude());
        final String longitude = String.valueOf(position.getLongitude());
        final String speed = String.valueOf(position.getSpeed());
        final String heading = String.valueOf(position.getCourse());
        final String datetime = convertToGmtDateTimeString(position.getDeviceTime());
        final String ignStatus = position.getAttributes().isIgnition() ? "1" : "0";
        final String location = "";

        var dataElement = DataElement.builder()
            .latitude(latitude)
            .longitude(longitude)
            .speed(speed)
            .heading(heading)
            .datetime(datetime)
            .ignStatus(ignStatus)
            .location(location)
            .build();

        var map = new HashMap<String, DataElement>();
        map.put("DATAELEMENTS", dataElement);

        return NicerGlobeRequest.builder()
            .dataElements(map)
            .gpsProviderKey(gpsProviderKey)
            .vehicleNo(vehicleRegistration)
            .build();
    }

    private static String convertToGmtDateTimeString(final String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);
        ZonedDateTime gmtDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("GMT"));
        return formatter.format(gmtDateTime);
    }

    static String createPayload(final NicerGlobeRequest request){
        return new GsonBuilder()
            .setFieldNamingStrategy(field -> field.getName().toUpperCase())
            //.setPrettyPrinting()
            .create().toJson(request);
    }
}
