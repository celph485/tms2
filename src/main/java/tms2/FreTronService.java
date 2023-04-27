package tms2;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
class FreTronService {

    private final ConfigData configData;

    private FreTronService(ConfigData configData) {
        this.configData = configData;
    }

    static FreTronService getInstance(final ConfigData configData){
        log.info("Creating FreTronService object");
        return new FreTronService(configData);
    }

    void sendRequests(final List<Position> positions){
        var requests = positions.stream()
            .filter(configData::canSendThisPositionToFreTronTrackingServer)
            .map(this::createFreTronReqFromPosition)
                .toList();
        log.info("going to sent {} positions to FreTron.", CollectionUtils.size(requests));

        var payload = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create().toJson(requests);
        log.info(payload);
        sendPayload(payload);
    }

    private void sendPayload(final String jsonPayload){
        var url = configData.getFreTronTrackingServerUrl();
        var req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();
        try {
            var response = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            log.info("Response: {}",response.body());
            log.info("sent all allowed positions to FreTron Tracking server");
        } catch (Exception e) {
            log.error("Error while sending request to FreTron tracking server",e);
        }

    }

    private FreTronRequest createFreTronReqFromPosition(final Position position){
        var vendor = configData.getFreTronTrackingServerVendor();
        var imei = configData.getImeiForPosition(position);
        return FreTronRequestFactory.createRequest(position,imei, vendor);
    }
}
