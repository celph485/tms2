package tms2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) throws Exception {
        log.info("Hello World");
        var filePath = "C:\\Users\\f3931\\src\\ganesh\\tms2\\src\\main\\resources\\tms2.properties";
        var configData = ConfigData.getInstance(filePath);
        var positionService = PositionService.getInstance(configData);
        var positions = positionService.getPositions();
        var nicerGlobeService = NicerGlobeService.getInstance(configData);
        nicerGlobeService.sendRequests(positions);
        log.info("Done");
    }
}
