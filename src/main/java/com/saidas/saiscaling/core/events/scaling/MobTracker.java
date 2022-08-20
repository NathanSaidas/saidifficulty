package com.saidas.saiscaling.core.events.scaling;

import com.saidas.saiscaling.core.entities.MonsterWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;

import java.util.HashMap;
import java.util.UUID;

public class MobTracker {
    private static final HashMap<UUID, MonsterWrapper> entities = new HashMap();

    public static void track(MonsterEntity entity) {
        if(entity != null) {
            entities.put(entity.getUUID(), new MonsterWrapper(entity));
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
