
package com.ne0nx3r0.rih.boss;

import com.ne0nx3r0.rih.boss.skills.BossSkillTemplate;
import com.ne0nx3r0.rih.boss.skills.BossSkillInstance;
import com.ne0nx3r0.rih.entities.BossEntityType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class BossTemplate {
    private final String name;
    private final int attackPower;
    private final int maxHealth;
    private final BossEntityType bossEntityType;
    private final int difficulty;
    private List<ItemStack> equipment;
    private ItemStack weapon;
    private List<BossSkillInstance> onHitSkills;
        
    public BossTemplate(String name,BossEntityType entityType,int maxHealth,int attackPower,int difficulty,List<ItemStack> equipment,ItemStack weapon)
    {
        this.name = name;
        this.attackPower = attackPower;
        this.maxHealth = maxHealth;
        this.bossEntityType = entityType;
        this.difficulty = difficulty;
        
        if(equipment != null && !equipment.isEmpty())
        {
            this.equipment = equipment;
        }
        
        if(weapon != null)
        {
            this.weapon = weapon;
        }
    }

    public void addOnHitSkill(BossSkillTemplate bs,int level,int chance)
    {
        if(this.onHitSkills == null)
        {
            this.onHitSkills = new ArrayList<BossSkillInstance>();
        }
        
        this.onHitSkills.add(new BossSkillInstance(bs,level,chance));
    }
    
    public List<BossSkillInstance> getOnHitSkills(){
        return this.onHitSkills;
    }
    
    public int getDifficulty(){
        return this.difficulty;
    }
    
    public int getAttackPower(){
        return this.attackPower;
    }
    
    public int getMaxHealth(){
        return this.maxHealth;
    }
    
    public String getName(){
        return this.name;
    }
    
    public BossEntityType getBossEntityType(){
        return this.bossEntityType;
    }
    
    public List<ItemStack> getEquipment(){
        return this.equipment;
    }
    
    public ItemStack getWeapon(){
        return this.weapon;
    }
}
