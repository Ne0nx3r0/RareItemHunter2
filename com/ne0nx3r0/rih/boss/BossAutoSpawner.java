package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.RareItemHunterPlugin;
import com.ne0nx3r0.rih.boss.egg.BossEgg;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BossAutoSpawner implements Runnable{
    private final double AUTOSPAWN_DISTANCE;
    private final RareItemHunterPlugin plugin;

    public BossAutoSpawner(RareItemHunterPlugin plugin)
    {
        this.plugin = plugin;
        
        // So don't square before you hand it to the object!
        this.AUTOSPAWN_DISTANCE = 15 * 15;
    }

    @Override
    public void run()
    {        
        for (BossEgg egg : plugin.getBossManager().getAllActiveEggs()) {
            if(egg.getAutoSpawn()) {                
                for(Player p : egg.getLocation().getWorld().getPlayers()) {
                    if(p.getLocation().distanceSquared(egg.getLocation()) < this.AUTOSPAWN_DISTANCE)
                    {        
                        final BossEgg eggToHatch = egg;
                        final String awakener = p.getName();

                        // Jump back into sync
                        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                plugin.getLogger().log(Level.INFO, "A legendary monster egg has been awakened at X:{0} Y:{1} Z:{2}]", new Object[]{
                                    eggToHatch.getLocation().getBlockX(), 
                                    eggToHatch.getLocation().getBlockX(), 
                                    eggToHatch.getLocation().getBlockX()}
                                );

                                for(Player p : eggToHatch.getLocation().getWorld().getPlayers())
                                {
                                    p.sendMessage(ChatColor.DARK_GREEN+"Legendary boss "+ChatColor.WHITE+eggToHatch.getTemplate().getName()+ChatColor.DARK_GREEN+" has been awakened by "+ChatColor.WHITE+awakener+ChatColor.DARK_GREEN+"!");
                                }

                                plugin.getBossManager().hatchEggIfBoss(eggToHatch.getLocation().getBlock());
                            }
                        },1);

                        // only spawn one egg per attempt, could help with lag if a mass of eggs occurs
                        return;
                    }
                }
            }
        }
    }
}
