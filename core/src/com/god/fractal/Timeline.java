package com.god.fractal;

import Entities.Enemy;
import Entities.EnemyBullet;
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

/**
 * A class that takes care of everything enemy related, including initializing them, parsing their path,
 * checking when to spawn them, etc.
 */
public class Timeline {
    public float time; // the current time past since last new node
    //using stack for the nodes but just parsing them backwards, writing a queue takes more time than just having the stage
    //file backwards
    public Stack<Node> timeline; //all the nodes
    public CatmullRomSpline<Vector2>[] paths; //all the paths that the enemy can have
    /*
    a map that maps a string to an enemy type, used for parsing. A map is not needed but I just added it for future proofing
     */
    public HashMap<String, StandardEnemy> enemies;
    public short ENEMY_WORLD;
    public PlayScreen screen;
    public Node curnode; //current node
    public Timeline(PlayScreen screen, short enemyLayer, short bulletLayer) throws IOException {
        timeline = new Stack();
        time = 0;
        this.screen = screen;

        BufferedReader reader = new BufferedReader(new FileReader("assets/points")); //read the possible points
        String tempLineRead; //the entire line
        int P = Integer.parseInt(reader.readLine()); //how many paths there are
        String[] tempArray; //the array that will hold every points pair
        String[] tempArray2; //the array that will hold the x and y coordinates
        ArrayList<Vector2>[] points = new ArrayList[P]; //the final array to parse into (this counts as a 2d array i think)
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
        //for every list of points, make it into a catmullromspline path
        paths = new CatmullRomSpline[P];
        for (int i = 0; i < P; i++) {
            paths[i] = new CatmullRomSpline<>(points[i].toArray(new Vector2[points[i].size()]), true);
        }
        ENEMY_WORLD = enemyLayer;

        Sprite bulletSprite = new Sprite(new Texture("assets/enemyBullet.png"));
        //theres only going to be one type of bullet cuz im too lazy
        EnemyBullet defectBullet = new EnemyBullet (bulletLayer, BodyDef.BodyType.KinematicBody, bulletSprite,
                new Vector2(bulletSprite.getWidth(), bulletSprite.getHeight()), screen.PPM, 8f, 10f);

        //two types of enemies, theyre mostly the same but one is faster and more damaging, with less health
        StandardEnemy defect = new StandardEnemy(enemyLayer, 0.8f,
                BodyDef.BodyType.DynamicBody, new Sprite(new Texture("assets/enemyDefect.png")), 0.1f, screen.PPM, 300, 30, defectBullet);

        StandardEnemy mutatedDefect = new StandardEnemy(enemyLayer, 0.5f,
                BodyDef.BodyType.DynamicBody, new Sprite(new Texture("assets/enemyDefect.png")), 0.2f,screen.PPM, 150, 50, defectBullet);
        // all types of enemies are in the map, since theres only 2 types right now ill have 4 as the initial size
        enemies = new HashMap<>(4);
        enemies.put("defect", defect);
        enemies.put("mutatedDefect", mutatedDefect);

        //parse the stage file, same logic as before but its a lot easier
        reader = new BufferedReader(new FileReader("assets/stage1"));
        int N = Integer.parseInt(reader.readLine());

        for (int i = 0; i < N; i++) {
            tempLineRead = reader.readLine();
            tempArray = tempLineRead.split(" ");
            insertNode(parseFloat(tempArray[0]), parseBoolean(tempArray[1]), enemies.get(tempArray[2]), Integer.parseInt(tempArray[3]));
        }
    }

    public void insertNode(float d, boolean i, StandardEnemy e, int a) {
        timeline.push(new Node(d, i, e, a));
    }
    /*
    the method that will go onto the next nodes and summon enemies when the correct amount of time has past
     */
    public void update(float delta) {
        if (!timeline.isEmpty()) {
            curnode = timeline.peak();
            time -= delta; //decrease the time
            //if enough time has past and the node is allowed to move on without all enemies being dead
            if (time <= 0 && !curnode.indefinite) {
                endAndKill(); //kill ALL ENEMIES
                curnode = timeline.pop(); //go to next node
                time = curnode.delta; //set the time
                System.out.println("SPAWNING " + curnode.amount + " of enemies");
                //summon all the enemies
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