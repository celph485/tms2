package tms2;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
class AditiTrackingService {
    private final ConfigData configData;

    private AditiTrackingService(final ConfigData configData) {
        this.configData = configData;
    }

    static AditiTrackingService getInstance(final ConfigData configData){
        return new AditiTrackingService(configData);
    }

    void sendRequests(final List<Position> positions){
        positions.stream()
            .filter(configData::canSendThisPositionToAditiTrackingServer)
            .forEach(this::sendRequest);
        log.info("sent all allowed positions to Aditi Tracking server");
    }

    private void sendRequest(final Position position){
        try{
            log.debug("Sending position: {}",position);
            var reqObj = AditiTrackingRequestFactory.createRequest(
                position,
                configData.getImeiForPosition(position)
            );

            var jsonPayload = AditiTrackingRequestFactory.createPayload(reqObj);
            log.info("JSON Payload: {}",jsonPayload);
            var req = HttpRequest.newBuilder()
                .uri(URI.create(configData.getAditiTrackingServerUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

            var response = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            log.info("Response: {}",response.body());
            log.info("Sleeping for {} seconds", configData.getAditiTrackingRequestIntervalInSeconds());
            TimeUnit.SECONDS.sleep(configData.getAditiTrackingRequestIntervalInSeconds());
        }catch (Exception e){
            log.error("Error while sending request to Aditi tracking server",e);
        }
    }

}
