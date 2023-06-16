package com.god.fractal;

import Entities.Enemy;
import Entities.StandardEnemy;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.god.fractal.Screens.PlayScreen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Float.parseFloat;

public class Timeline {
    public float time;
    public Stack<Node> timeline;
    public CatmullRomSpline<Vector2>[] paths;
    public HashMap<String, StandardEnemy> enemies;
    public short ENEMY_WORLD;

    public Timeline(PlayScreen screen, short enemy) throws IOException {
        timeline = new Stack();
        time = 0;

        BufferedReader reader = new BufferedReader(new FileReader("assets/points")); //read the possible points
        String tempLineRead;
        int P = Integer.parseInt(reader.readLine());
        String[] tempArray;
        String[] tempArray2;
        ArrayList<Vector2>[] points = new ArrayList[P];
        for (int i = 0; i < P; i++) {
            tempLineRead = reader.readLine();
            tempArray = tempLineRead.split(",");
            points[i] = new ArrayList<>();
            System.out.println("new path!");
            for (int j = 0; j < tempArray.length; j++) {
                tempArray2 = tempArray[j].split(" ");
                points[i].add(new Vector2(parseFloat(tempArray2[0]), parseFloat(tempArray2[1])));
                System.out.println(" point parsed " + tempArray2[0] + ", " + tempArray2[1]);
            }
        }

        for (int i = 0; i < P; i++) {
            paths[i] = new CatmullRomSpline<>(points[i].toArray(new Vector2[points[i].size()]), true);
        }
        ENEMY_WORLD = enemy;

        StandardEnemy defect = new StandardEnemy(enemy, );
        StandardEnemy mutatedDefect = new StandardEnemy(enemy, );

        enemies = new HashMap<>(2);
        enemies.put("defect", defect);
        enemies.put("mutatedDefect", mutatedDefect);
    }

    public void insertNode(float d, boolean i, Enemy e, int a) {
        timeline.push(new Node(d, i, e, a));
    }

    public void update(float delta) {
        if (!timeline.isEmpty()) {
            Node curnode = timeline.peak();
            time -= delta;
            if (time <= 0 && curnode.indefinite == false) {
                curnode = timeline.pop();
                time = curnode.delta;
                for (int i = 0; i < curnode.amount; i++) {

                }
            }
        }
    }

    public void end() {

    }
}

class Node {
    public float delta; //time required to pass until it automatically ends
    public boolean indefinite; // do the enemies have to be killed before the timeline can move on
    public Enemy enemy; //type of enemy to spawn
    public int amount; //amount of enemies

    public Node(float d, boolean i, Enemy e, int a) {
        delta = d;
        indefinite = i;
        enemy = e;
        amount = a;
    }
}