package tms2;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
class NicerGlobeService {
    private final ConfigData configData;
    private NicerGlobeService(final ConfigData configData) {
        this.configData = configData;
    }

    static NicerGlobeService getInstance(final ConfigData configData){
        return new NicerGlobeService(configData);
    }

    void sendRequests(final List<Position> positions){
        positions.stream()
            .filter(configData::canSendThisPositionToNicerGlobeServer)
            .forEach(this::sendRequest);
        log.info("sent all allowed positions to NicerGlobe server");
    }

    private void sendRequest(final Position position){
        try{
            log.debug("Sending position: {}",position);
            var reqObj = NicerGlobeRequestFactory.createRequest(
                position,
                configData.getVehicleRegNoForPosition(position),
                configData.getNicerGlobeSecretKey()
            );

            var jsonPayload = NicerGlobeRequestFactory.createPayload(reqObj);
            log.info("JSON Payload: {}",jsonPayload);
            var req = HttpRequest.newBuilder()
                .uri(URI.create(configData.getNicerGlobeServerUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

            var response = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            log.info("Response: {}",response.body());
            log.info("Sleeping for {} seconds", configData.getNicerGlobeRequestIntervalInSeconds());
            TimeUnit.SECONDS.sleep(configData.getNicerGlobeRequestIntervalInSeconds());
        }catch (Exception e){
            log.error("Error while sending request to nicer globe server",e);
        }
    }


}
