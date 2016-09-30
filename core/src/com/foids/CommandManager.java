package com.foids;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;

/**
 * Created by 1544256 on 2016-09-30.
 */
public class CommandManager {


    private FoidsGame game;
    private boolean hitbox;
    private LinkedList<Foid> foidList;
    private SpriteBatch batch;

    public CommandManager(FoidsGame game)
    {
        this.game = game;
        this.hitbox = false;
        this.foidList = game.getFoidList();
        this.batch = game.getBatch();

    }

    /*
    Command list
    Show Vectors (Direction only) (V)
    Show Vectors w/ length (V toggle)
    Show Hitbox (H)
    Show Pointing Position
    Show Flow Field (F)
    Show Foid Origin (O)
    Speed (Arrows)
    Show ALL
    Remove ALL
    */

    public void toggleHitbox()
    {
        hitbox = !hitbox;
    }

    public void draw()
    {
        if(hitbox)
            drawHitboxes();
    }

    public void drawHitboxes()
    {
        for(Foid foid : foidList)
        {

        }
    }
}
