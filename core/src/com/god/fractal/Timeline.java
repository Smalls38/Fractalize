package com.god.fractal;

import Entities.Enemy;
import Entities.StandardEnemy;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.god.fractal.Screens.PlayScreen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Float.parseFloat;

public class Timeline {
    public float time;
    //using stack for the nodes but just parsing them backwards, writing a queue takes more time than just having the stage
    //file backwards
    public Stack<Node> timeline;

    public CatmullRomSpline<Vector2>[] paths;
    public HashMap<String, StandardEnemy> enemies;
    public short ENEMY_WORLD;
    public PlayScreen screen;
    public Node curnode;
    public Timeline(PlayScreen screen, short enemyLayer, short playerLayer, short bulletLayer) throws IOException {
        timeline = new Stack();
        time = 0;
        this.screen = screen;

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
        paths = new CatmullRomSpline[P];
        for (int i = 0; i < P; i++) {
            paths[i] = new CatmullRomSpline<>(points[i].toArray(new Vector2[points[i].size()]), true);
        }
        ENEMY_WORLD = enemyLayer;

        StandardEnemy defect = new StandardEnemy(enemyLayer, playerLayer, bulletLayer,
                BodyDef.BodyType.KinematicBody, new Sprite(new Texture("assets/enemyDefect.png")), 0.1f, screen.PPM);

        StandardEnemy mutatedDefect = new StandardEnemy(enemyLayer, playerLayer, bulletLayer,
                BodyDef.BodyType.KinematicBody, new Sprite(new Texture("assets/enemyDefect.png")), 0.2f,screen.PPM );

        enemies = new HashMap<>(4);
        enemies.put("defect", defect);
        enemies.put("mutatedDefect", mutatedDefect);

        reader = new BufferedReader(new FileReader("assets/stage1"));
        int N = Integer.parseInt(reader.readLine());

        for (int i = 0; i < N; i++) {
            tempLineRead = reader.readLine();
            tempArray = tempLineRead.split(" ");
            insertNode(parseFloat(tempArray[0]), parseBoolean(tempArray[1]), enemies.get(tempArray[2]), Integer.parseInt(tempArray[3]));
        }
        //set curnode as first pop
    }

    public void insertNode(float d, boolean i, StandardEnemy e, int a) {
        timeline.push(new Node(d, i, e, a));
    }

    public void update(float delta) {
        if (!timeline.isEmpty()) {
            curnode = timeline.peak();
            time -= delta;
            //System.out.println("delta and time is " + delta + " , " + time );
            if (time <= 0 && curnode.indefinite == false) {
                endAndKill();
                curnode = timeline.pop();
                time = curnode.delta;
                System.out.println("SPAWNING " + curnode.amount + " of enemies");
                for (int i = 0; i < curnode.amount; i++) {
                    curnode.enemy.makeEnemy(screen, paths[(int) (Math.random()*paths.length)]);
                }
            }
        }
    }

    /**
     * ends the current node and time, this might seem useless but it prevents players from
     * waiting for an eternity if they killed all enemies too fast
     */
    public void end() {
        if (!timeline.isEmpty() && time < 0 ) {
            curnode = timeline.pop();
            time = curnode.delta;
            System.out.println("SPAWNING " + curnode.amount + " of enemies");
            for (int i = 0; i < curnode.amount; i++) {
                curnode.enemy.makeEnemy(screen, paths[(int) (Math.random()*paths.length)]);
            }
        }

    }
    public void endAndKill(){
        screen.killAllEnemies();
    }
}

class Node {
    public float delta; //time required to pass until it automatically ends
    public boolean indefinite; // do the enemies have to be killed before the timeline can move on
    public StandardEnemy enemy; //type of enemy to spawn
    public int amount; //amount of enemies

    public Node(float d, boolean i, StandardEnemy e, int a) {
        delta = d;
        indefinite = i;
        enemy = e;
        amount = a;
    }
}