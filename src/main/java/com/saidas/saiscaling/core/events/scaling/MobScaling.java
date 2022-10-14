package com.saidas.saiscaling.core.events.scaling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saidas.saiscaling.SaiDifficultyMod;
import com.saidas.saiscaling.config.LootTableConfig;
import com.saidas.saiscaling.config.ScalingConfig;
import com.saidas.saiscaling.core.entities.MonsterWrapper;
import com.saidas.saiscaling.core.info.MobScaleInfo;
import com.saidas.saiscaling.core.info.MobScaleInfoBuilder;
import com.saidas.saiscaling.core.utility.SaiLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.injection.At;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MobScaling {
    /** Modifiers **/
    private static final UUID scalingBonusHealthUUID = UUID.fromString("2fd506dc-3e55-4c28-89d8-cfe73d482c4f");
    private static final UUID scalingBonusArmorUUID = UUID.fromString("8be4c967-4edd-4a31-96ca-8edd3543c939");
    private static final UUID scalingBonusArmorToughnessUUID = UUID.fromString("cfbd3d72-31ab-470e-83e1-ffe31190fdb3");
    private static final UUID scalingBonusDamageUUID = UUID.fromString("a22356d0-f336-48a5-9956-14b053e245f0");
    private static final UUID dimensionScalingBonusHealthUUID = UUID.fromString("774f8540-9e44-4ab9-8a2e-b09c441ff92d");
    private static final UUID dimensionScalingBonusArmorUUID = UUID.fromString("20c3da71-cef7-45cb-aaa1-d186ed741052");
    private static final UUID dimensionScalingBonusArmorToughnessUUID = UUID.fromString("06d95061-41b5-459b-96e8-a105a759c5b8");
    private static final UUID dimensionScalingBonusDamageUUID = UUID.fromString("310720fa-fbfe-4e8b-b70f-780f23b63307");

    // FEATURE IDEA: We can also apply scaling based on dimension. Let's just keep it simple for now.
    // FEATURE IDEA: Since we're increasing the difficulty of a mob by adjusting health/armor/damage. We should
    //               also provide rewards. (Increased XP from kill, Better Drop Table)


    public static void processSpawn(MonsterEntity entity) {
        if (entity == null) {
            return;
        }

        if (entity.getType() == null) {
            return;
        }

        if(entity.level == null) {
            return;
        }

        ResourceLocation typeName = entity.getType().getRegistryName();
        ResourceLocation dimensionName = entity.level.dimension().location();
        BlockPos position = entity.blockPosition();
        if (typeName == null || position == null || dimensionName == null) {
            return;
        }

        ScalingConfig config = ScalingConfig.get();

        // attempt to get override scaling if provided...
        ArrayList<MobScaleInfo> scaling = config.overrideScaling.get(typeName.toString());
        if (scaling == null) {
            scaling = config.defaultScaling;
        }

        ArrayList<MobScaleInfo> dimensionScaling = config.overrideDimensionScaling.get(dimensionName.toString());
        if(dimensionScaling == null) {
            dimensionScaling = config.defaultDimensionScaling;
        }


        // find the appropriate scale info
        MobScaleInfo mobScaleInfo = findScaleInfo(scaling, MathHelper.floor(distance2D(position)));
        if (mobScaleInfo == null) {
            return;
        }

        MobScaleInfo dimensionScaleInfo = findScaleInfo(dimensionScaling, MathHelper.floor(distance2D(position)));
        if(dimensionScaleInfo == null) {
            return;
        }


        try {
            applyScaleInfo(entity, mobScaleInfo, dimensionScaleInfo);

        } catch (Exception e) {
            SaiLogger.exception(e);
        }
    }

    /** Apply the scaling effects to the mob.
     * @param entity The mob being modified.
     * @param info The scaling information used for modification.
     */
    private static void applyScaleInfo(MonsterEntity entity, MobScaleInfo info, MobScaleInfo dimensionInfo) {
        ModifiableAttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
        ModifiableAttributeInstance armor = entity.getAttribute(Attributes.ARMOR);
        ModifiableAttributeInstance armorToughness = entity.getAttribute(Attributes.ARMOR_TOUGHNESS);
        ModifiableAttributeInstance attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);

        float healthPercent = entity.getHealth() / entity.getMaxHealth();
        MathHelper.clamp(healthPercent, 0.0f, 1.0f);

        if(maxHealth != null) {
            if(info.bonusMaxHealth > 0) {
                maxHealth.removePermanentModifier(scalingBonusHealthUUID);
                maxHealth.addPermanentModifier(new AttributeModifier(scalingBonusHealthUUID, "Scaling Bonus Health", info.bonusMaxHealth, AttributeModifier.Operation.ADDITION));
            }
            if(dimensionInfo.bonusMaxHealth > 0) {
                float scaleValue = (float)dimensionInfo.bonusMaxHealth / 100.0f;
                maxHealth.removePermanentModifier(dimensionScalingBonusHealthUUID);
                maxHealth.addPermanentModifier(new AttributeModifier(dimensionScalingBonusHealthUUID, "Dimension Scaling Bonus Health", scaleValue, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }

        if(armor != null) {
            if(info.bonusArmor > 0) {
                armor.removePermanentModifier(scalingBonusArmorUUID);
                armor.addPermanentModifier(new AttributeModifier(scalingBonusArmorUUID, "Scaling Bonus Armor", info.bonusArmor, AttributeModifier.Operation.ADDITION));
            }
            if(dimensionInfo.bonusArmor > 0) {
                float scaleValue = (float)dimensionInfo.bonusArmor / 100.0f;
                armor.removePermanentModifier(dimensionScalingBonusArmorUUID);
                armor.addPermanentModifier(new AttributeModifier(dimensionScalingBonusArmorUUID, "Dimension Scaling Bonus Armor", scaleValue, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }

        if(armorToughness != null) {
            if(info.bonusArmorToughness > 0) {
                armorToughness.removePermanentModifier(scalingBonusArmorToughnessUUID);
                armorToughness.addPermanentModifier(new AttributeModifier(scalingBonusArmorToughnessUUID, "Scaling Bonus Armor Toughness", info.bonusArmorToughness, AttributeModifier.Operation.ADDITION));
            }
            if(dimensionInfo.bonusArmorToughness > 0) {
                float scaleValue = (float)dimensionInfo.bonusArmorToughness / 100.0f;
                armorToughness.removePermanentModifier(dimensionScalingBonusArmorToughnessUUID);
                armorToughness.addPermanentModifier(new AttributeModifier(dimensionScalingBonusArmorToughnessUUID, "Dimension Scaling Bonus Armor Toughness", scaleValue, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }

        if(attackDamage != null) {
            if(info.bonusDamage > 0) {
                attackDamage.removePermanentModifier(scalingBonusDamageUUID);
                attackDamage.addPermanentModifier(new AttributeModifier(scalingBonusDamageUUID, "Scaling Bonus Damage", info.bonusDamage, AttributeModifier.Operation.ADDITION));
            }
            if(dimensionInfo.bonusDamage > 0) {
                float scaleValue = (float)dimensionInfo.bonusDamage / 100.0f;
                attackDamage.removePermanentModifier(dimensionScalingBonusDamageUUID);
                attackDamage.addPermanentModifier(new AttributeModifier(dimensionScalingBonusDamageUUID, "Dimension Scaling Bonus Damage", scaleValue, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }

        final boolean hasLoot = !info.bonusLoot.isEmpty() || !dimensionInfo.bonusLoot.isEmpty();
        if(hasLoot) {
            MonsterWrapper monster = MobTracker.getMonster(entity);
            if(monster != null) {
                for(String tableName : info.bonusLoot) {
                    ResourceLocation fullname = new ResourceLocation(SaiDifficultyMod.MOD_ID, tableName);
                    LootTable lootTable = LootTableConfig.get().find(fullname);
                    if(lootTable != null) {
                        monster.addLoot(lootTable);
                    }
                }

                for(String tableName : dimensionInfo.bonusLoot) {
                    ResourceLocation fullname = new ResourceLocation(SaiDifficultyMod.MOD_ID, tableName);
                    LootTable lootTable = LootTableConfig.get().find(fullname);
                    if(lootTable != null) {
                        monster.addLoot(lootTable);
                    }
                }
            }
        }

        // Adjust the health to reflect the new max health.
        entity.setHealth(entity.getMaxHealth() * healthPercent);
    }

    /** Find the scale info based on distance from spawn.
     * @param distance The distance the mob is from 'spawn' (0,0,0).
     */
    private static MobScaleInfo findScaleInfo(ArrayList<MobScaleInfo> scaling, int distance) {
        MobScaleInfo result = null;
        for(int i = 0, length = scaling.size(); i < length; ++i) {
            result = scaling.get(i);
            if(result.maxDistance >= distance) {
                break;
            }
        }
        return result;
    }

    /** Calculate the 2 dimensional square-distance from spawn
     * @param position The position to calculate from.
     */
    private static int distance2DSqr(BlockPos position) {
        int x = MathHelper.abs(position.getX());
        int z = MathHelper.abs(position.getZ());
        return x * x + z * z;
    }

    /** Calculate the 2 dimensional distance from spawn
     * @param position The position to calculate from.
     */
    private static float distance2D(BlockPos position) {
        // note: We use double as worlds are massive and we want precision.
        return MathHelper.sqrt((double)distance2DSqr(position));
    }


}
