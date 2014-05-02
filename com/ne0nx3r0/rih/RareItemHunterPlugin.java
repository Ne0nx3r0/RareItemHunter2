package com.ne0nx3r0.rih;

import com.ne0nx3r0.rih.commands.RareItemHunterCommandExecutor;
import com.ne0nx3r0.rih.entities.*;
import java.lang.reflect.Field;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

public class RareItemHunterPlugin extends JavaPlugin{
    @Override
    public void onEnable(){
        this.getCommand("ri2").setExecutor(new RareItemHunterCommandExecutor(this));
        
        RareItemHunterPlugin.addBossEntity(BossEntityZombie.class, "BossZombie", 54);
        RareItemHunterPlugin.addBossEntity(BossEntityOcelot.class, "BossOcelot", 98);
        RareItemHunterPlugin.addBossEntity(BossEntityChicken.class, "BossCucco", 93);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
// Begin strange registration code

// registery... thingy...
    protected static Field mapStringToClassField, mapClassToStringField, mapClassToIdField, mapStringToIdField;
    //protected static Field mapIdToClassField;
 
    static
    {
        try
        {
            mapStringToClassField = net.minecraft.server.v1_7_R3.EntityTypes.class.getDeclaredField("c");
            mapClassToStringField = net.minecraft.server.v1_7_R3.EntityTypes.class.getDeclaredField("d");
            //mapIdtoClassField = net.minecraft.server.v1_7_R1.EntityTypes.class.getDeclaredField("e");
            mapClassToIdField = net.minecraft.server.v1_7_R3.EntityTypes.class.getDeclaredField("f");
            mapStringToIdField = net.minecraft.server.v1_7_R3.EntityTypes.class.getDeclaredField("g");

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
