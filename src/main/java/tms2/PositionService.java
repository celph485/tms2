package tms2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class PositionService {

    private final HttpRequest request;
    private final HttpClient httpClient;

    private PositionService(final ConfigData configData) {
        this.request = HttpRequest.newBuilder()
            .uri(URI.create(configData.getPositionUrl()))
            .GET()
            .build();

        this.httpClient = HttpClient.newBuilder()
            .authenticator(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(configData.getTcUser(), configData.getTcPass().toCharArray());
                }
            })
            .build();
    }

    static PositionService getInstance(final ConfigData configData){
        return new PositionService(configData);
    }

    List<Position> getPositions() throws Exception {
        log.info("Getting positions from Traccar");
        var response = this.httpClient
            .send(this.request, HttpResponse.BodyHandlers.ofString());

        List<Position> positions = new Gson()
            .fromJson(response.body(), new TypeToken<ArrayList<Position>>(){}.getType());
        log.info("Total {} positions received from Traccar", CollectionUtils.size(positions));
        return positions;
    }
}
