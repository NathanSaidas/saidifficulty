package com.saidas.saiscaling.core.info;

/** A builder class to build MobScaleInfo **/
public class MobScaleInfoBuilder {

    /** Internal object the builder actively builds. **/
    private MobScaleInfo object = new MobScaleInfo();

    /** Set the maxDistance **/
    public MobScaleInfoBuilder maxDistance(int distance) {
        object.maxDistance = distance;
        return this;
    }

    /** Set the maxHealth **/
    public MobScaleInfoBuilder bonusMaxHealth(int value) {
        object.bonusMaxHealth = value;
        return this;
    }

    /** Set the armor **/
    public MobScaleInfoBuilder bonusArmor(int value) {
        object.bonusArmor = value;
        return this;
    }

    /** Set the armor toughness **/
    public MobScaleInfoBuilder bonusArmorToughness(int value) {
        object.bonusArmorToughness = value;
        return this;
    }

    /** Set the damage **/
    public MobScaleInfoBuilder bonusDamage(int value) {
        object.bonusDamage = value;
        return this;
    }

    /** 'pop' the internal object off and return it. A new object is created for the internal object. **/
    public MobScaleInfo build() {
        MobScaleInfo result = object;
        object = new MobScaleInfo();
        return result;
    }

}
