package com.saidas.saiscaling.core.events;

import com.saidas.saiscaling.core.entities.MonsterWrapper;
import com.saidas.saiscaling.core.events.scaling.MobScaling;
import com.saidas.saiscaling.core.events.scaling.MobTracker;
import com.saidas.saiscaling.core.utility.SaiLogger;
import com.saidas.saiscaling.init.Command;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onMobSpawn(EntityJoinWorldEvent event) {
        MobEventHandler.onMobSpawn(event);
    }

    @SubscribeEvent
    public static void leaveWorld(final EntityLeaveWorldEvent event) {
        MobEventHandler.leaveWorld(event);
    }

    @SubscribeEvent
    public static void entityDied(final LivingDeathEvent event) {
        MobEventHandler.entityDied(event);
    }

    @SubscribeEvent
    public static void onRegisterCommand(final RegisterCommandsEvent event) {
        CommandsEventHandler.onRegisterCommand(event);
    }
}
