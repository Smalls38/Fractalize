package com.god.fractal;

import Entities.Enemy;

import java.util.ArrayList;

public class Timeline {
    public float time;
    public ArrayList<Node> timeline;
    public Timeline(){
        timeline = new ArrayList<>();
        time = 0;
    }
    public void insertNode(float d, boolean i, Enemy e, int a ){
        timeline.add(new Node(d, i, e, a));
    }
    //public;
}
class Node {
    public float delta; //time required to pass until it automatically ends
    public boolean indefinite; // do the enemies have to be killed before the timeline can move on
    public Enemy enemy; //type of enemy to spawn
    public int amount; //amount of enemies
    public Node (float d, boolean i, Enemy e, int a ){
        delta = d;
        indefinite = i;
        enemy = e;
        amount = a;
    }
}