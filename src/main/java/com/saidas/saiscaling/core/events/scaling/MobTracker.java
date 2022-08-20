package com.saidas.saiscaling.core.events.scaling;

import com.saidas.saiscaling.SaiDifficultyMod;
import com.saidas.saiscaling.core.entities.MonsterWrapper;
import com.saidas.saiscaling.core.utility.SaiLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MobTracker {
    private static final HashMap<UUID, MonsterWrapper> entities = new HashMap();

    public static void track(MonsterEntity entity) {
        if(entity != null) {
            entities.put(entity.getUUID(), new MonsterWrapper(entity));

            /*
            ResourceLocation lootTableType = entity.getLootTable();
            LootTable overrideTable = customTables.get(lootTableType.toString());
            if(overrideTable == null) {
                // OK! So we CANNOT override loot tables...
                //
                // Basically we can have a zombie in < 500 or a zombie in < 4000 zone.
                // Both zombies neeed separate loot tables, but all of them share loot tables..
                //
                // So now we need to add per-instance loot table.
                LootTable entityTable = entity.level.getServer().getLootTables().get(lootTableType);
                overrideTable = copy(entityTable);
                ResourceLocation overrideTableName = new ResourceLocation(SaiDifficultyMod.MOD_ID, "override_" + lootTableType.getNamespace() + "_" + lootTableType.getPath());

                new LootTable.Builder().build();
                // entity.level.getServer().getLootTables().
            }
            */
        }
    }

    public static void untrack(MonsterEntity entity) {
        if(entity != null) {
            entities.remove(entity.getUUID());
        }
    }

    public static MonsterWrapper getMonster(Entity entity) {
        MonsterWrapper result = null;
        if(entity != null) {
            result = entities.get(entity.getUUID());
        }
        return result;
    }
}
