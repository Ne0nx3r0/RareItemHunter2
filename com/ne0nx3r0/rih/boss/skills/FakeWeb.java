package com.ne0nx3r0.rih.boss.skills;

import com.ne0nx3r0.rih.boss.Boss;
import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FakeWeb extends BossSkillTemplate
{
    public FakeWeb()
    {
        super("Fake Web");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        if(eAttacker instanceof Player)
        {
            Block block = eAttacker.getLocation().getBlock();
            Player pAttacker = (Player) eAttacker;

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
