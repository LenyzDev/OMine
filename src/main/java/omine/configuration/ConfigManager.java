package omine.configuration;

import omine.OMine;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.logging.Logger;

public class ConfigManager {

    private ConfigUtils configUtils;
    private HashMap<String, FileConfiguration> configurationMap = new HashMap<>();

    public ConfigManager(OMine main, Logger logger){
        configUtils = new ConfigUtils(main, logger);

        logger.info("Loading configurations...");
        configUtils.createConfig("config");
        configurationMap.put("config", configUtils.getConfig("config"));
        logger.info("Configurations loaded.");
    }

    public FileConfiguration getConfig(String config){
        return configurationMap.get(config);
    }
}