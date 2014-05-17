package com.ne0nx3r0.rih;

import com.ne0nx3r0.rih.boss.entities.BossEntityPig;
import com.ne0nx3r0.rih.boss.entities.BossEntityZombie;
import com.ne0nx3r0.rih.boss.entities.BossEntityChicken;
import com.ne0nx3r0.rih.boss.entities.BossEntityOcelot;
import com.ne0nx3r0.rih.boss.entities.BossEntityEnderman;
import com.ne0nx3r0.rih.listeners.RareItemHunterPlayerListener;
import com.ne0nx3r0.rih.listeners.RareItemHunterBossListener;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.boss.entities.BossEntitySnowman;
import com.ne0nx3r0.rih.commands.RareItemHunterCommandExecutor;
import com.ne0nx3r0.rih.property.RareItemPropertyManager;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

public class RareItemHunterPlugin extends JavaPlugin{
    private BossManager bossManager;
    private RecipeManager recipeManager;
    private RareItemPropertyManager propertyManager;
    
    @Override
    public void onEnable(){
        getDataFolder().mkdirs();
        
        File configFile = new File(getDataFolder(),"config.yml");
        
        if(!configFile.exists())
        {
            copy(getResource("config.yml"), configFile);
        }
        
        RareItemHunterPlugin.addBossEntity(BossEntityZombie.class, "BossZombie", 54);
        RareItemHunterPlugin.addBossEntity(BossEntityEnderman.class, "BossEnderman", 58);
        RareItemHunterPlugin.addBossEntity(BossEntityPig.class, "BossPig", 90);
        RareItemHunterPlugin.addBossEntity(BossEntityChicken.class, "BossChicken", 93);
        RareItemHunterPlugin.addBossEntity(BossEntityOcelot.class, "BossOcelot", 98);
        RareItemHunterPlugin.addBossEntity(BossEntitySnowman.class, "BossSnowman", 97);
        
        this.propertyManager = new RareItemPropertyManager(this);
        
        this.recipeManager = new RecipeManager(this);
        
        this.bossManager = new BossManager(this);
        
        this.getCommand("ri2").setExecutor(new RareItemHunterCommandExecutor(this));
        
        this.getServer().getPluginManager().registerEvents(new RareItemHunterBossListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RareItemHunterPlayerListener(this), this);
    }
    
    public BossManager getBossManager(){
        return this.bossManager;
    }
    
    public RecipeManager getRecipeManager(){
        return this.recipeManager;
    }
    
    public RareItemPropertyManager getPropertymanager(){
        return this.propertyManager;
    }
    
    

    
    
    
    
    
// Public helper methods
    
    public void copy(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0)
            {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
