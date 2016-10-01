package com.foids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.LinkedList;

/**
 * Created by 1544256 on 2016-09-30.
 */
public class CommandManager {


    private FoidsGame game;

    private LinkedList<Foid> foidList;
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

    public CommandManager(FoidsGame game)
    {
        this.game = game;

        this.foidList = game.getFoidList();
        this.batch = game.getBatch();
        this.shapeRenderer = new ShapeRenderer();


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
    Show Pointing Position for Flow (Q)
    Show Flow Field (F)
    Show Foid Origin (O)
    Speed (Arrows)
	Show Parents (P)
	Show Children (C)
	Checkbox mode for hotkeys?
    Show ALL (A)
    Remove ALL (R)
    */

    public void draw()
    {
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

    public void showAll()
    {
        hitbox = true;
        origin = true;
    }

    public void removeAll()
    {
        hitbox = false;
        origin = false;
    }


    private void drawHitboxes()
    {
        for(Foid foid : foidList)
        {
            batch.draw(hitboxTexture, foid.getX(), foid.getY());
        }
    }

    private void drawOrigin()
    {
        for(Foid foid : foidList)
        {
            batch.draw(originTexture, foid.getOriginX() + foid.getX() - originMarkerSize/2, foid.getOriginY() + foid.getY() - originMarkerSize/2);
        }
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
