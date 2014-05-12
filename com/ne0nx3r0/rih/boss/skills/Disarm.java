package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import java.util.Random;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Disarm extends BossSkillTemplate
{
    public Disarm()
    {
        super("Disarm");
    }
  
    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){      
        if(target instanceof Player)
        {
            Player pAttacker = (Player) target;

            if(pAttacker.getItemInHand() != null)
            {
                int iRandomSlot = (new Random()).nextInt(44)+9;

                ItemStack swapOut = pAttacker.getInventory().getItem(pAttacker.getInventory().getHeldItemSlot());
                ItemStack swapIn = pAttacker.getInventory().getItem(iRandomSlot);

                pAttacker.getInventory().setItem(pAttacker.getInventory().getHeldItemSlot(), swapIn);
                pAttacker.getInventory().setItem(iRandomSlot, swapOut);

                pAttacker.sendMessage("You have been disarmed!");

                return true;
            }
        }
        
        return false;
    }
}
