package tms2;

import java.util.HashMap;
import java.util.Map;

enum ConfigKey {
    TC_USER,
    TC_PASS,
    TC_POSITION_URL,
    NICER_GLOBE_SERVER_URL,
    NICER_GLOBE_SECRET_KEY,
    NICER_GLOBE_REQUEST_INTERVAL_IN_MILLS,
    ADITI_TRACKING_REQUEST_INTERVAL_IN_MILLS,
    DEVICE_REGISTRATION_MAPPING,
    ADITI_TRACKING_SERVER_URL,
    DEVICE_IMEI_MAPPING;

    private static final Map<String, ConfigKey> BY_LABEL = new HashMap<>();

    static {
        for (ConfigKey e: values()) {
            BY_LABEL.put(e.name(), e);
        }
    }

    public static ConfigKey getConfigDataKeyForString(final String label) {
        return BY_LABEL.get(label);
    }
}
