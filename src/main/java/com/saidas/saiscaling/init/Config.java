package com.saidas.saiscaling.init;

import com.saidas.saiscaling.config.LootTableConfig;
import com.saidas.saiscaling.config.ScalingConfig;

public class Config {
    public static void init() {
        ScalingConfig.load();
        LootTableConfig.load();
    }
}