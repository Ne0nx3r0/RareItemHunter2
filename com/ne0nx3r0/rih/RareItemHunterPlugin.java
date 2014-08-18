package com.ne0nx3r0.rih;

import com.earth2me.essentials.Essentials;
import com.ne0nx3r0.badges.LonelyBadgesAPI;
import com.ne0nx3r0.rih.boss.spawning.SpawnPointManager;
import com.ne0nx3r0.rih.listeners.PlayerListener;
import com.ne0nx3r0.rih.listeners.BossListener;
import com.ne0nx3r0.rih.boss.BossManager;
import com.ne0nx3r0.rih.commands.RareItemHunterCommandExecutor;
import com.ne0nx3r0.rih.gui.GuiManager;
import com.ne0nx3r0.rih.property.PropertyManager;
import com.ne0nx3r0.rih.recipe.RecipeManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.ne0nx3r0.rih.boss.entities.EntityLoader;

public class RareItemHunterPlugin extends JavaPlugin{
    private BossManager bossManager;
    private RecipeManager recipeManager;
    private PropertyManager propertyManager;
    private GuiManager guiManager;
    private SpawnPointManager spawnPointManager;
    private Essentials essentials;
    private Economy economy;
    
    public final String RIH_ITEMS_CRAFTED = "rih_items_crafted";
    public final String RIH_MOST_DAMAGE_TO_BOSS = "rih_boss_most_damage";
    private LonelyBadgesAPI lb;
    
    @Override
    public void onEnable(){
        getDataFolder().mkdirs();
        
        File configFile = new File(getDataFolder(),"config.yml");
        
        if(!configFile.exists())
        {
            copy(getResource("config.yml"), configFile);
        }
        
        // load entities to the server - are you at all surprised that's what happens with this line?
        new EntityLoader().load();
        
        this.essentials = ((Essentials) Bukkit.getPluginManager().getPlugin("Essentials"));
        
        this.setupEconomy();
        
        this.recipeManager = new RecipeManager(this);
        
        this.propertyManager = new PropertyManager(this);
        
        this.bossManager = new BossManager(this);
        
        this.spawnPointManager = new SpawnPointManager(this);
        
        this.guiManager = new GuiManager(this);
        
        RareItemHunterCommandExecutor executor = new RareItemHunterCommandExecutor(this);
        
        this.getCommand("ri").setExecutor(executor);
        this.getCommand("hat").setExecutor(executor);
        
        this.getServer().getPluginManager().registerEvents(new BossListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);  
        
        // Register badge properties
        LonelyBadgesAPI lb = (LonelyBadgesAPI) this.getServer().getPluginManager().getPlugin("LonelyBadges");
        
        if(lb != null && lb.isEnabled()){
            this.lb = lb;
            
            this.lb.registerBadgeProperty(this.RIH_ITEMS_CRAFTED,"# of rare items crafted");
            this.lb.registerBadgeProperty(this.RIH_MOST_DAMAGE_TO_BOSS,"# of times did the most damage to a boss");
        }
    }
    
    public BossManager getBossManager(){
        return this.bossManager;
    }
    
    public RecipeManager getRecipeManager(){
        return this.recipeManager;
    }
    
    public PropertyManager getPropertymanager(){
        return this.propertyManager;
    }

    public SpawnPointManager getSpawnPointManager() {
        return this.spawnPointManager;
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }
    
    public Essentials getEssentials() {
        return this.essentials;
    }
    
    public Economy getEconomy(){
        return this.economy;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if(economyProvider != null)
        {
            this.economy = economyProvider.getProvider();
        }
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

    public LonelyBadgesAPI getLonelyBadgesAPI() {
        return this.lb;
    }
}
