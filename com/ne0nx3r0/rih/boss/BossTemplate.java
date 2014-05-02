
package com.ne0nx3r0.rih.boss;

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
    private List<BossSkill> skills;
        
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

    public void addSkill(BossSkillTemplate bs,int level,int chance)
    {
        if(this.skills == null)
        {
            this.skills = new ArrayList<BossSkill>();
        }
        
        this.skills.add(new BossSkill(bs,level,chance));
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
