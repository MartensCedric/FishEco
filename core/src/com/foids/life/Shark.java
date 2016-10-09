package com.foids.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.foids.FishEco;

import java.util.Random;

/**
 * Created by Cedric Martens on 2016-10-08.
 */
public class Shark {

    private FishEco game;

    private Vector2 desired;
    private Vector2 force;
    private Vector2 velocity;
    private Vector2 location;
    private Vector2 originVector;

    private float sight;

    private Food foodTarget;
    private int foodAte;

    private int width;
    private int height;

    private int originRelativeToFishX;
    private int originRelativeToFishY;

    private float maxSpeed;

    private float dir;
    private float mass;

    private static int idCounter = 0;
    private int id;

    private TextureRegion textureRegion;

    public Shark()
    {
        Random randomizer = new Random();
        location = new Vector2(randomizer.nextInt(Gdx.graphics.getWidth()), randomizer.nextInt(Gdx.graphics.getHeight()));

        this.mass = 3.5f;



        this.foodTarget = null;
        this.foodAte = 0;

        createTexture();

        this.width = textureRegion.getRegionWidth();
        this.height = textureRegion.getRegionHeight();

        this.originRelativeToFishX = width/2;
        this.originRelativeToFishY = height/2;

    }

    private void createTexture()
    {
        Texture texture = new Texture(new FileHandle("shark.png"));
        textureRegion = new TextureRegion(texture);
    }
}
