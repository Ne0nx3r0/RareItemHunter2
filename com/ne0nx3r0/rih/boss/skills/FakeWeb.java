package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FakeWeb extends BossSkillTemplate
{
    public FakeWeb()
    {
        super("Fake Web");
    }

    @Override
    public boolean activateOnHitSkill(LivingEntity bossEntity, Boss boss, LivingEntity target, int level, int damageTaken){      
        if(target instanceof Player)
        {
            Block block = target.getLocation().getBlock();
            Player pAttacker = (Player) target;

            BlockFace[] bfs = new BlockFace[]{
                BlockFace.SELF,
                BlockFace.UP,
                BlockFace.EAST,
                BlockFace.WEST,
                BlockFace.SOUTH,
                BlockFace.NORTH
            };
            
            for(BlockFace bf : bfs)
            {
                if(block.getRelative(bf).getType() == Material.AIR)
                {
                    pAttacker.sendBlockChange(block.getRelative(bf).getLocation(), Material.WEB, (byte) 0x0);
                }                
            }

            return true;
        }
        return false;
    }
}
