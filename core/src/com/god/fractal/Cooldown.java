package com.god.fractal;

public class Cooldown {
    float[] timers;
    float[] cooldowns;
    public Cooldown(float[] cooldowns){
        this.cooldowns = cooldowns;
        timers = new float[cooldowns.length];
        for (int i = 0; i < cooldowns.length; i++) {
            timers[i] = cooldowns[i];
        }
    }
    public void reset(){
        for (int i = 0; i < cooldowns.length; i++) {
            timers[i] = cooldowns[i];
        }
    }
    public boolean isOver(int index){
        if (timers[index] < 0){
            timers[index] = cooldowns[index];
            return true;
        }
        return false;
    }
    public boolean isOverCheckOnly(int index){
        if (timers[index] < 0){
            return true;
        }
        return false;
    }
    public void update(float delta){
        for (int i = 0; i < timers.length; i++) {
            timers[i] = timers[i]-delta;
        }
    }
    public float get(int index){
        return timers[index];
    }
}
