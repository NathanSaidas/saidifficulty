package com.saidas.saiscaling.core.events;

import com.saidas.saiscaling.SaiDifficultyMod;
import com.saidas.saiscaling.config.LootTableConfig;
import com.saidas.saiscaling.core.entities.MonsterWrapper;
import com.saidas.saiscaling.core.events.scaling.MobScaling;
import com.saidas.saiscaling.core.events.scaling.MobTracker;
import com.saidas.saiscaling.core.utility.SaiLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.loot.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class MobEventHandler {
    private static LootTable table = null;

    public static void onMobSpawn(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if(!event.getWorld().isClientSide() && entity!= null && entity instanceof MonsterEntity) {
            SaiLogger.info("MobSpawn scale info to entity %s - %s.", entity.getType().getRegistryName().toString(), entity.getUUID().toString());

            MonsterEntity monster = (MonsterEntity) entity;
            MobTracker.track(monster);
            MobScaling.processSpawn(monster);
        }
    }

    public static void leaveWorld(final EntityLeaveWorldEvent event) {
        Entity entity = event.getEntity();
        if(!event.getWorld().isClientSide() && entity != null && entity instanceof MonsterEntity) {
            MonsterEntity monster = (MonsterEntity) entity;
            SaiLogger.info("MobLeave entity %s -- %s -- %f/%f.", entity.getType().getRegistryName().toString(), entity.getUUID().toString(), monster.getHealth(), monster.getMaxHealth());
            MobTracker.untrack(monster);
        }
    }

    public static void entityDied(final LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if(entity != null && !entity.isAlive() && entity instanceof MonsterEntity) {
            SaiLogger.info("Mob killed %s -- %s.", entity.getType().getRegistryName().toString(), entity.getUUID().toString());

            MonsterWrapper wrapper = MobTracker.getMonster(entity);
            MonsterEntity monster = (MonsterEntity) entity;
            DamageSource damage = event.getSource();
            if(wrapper != null && damage != null) {
                wrapper.forEachLoot((table) -> {
                    LootContext.Builder builder = new LootContext.Builder((ServerWorld) monster.level)
                            .withRandom(monster.getRandom())
                            .withParameter(LootParameters.THIS_ENTITY, monster)
                            .withParameter(LootParameters.ORIGIN, monster.position())
                            .withParameter(LootParameters.DAMAGE_SOURCE, damage)
                            .withOptionalParameter(LootParameters.KILLER_ENTITY, damage.getEntity())
                            .withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, damage.getDirectEntity());

                    LootContext context = builder.create(LootParameterSets.ENTITY);
                    table.getRandomItems(context).forEach(monster::spawnAtLocation);
                });
            }
        }
    }
}
