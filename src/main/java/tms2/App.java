package tms2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class App {

    public static void main(String[] args) {

        final String configFilePath = getConfigFilePath(args);
        final var configData = ConfigData.getInstance(configFilePath);

        final var positionService = PositionService.getInstance(configData);
        final var nicerGlobeService = NicerGlobeService.getInstance(configData);
        final var aditiTrackingService = AditiTrackingService.getInstance(configData);


        ScheduledExecutorService executorService = Executors
            .newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(()->{
            try{
                final var positions = positionService.getPositions();
                nicerGlobeService.sendRequests(positions);
                aditiTrackingService.sendRequests(positions);
            }catch (Exception e){
                log.error("Error while processing ", e);
            }
        }, 1, 10, TimeUnit.SECONDS);



        log.info("Done");
    }

    private static String getConfigFilePath(final String[] args){
        if(ArrayUtils.isNotEmpty(args) && Objects.nonNull(args[0])){
            return args[0];
        }
        log.error("Config file path is required as argument");
        throw new IllegalArgumentException("Config file path is required as argument");
    }
}
