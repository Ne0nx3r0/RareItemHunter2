package com.ne0nx3r0.rih.boss.entities;

import java.lang.reflect.Field;
import java.util.Map;

public class EntityLoader {
    public void load(){
        EntityLoader.addBossEntity(BossEntityZombie.class, "BossZombie", 54);
        EntityLoader.addBossEntity(BossEntityEnderman.class, "BossEnderman", 58);
        EntityLoader.addBossEntity(BossEntityPig.class, "BossPig", 90);
        EntityLoader.addBossEntity(BossEntityChicken.class, "BossChicken", 93);
        EntityLoader.addBossEntity(BossEntityOcelot.class, "BossOcelot", 98);
        EntityLoader.addBossEntity(BossEntitySnowman.class, "BossSnowman", 97);
        EntityLoader.addBossEntity(BossEntityIronGolem.class, "BossIronGolem", 99);
    }
    
// Begin strange registration code

// registery... thingy...
    protected static Field mapStringToClassField, mapClassToStringField, mapClassToIdField, mapStringToIdField;
    //protected static Field mapIdToClassField;
 
    static
    {
        try
        {
            mapStringToClassField = net.minecraft.server.v1_7_R4.EntityTypes.class.getDeclaredField("c");
            mapClassToStringField = net.minecraft.server.v1_7_R4.EntityTypes.class.getDeclaredField("d");
            //mapIdtoClassField = net.minecraft.server.v1_7_R1.EntityTypes.class.getDeclaredField("e");
            mapClassToIdField = net.minecraft.server.v1_7_R4.EntityTypes.class.getDeclaredField("f");
            mapStringToIdField = net.minecraft.server.v1_7_R4.EntityTypes.class.getDeclaredField("g");

            mapStringToClassField.setAccessible(true);
            mapClassToStringField.setAccessible(true);
            //mapIdToClassField.setAccessible(true);
            mapClassToIdField.setAccessible(true);
            mapStringToIdField.setAccessible(true);
        }
        catch(Exception e) {e.printStackTrace();}
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected static void addBossEntity(Class entityClass, String name, int id)
    {
        if (mapStringToClassField == null || mapStringToIdField == null || mapClassToStringField == null || mapClassToIdField == null)
        {
            return;
        }
        else
        {
            try
            {
                Map mapStringToClass = (Map) mapStringToClassField.get(null);
                Map mapStringToId = (Map) mapStringToIdField.get(null);
                Map mapClasstoString = (Map) mapClassToStringField.get(null);
                Map mapClassToId = (Map) mapClassToIdField.get(null);

                mapStringToClass.put(name, entityClass);
                mapStringToId.put(name, Integer.valueOf(id));
                mapClasstoString.put(entityClass, name);
                mapClassToId.put(entityClass, Integer.valueOf(id));

                mapStringToClassField.set(null, mapStringToClass);
                mapStringToIdField.set(null, mapStringToId);
                mapClassToStringField.set(null, mapClasstoString);
                mapClassToIdField.set(null, mapClassToId);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
