package com.saidas.saiscaling.core.info;

import java.util.ArrayList;

/** MobScaleInfo describes how mobs should be scaled when they are spawned. (see MobScaling) **/
public class MobScaleInfo {
    /** The maximum distance the scale info applies to. **/
    public int maxDistance = 0;
    /** The bonus max health the entity receives. base=(base+value) **/
    public int bonusMaxHealth = 0;
    /** The bonus damage the entity receives. base=(base+value) **/
    public int bonusDamage = 0;
    /** The bonus armor the entity receives. base=(base+value) **/
    public int bonusArmor = 0;
    /** The bonus armor toughness the entity receives. base=(base+value) **/
    public int bonusArmorToughness = 0;
    /** The bonus loot a mob can drop. (These are references to the loot tables stored in /mod_name/loottables/ )**/
    public ArrayList<String> bonusLoot = new ArrayList<>();
}
