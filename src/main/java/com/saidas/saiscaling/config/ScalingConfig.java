package com.saidas.saiscaling.config;

import com.saidas.saiscaling.core.info.MobScaleInfo;
import com.saidas.saiscaling.core.info.MobScaleInfoBuilder;
import com.saidas.saiscaling.core.utility.SaiConfigUtil;
import com.saidas.saiscaling.core.utility.SaiLogger;

import java.util.ArrayList;
import java.util.HashMap;

/** Json config class used to get/set values as well as save/load/revert to default. **/
public class ScalingConfig {
    /** The name of the config file **/
    private final static String FILE_NAME = "scaling.json";
    /** The instance of the config data. **/
    private static ScalingConfig config = new ScalingConfig();

    /** The default scaling values used to affect mob scaling. **/
    public final ArrayList<MobScaleInfo> defaultScaling = new ArrayList<>();
    /** The override scaling values based on the entity type. eg 'minecraft:creeper' => specific scaling **/
    public final HashMap<String, ArrayList<MobScaleInfo>> overrideScaling = new HashMap<>();
    /** The default scaling multiplier for a dimension. Use 'dimensionOverrideScaling' to create settings for each dimension. **/
    public final ArrayList<MobScaleInfo> defaultDimensionScaling = new ArrayList<>();
    /** The override scaling multiplier values based on the dimension. Applies to all entities based on distance. **/
    public final HashMap<String, ArrayList<MobScaleInfo>> overrideDimensionScaling = new HashMap<>();

    /** Get the instance of the config (do not hold onto it for it might change when a call to load is made). **/
    public static ScalingConfig get() {
        return config;
    }

    /** Load the scaling config from file. (NOTE: If the file doesn't exist then defaults are written).**/
    public static boolean load() {
        if(!exists()) {
            SaiLogger.info("No config present, creating deafult config. " + FILE_NAME);
            setDefault();
            save();
            return true;
        }

        boolean success = false;
        try {
            config = SaiConfigUtil.readFile(FILE_NAME, ScalingConfig.class);
            if(config == null) {
                setDefault();
            }
            SaiLogger.info("Loaded config " + FILE_NAME);
            success = true;
        }
        catch(Exception e) {
            SaiLogger.warn("Failed to load config. Falling back to defaults. (Check syntax!)");
            SaiLogger.exception(e);
            setDefault();
        }
        return success;
    }

    /** Save the scaling config to file. **/
    public static void save() {
        try {
            SaiConfigUtil.writeFile(FILE_NAME, config);
            SaiLogger.info("Saved config " + FILE_NAME);
        }
        catch (Exception e) {
            SaiLogger.exception(e);
        }
    }

    /** Sets the config back to the defaults. **/
    public static void setDefault() {
        config.defaultScaling.clear();
        config.overrideScaling.clear();
        config.defaultDimensionScaling.clear();
        config.overrideDimensionScaling.clear();

        // Until 1000 blocks there is no scaling applied.
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(1000)
                .build());

        // 1000-1500 blocks ( +10 hp, +4 attack )
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(1500)
                .bonusMaxHealth(10)
                .bonusDamage(4)
                .build());

        // 1500-2000 blocks ( +25hp, +7 attack )
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(2000)
                .bonusMaxHealth(25)
                .bonusDamage(7)
                .build());

        // 2000-2500 blocks ( +40hp, +7 attack )
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(2500)
                .bonusMaxHealth(40)
                .bonusDamage(9)
                .build());

        // 2500-3000
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(3000)
                .bonusMaxHealth(60)
                .bonusDamage(9)
                .bonusArmor(3)
                .bonusArmorToughness(1)
                .build());

        // 3000-3500
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(3500)
                .bonusMaxHealth(85)
                .bonusDamage(9)
                .bonusArmor(4)
                .bonusArmorToughness(1)
                .build());

        // 3500-4000
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(4000)
                .bonusMaxHealth(95)
                .bonusDamage(13)
                .bonusArmor(5)
                .bonusArmorToughness(2)
                .build());

        // 4000-4500
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(4500)
                .bonusMaxHealth(95)
                .bonusDamage(13)
                .bonusArmor(8)
                .bonusArmorToughness(2)
                .build());

        // 4500-5000
        config.defaultScaling.add( new MobScaleInfoBuilder()
                .maxDistance(5000)
                .bonusMaxHealth(105)
                .bonusDamage(16)
                .bonusArmor(12)
                .bonusArmorToughness(3)
                .build());

        config.defaultDimensionScaling.add(new MobScaleInfoBuilder()
                .maxDistance(1000)
                        .bonusMaxHealth(200)
                        .bonusDamage(200)
                        .build());
    }

    /** Check if the config file exists. **/
    public static boolean exists() {
        return SaiConfigUtil.exists(FILE_NAME);
    }
}
