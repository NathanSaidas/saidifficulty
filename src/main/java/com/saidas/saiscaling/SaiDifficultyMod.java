package com.saidas.saiscaling;

import com.saidas.saiscaling.init.Config;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SaiDifficultyMod.MOD_ID)
public class SaiDifficultyMod {
    public static final String MOD_ID = "saidifficulty";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SaiDifficultyMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        Config.init();
    }


}


