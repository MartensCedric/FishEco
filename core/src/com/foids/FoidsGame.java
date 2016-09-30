package com.foids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.GdxNativesLoader;

import java.util.LinkedList;
import java.util.Random;

public class FoidsGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private LinkedList<Foid> foidList;
	private final int START_FOID_COUNT = 50;

	private byte[] fishTexture;

	private final int TRANS = 0;
	private final int BLACK = 1;
	private final int COLOR = 2;

	private CommandManager commandManager;
	private InputManager inputManager;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		GdxNativesLoader.load();

		commandManager = new CommandManager(this);
		inputManager = new InputManager(commandManager);

		setTextures();
		spawnFish();
	}

	@Override
	public void render () {

		update();

		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for(Foid foid:foidList)
			batch.draw(foid.getTextureRegion(), foid.getX(), foid.getY(),foid.getOriginX(),foid.getOriginY(),foid.getTexture().getWidth(), foid.getTexture().getHeight(), 1,1, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	private void update()
	{
		for(Foid foid : foidList)
			foid.update();
	}

	private void setTextures()
	{
		fishTexture = new byte[]{
				TRANS,BLACK,BLACK,BLACK,TRANS,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				BLACK,COLOR,COLOR,COLOR,BLACK,
				TRANS,BLACK,COLOR,BLACK,TRANS,
				TRANS,TRANS,COLOR,TRANS,TRANS,
				TRANS,BLACK,BLACK,BLACK,TRANS
		};
	}

	private void spawnFish()
	{
		foidList = new LinkedList<Foid>();

		for (int i = 0; i < START_FOID_COUNT; i++)
		{
			Random randomizer = new Random();
			foidList.add(new Foid(randomizer.nextInt(1280), randomizer.nextInt(720), fishTexture));
		}
	}

    public LinkedList<Foid> getFoidList() {
        return foidList;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
