package com.foids.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.foids.FlowField;
import com.foids.life.Fish;
import com.foids.FishEco;

import java.util.LinkedList;

/**
 * Created by 1544256 on 2016-09-30.
 */
public class CommandManager {


    private FishEco game;

    private LinkedList<Fish> fishList;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private Gdx2DPixmap hitboxPxMap2D;
    private Texture hitboxTexture;
    private boolean hitbox;

    private Gdx2DPixmap originPxMap2D;
    private Texture originTexture;
    private int originMarkerSize;
    private boolean origin;

    private boolean direction;
    private int lineSize;

    private boolean flowField;
    private FlowField field;

    public CommandManager(FishEco game)
    {
        this.game = game;

        this.fishList = game.getFishList();
        this.batch = game.getBatch();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.field = game.getField();

        this.hitbox = false;
        this.origin = false;
        this.direction = false;

        this.originMarkerSize = 1;
        this.lineSize = 3;

        createTextures();
    }

    /*
    Command list
    Show Vectors (Direction only) (D)
    Show Vector Length (L)
    Show Flow Field Marker (M)
    Show Flow Field (F)
    Show Fish Origin (O)
    Speed (Arrows)
	Show Parents (P)
	Show Children (C)
	Checkbox mode for hotkeys?
    Show ALL (A)
    Remove ALL (R)
    */

    public void draw()
    {

        if(flowField)
            drawFlowField();

        if(hitbox)
            drawHitboxes();

        if(origin)
            drawOrigin();
    }

    public void toggleHitbox()
    {
        hitbox = !hitbox;
    }

    public void toggleOrigin()
    {
        origin = !origin;
    }

    public void toggleFlowField()
    {
        flowField = !flowField;
    }

    public void showAll()
    {
        hitbox = true;
        origin = true;
        flowField = true;
    }

    public void removeAll()
    {
        hitbox = false;
        origin = false;
        flowField = false;
    }


    private void drawHitboxes()
    {
        for(Fish fish : fishList)
        {
            batch.draw(hitboxTexture, fish.getX(), fish.getY());
        }
    }

    private void drawOrigin()
    {
        for(Fish fish : fishList)
        {
            batch.draw(originTexture, fish.getOriginX() + fish.getX() - originMarkerSize/2, fish.getOriginY() + fish.getY() - originMarkerSize/2);
        }
    }

    private void drawFlowField()
    {
        batch.end();
        shapeRenderer.begin();
        for(int i = 0; i < field.getWidth(); i++)
        {
            shapeRenderer.line(i*field.getTileWidth(), Gdx.graphics.getHeight(), i*field.getTileWidth(), 0, Color.BLACK, Color.BLACK);
        }

        for(int i = 0; i < field.getHeight(); i++)
        {
            shapeRenderer.line(0, i*field.getTileHeight(), Gdx.graphics.getWidth(), i*field.getTileHeight(), Color.BLACK, Color.BLACK);
        }
        shapeRenderer.end();
        batch.begin();
    }

    private void createTextures()
    {
        hitboxPxMap2D = new Gdx2DPixmap(game.getFoidWidth(), game.getFoidHeight(), Gdx2DPixmap.GDX2D_FORMAT_RGBA8888 );

        for(int i = 0; i < game.getFoidWidth() * game.getFoidHeight(); i++)
        {
            hitboxPxMap2D.setPixel(i%game.getFoidWidth(), (i/game.getFoidWidth()), Color.rgba8888(1f, 144/255f, 0, 0.65f));
        }

        hitboxTexture = new Texture(new Pixmap(hitboxPxMap2D));


        originPxMap2D = new Gdx2DPixmap(originMarkerSize, originMarkerSize, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
        for(int i = 0; i < originMarkerSize * originMarkerSize; i++)
        {
            originPxMap2D.setPixel(i%originMarkerSize, (i/originMarkerSize), Color.rgba8888(1f, 0, 0, 1f));
        }

        originTexture = new Texture(new Pixmap(originPxMap2D));
    }

    private void createShapes()
    {

    }
}