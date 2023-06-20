package com.god.fractal;
/**
 * Class to keep track of times, since frames are not reliable, this class gets updated with time past and allows me to
 * do things right as the frame that is past the time indicated
 */
public class Cooldown {
    float[] timers; // the time remaining
    float[] cooldowns; // the actual time length
    public Cooldown(float[] cooldowns){
        this.cooldowns = cooldowns;
        timers = new float[cooldowns.length];
        //doing this so timers is not a memory reference to cooldown
        for (int i = 0; i < cooldowns.length; i++) {
            timers[i] = cooldowns[i];
        }
    }
    //rest everything in timers, this is for when the player dies but have extra life, I aint doing that here but
    //just in case for the future
    public void reset(){
        for (int i = 0; i < cooldowns.length; i++) {
            timers[i] = cooldowns[i];
        }
    }

    /*
     checks if the indicated cooldown is over, if it is then it returns true and restarts the timer
     */
    public boolean isOver(int index){
        if (timers[index] < 0){
            timers[index] = cooldowns[index];
            return true;
        }
        return false;
    }

    /*
     this is like the previous class but doesn't actually restart the timer
     */
    public boolean isOverCheckOnly(int index){
        if (timers[index] < 0){
            return true;
        }
        return false;
    }
    //updates all the timers
    public void update(float delta){
        for (int i = 0; i < timers.length; i++) {
            timers[i] = timers[i]-delta;
        }
    }
    public float get(int index){
        return timers[index];
    }
}
