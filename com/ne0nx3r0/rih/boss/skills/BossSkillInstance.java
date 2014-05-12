
package com.ne0nx3r0.rih.boss.skills;

public class BossSkillInstance
{
    private final int chance;
    private final int level;
    private final BossSkillTemplate bossSkill;
    
    public BossSkillInstance(BossSkillTemplate bossSkill,int level,int chance)
    {
        this.bossSkill = bossSkill;
        this.level = level;
        this.chance = chance;
    }
    
    public int getLevel(){
        return this.level;
    }
    
    public int getChance(){
        return this.chance;
    }
    
    public BossSkillTemplate getSkillTemplate(){
        return this.bossSkill;
    }
}
