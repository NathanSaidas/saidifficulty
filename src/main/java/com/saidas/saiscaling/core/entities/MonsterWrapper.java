package com.saidas.saiscaling.core.entities;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.loot.LootTable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MonsterWrapper {
    private ArrayList<LootTable> lootTables = new ArrayList<>();
    public final MonsterEntity observed;

    public MonsterWrapper(MonsterEntity entity) {
        observed = entity;
    }

    public void addLoot(LootTable tbl) {
        lootTables.add(tbl);
    }

    public void forEachLoot( Consumer<LootTable> func) {
        lootTables.forEach(func);
    }

    public boolean isRemoved() { return observed != null || observed.isAlive(); }
}
