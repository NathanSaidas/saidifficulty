package com.saidas.saiscaling.core.events;

import com.saidas.saiscaling.SaiDifficultyMod;
import com.saidas.saiscaling.config.LootTableConfig;
import com.saidas.saiscaling.core.entities.MonsterWrapper;
import com.saidas.saiscaling.core.events.scaling.MobScaling;
import com.saidas.saiscaling.core.events.scaling.MobTracker;
import com.saidas.saiscaling.core.utility.SaiLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.loot.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

/**
 * Process mob specific events
 */
public class MobEventHandler {
    private static LootTable table = null;
    private static boolean processArrowAttacks = true;

    /**
     * Handle the mob spawn event and begin tracking and scaling the mob.
     * @param event
     */
    public static void onMobSpawn(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if(!event.getWorld().isClientSide() && entity!= null && entity instanceof MonsterEntity) {
            SaiLogger.info("MobSpawn scale info to entity %s - %s.", entity.getType().getRegistryName().toString(), entity.getUUID().toString());

            MonsterEntity monster = (MonsterEntity) entity;
            MobTracker.track(monster);
            MobScaling.processSpawn(monster);
        }
    }

    /**
     * Handle the mob leave world event and begin tracking and scaling the mob.
     * @param event
     */
    public static void leaveWorld(final EntityLeaveWorldEvent event) {
        Entity entity = event.getEntity();
        if(!event.getWorld().isClientSide() && entity != null && entity instanceof MonsterEntity) {
            MonsterEntity monster = (MonsterEntity) entity;
            SaiLogger.info("MobLeave entity %s -- %s -- %f/%f.", entity.getType().getRegistryName().toString(), entity.getUUID().toString(), monster.getHealth(), monster.getMaxHealth());
            MobTracker.untrack(monster);
        }
    }

    /**
     * When a monster dies check them for extra loot tables and process them.
     * @param event
     */
    public static void entityDied(final LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if(entity != null
                && !entity.isAlive()
                && entity instanceof MonsterEntity
                && entity.level != null && !entity.level.isClientSide())
        {
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

    /**
     * Handle damage scaling for projectile, explosion, magic damage. Melee damage is already handled.
     * @param event
     */
    public static void entityHurt(final LivingHurtEvent event) {
        if(event.getEntity() instanceof PlayerEntity && event.getSource().getEntity() instanceof MonsterEntity) {
            if(event.getSource().isProjectile() || event.getSource().isExplosion() || event.getSource().isMagic()) {
                ModifiableAttributeInstance attribute = ((MonsterEntity)event.getSource().getEntity()).getAttribute(Attributes.ATTACK_DAMAGE);
                double originalBaseValue = attribute.getBaseValue();
                attribute.setBaseValue(event.getAmount());
                float modifiedValue = (float)attribute.getValue();
                SaiLogger.info("Handle attack event %s. %f -> %f", event.getSource().msgId, event.getAmount(), modifiedValue);
                event.setAmount(modifiedValue);
                attribute.setBaseValue(originalBaseValue);
            }
        }
    }
}
