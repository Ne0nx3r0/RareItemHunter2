package com.ne0nx3r0.rih.properties;


public class RareItemProperty {
    private final int id;
    private final String name;
    
    public RareItemProperty(int id,String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }
}
