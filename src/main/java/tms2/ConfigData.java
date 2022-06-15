package tms2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
class ConfigData {

    private final Configuration config;
    private final Map<Integer, String> deviceRegistrationMap;
    private final Map<Integer, String> deviceImeiMap;

    private ConfigData(final String filePath)  {
        try{
            this.config = initializeConfig(filePath);
            this.deviceRegistrationMap = initializeDeviceMap(this.config.getString(ConfigKey.DEVICE_REGISTRATION_MAPPING.name()));
            this.deviceImeiMap = initializeDeviceMap(this.config.getString(ConfigKey.DEVICE_IMEI_MAPPING.name()));
        }catch (Exception e){
            log.error("Error while loading config properties. ", e);
            throw new RuntimeException("Error while loading config properties. ", e);
        }
    }

    static ConfigData getInstance(final String filePath){
        return new ConfigData(filePath);
    }

    private Configuration initializeConfig(final String filePath) throws ConfigurationException {
        Parameters params = new Parameters();
        return new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.properties()
                    .setFileName(filePath)).getConfiguration();
    }

    private Map<Integer, String> initializeDeviceMap(final String data){
        log.debug("Creating device map from: {}",data);
        final String comma = ",";
        final String hyphen = "-";
        return  Arrays.stream(data.split(comma))
            .map(line -> ImmutablePair.of(StringUtils.substringBefore(line, hyphen), StringUtils.substringAfter(line, hyphen)))
            .collect(Collectors.toMap(pair ->  Integer.parseInt(pair.left), pair -> pair.right));
    }

    String getPositionUrl() {
        return this.config.getString(ConfigKey.TC_POSITION_URL.name());
    }

    String getTcUser() {
        return this.config.getString(ConfigKey.TC_USER.name());
    }

    String getTcPass() {
        return this.config.getString(ConfigKey.TC_PASS.name());
    }

    boolean isDeviceAllowed(Position position){
        final int deviceId = position.getDeviceId();
        return this.deviceImeiMap.containsKey(deviceId) || this.deviceRegistrationMap.containsKey(deviceId);
    }

    String getVehicleRegNoForPosition(Position position) {
        return this.deviceRegistrationMap.get(position.getDeviceId());
    }

    String getNicerGlobeSecretKey() {
        return this.config.getString(ConfigKey.NICER_GLOBE_SECRET_KEY.name());
    }

    String getNicerGlobeServerUrl() {
        return this.config.getString(ConfigKey.NICER_GLOBE_SERVER_URL.name());
    }

    boolean canSendThisPositionToNicerGlobeServer(final Position position){
        return this.deviceRegistrationMap.containsKey(position.getDeviceId());
    }

    int getNicerGlobeRequestIntervalInSeconds(){
        return this.config.getInt(ConfigKey.NICER_GLOBE_REQUEST_INTERVAL_IN_SECONDS.name(),0);
    }

    String getImeiForPosition(final Position position){
        return this.deviceImeiMap.get(position.getDeviceId());
    }

    String getAditiTrackingServerUrl() {
        return this.config.getString(ConfigKey.ADITI_TRACKING_SERVER_URL.name());
    }

    int getAditiTrackingRequestIntervalInSeconds(){
        return this.config.getInt(ConfigKey.ADITI_TRACKING_REQUEST_INTERVAL_IN_SECONDS.name(),0);
    }

    boolean canSendThisPositionToAditiTrackingServer(final Position position){
        return this.deviceImeiMap.containsKey(position.getDeviceId());
    }
}
