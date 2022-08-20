package com.saidas.saiscaling.config;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.saidas.saiscaling.SaiDifficultyMod;
import com.saidas.saiscaling.core.utility.SaiConfigUtil;
import com.saidas.saiscaling.core.utility.SaiLogger;
import net.minecraft.loot.LootPredicateManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.HashMap;

/** Manage custom loot tables (loading them from json) **/
public class LootTableConfig {
    private static final String FILE_NAME = "loottables.json";
    private static final String COLLECTION_NAME = "loottables";
    private static LootTableConfig config = new LootTableConfig();

    /** The target loot tables to load **/
    public final ArrayList<String> lootFiles = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    private final HashMap<ResourceLocation, LootTable> lootTables = new HashMap<>();
    @Expose(serialize = false, deserialize = false)
    private final LootPredicateManager predicateManager = new LootPredicateManager();
    @Expose(serialize = false, deserialize = false)
    private final LootTableManager lootManager = new LootTableManager(this.predicateManager);

    /** Accessor to the 'singleton' like config. DO NOT STORE! Changes whe 'load' is called. **/
    public static LootTableConfig get() { return config; }

    /** Find a loot table for a given resource name **/
    public LootTable find(ResourceLocation name) {
        return lootTables.get(name);
    }

    public static boolean load() {
        boolean success = false;
        try {
            config = SaiConfigUtil.readFile(FILE_NAME, LootTableConfig.class);
            if(config == null) {
                config = new LootTableConfig();
            }
            for(String file : config.lootFiles) {
                ResourceLocation location = new ResourceLocation(SaiDifficultyMod.MOD_ID, file);
                JsonObject object = SaiConfigUtil.readCollectionFile(file, COLLECTION_NAME, JsonObject.class);
                LootTable tbl = ForgeHooks.loadLootTable(SaiConfigUtil.GSON, location, object, true, config.lootManager);
                if(tbl != null) {
                    config.lootTables.put(location, tbl);
                }
            }
            SaiLogger.info("Loaded config " + FILE_NAME);
            success = true;
        }
        catch(Exception e) {
            SaiLogger.exception(e);
        }
        return success;
    }

}
