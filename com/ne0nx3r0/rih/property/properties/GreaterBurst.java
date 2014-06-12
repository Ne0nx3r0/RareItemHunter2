package com.ne0nx3r0.rih.property.properties;

import com.ne0nx3r0.rih.property.PropertyCostType;
import com.ne0nx3r0.rih.property.PropertyType;
import com.ne0nx3r0.rih.property.RareItemProperty;
import com.ne0nx3r0.util.FireworkVisualEffect;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class GreaterBurst extends RareItemProperty
{
    private final FireworkVisualEffect fireworks;
    
    public GreaterBurst()
    {
        super(
            PropertyType.GREATER_BURST.ordinal(),
            "Greater Burst",
            "Forcibly shoves all nearby creatures away",
            PropertyCostType.FOOD,
            5,
            5
        );
        
        this.fireworks = new FireworkVisualEffect();
    }
    
    @Override
    public boolean onDamageOther(EntityDamageByEntityEvent e, Player player, int level)
    {
        return this.activate(player,level);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        return this.activate(e.getPlayer(),level);
    }

    private boolean activate(Player player,int level)
    {
        List<Entity> nearbyEntities = player.getNearbyEntities(8, 8, 8);
        
        if(!nearbyEntities.isEmpty())
        {
            boolean showFx = false;
            Vector vPlayer = player.getLocation().toVector();
            
            for(Entity ent : nearbyEntities)
            {
                if(ent instanceof LivingEntity)
                {
                    Vector unitVector = ent.getLocation().toVector().subtract(vPlayer).normalize();

                    unitVector.setY(0.55/level);

                    ent.setVelocity(unitVector.multiply(level*2));
                    
                    showFx = true;
                }
            }
            
            if(showFx)
            {
                try
                {
                    fireworks.playFirework(
                        player.getWorld(), player.getLocation(),
                        FireworkEffect
                            .builder()
                            .with(Type.BALL_LARGE)
                            .withColor(Color.WHITE)
                            .build()
                    );
                }
                catch (Exception ex)
                {
                    //For enabling later, for now just let older versions gracefully downgrade. 
                    //player.sendMessage(ChatColor.RED+"An error occurred while executing this RareItem's power.");
                    //Logger.getLogger(GreaterBurst.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                return false;
            }
            
            return true;
        }
        
        return false;
    }
}