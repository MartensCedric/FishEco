package com.foids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Queue;
import com.foids.util.FlowField;
import com.foids.util.OpenSimplexNoise;
import com.foids.commands.CommandManager;
import com.foids.commands.InputManager;
import com.foids.commands.DeathInfo;
import com.foids.life.Egg;
import com.foids.life.Fish;
import com.foids.life.Food;

import java.util.LinkedList;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE0;

/**
 * Main game class
 * Created by Cedric on 2016-09-21.
 *
 * Update 2018-03-11
 * To anyone wanting to read the code of this program... think again!
 * The code is absolutely disgusting.
 * You have been warned!
 */
public class FishEco extends ApplicationAdapter {

	private final int START_FISH_COUNT = 25;
	private final int START_FOOD_COUNT = 20;

	private SpriteBatch batch;
	private OrthographicCamera cam;

	private float timef = 0f;

	private LinkedList<Fish> fishList;
	private LinkedList<Food> foodList;
	private LinkedList<Egg> eggList;
	private Queue<DeathInfo> deathList;

	private Texture background;
	private byte[] fishTexture;

	private final int TRANS = 0;
	private final int BLACK = 1;
	private final int COLOR = 2;

	private CommandManager commandManager;
	private InputManager inputManager;

	private int foidWidth;
	private int foidHeight;

	private FlowField field;
	private long numberOfFrames;

	private AssetManager assetManager;
	private ShaderProgram waterRefractionShader;

	@Override
	public void create ()
	{
		assetManager = new AssetManager();
		assetManager.load("distortion.jpg", Texture.class);
		assetManager.finishLoading();

		ShaderProgram.pedantic = false;

		String defaultVertex = Gdx.files.internal("shaders/default.vs.glsl").readString();
		String waterRefraction = Gdx.files.internal("shaders/water.fs.glsl").readString();
		waterRefractionShader = new ShaderProgram(defaultVertex, waterRefraction);

		if (!waterRefractionShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + waterRefractionShader.getLog());

		numberOfFrames = 0;
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		cam.zoom = 1.0f;
		cam.update();

		setTextures();
		batch = new SpriteBatch();

		//We need to load this to make our own textures with the Gdx2DPixmap
		GdxNativesLoader.load();

		//We create the water currents
		field = new FlowField();

		//Set the dimensions of the fish
		foidWidth = 5;
		foidHeight = 9;

		spawnFish();
		spawnFood();

		eggList = new LinkedList<>();
        deathList = new Queue<>(100);

		commandManager = new CommandManager(this);

		inputManager = new InputManager(commandManager, this);
		Gdx.input.setInputProcessor(inputManager);
	}

	@Override
	public void render () {

		update();

		timef += Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		waterRefractionShader.begin();
		waterRefractionShader.setUniformf("timef", timef);
		waterRefractionShader.setUniformi("u_refraction", 1);
		waterRefractionShader.setUniformf("world_x", 0);
		waterRefractionShader.setUniformf("world_y", 0);
		waterRefractionShader.setUniformf("screen_width", Gdx.graphics.getWidth());
		waterRefractionShader.setUniformf("screen_height", Gdx.graphics.getHeight());
		waterRefractionShader.end();

		assetManager.get("distortion.jpg", Texture.class).bind(1);

		batch.setShader(waterRefractionShader);

		batch.draw(background, 0, 0);
		Gdx.gl.glActiveTexture(GL_TEXTURE0);
		batch.setShader(null);

		for(Food food : foodList)
		{
			food.draw();
		}

		for(Egg egg : eggList)
		{
			egg.draw();
		}


		for(Fish fish : fishList)
		{
			fish.draw();
		}

		commandManager.draw();
		batch.flush();
		batch.end();
	}

	/*
	private void setShaderWorldCoords(float x, float y)
	{
		waterRefractionShader.begin();
		waterRefractionShader.setUniformf("timef", timef);
		waterRefractionShader.setUniformi("u_refraction", 1);
		waterRefractionShader.setUniformf("world_x", x);
		waterRefractionShader.setUniformf("world_y", y);
		waterRefractionShader.setUniformf("screen_width", Gdx.graphics.getWidth());
		waterRefractionShader.setUniformf("screen_height", Gdx.graphics.getHeight());
		waterRefractionShader.end();

		assetManager.get("distortion.jpg", Texture.class).bind(1);
	}
*/
	@Override
	public void dispose () {
		batch.dispose();
	}

	private void update()
	{

		numberOfFrames++;

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			inputManager.moveCamera(-inputManager.MOVE_SPEED, 0);

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			inputManager.moveCamera(inputManager.MOVE_SPEED, 0);

		if(Gdx.input.isKeyPressed(Input.Keys.UP))
			inputManager.moveCamera(0, inputManager.MOVE_SPEED);

		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
			inputManager.moveCamera(0, -inputManager.MOVE_SPEED);

		for(int i = 0; i < fishList.size(); i++)
		{
			Fish fish = fishList.get(i);
			fish.update();

			if(fish.isDead())
			{
				deathList.addLast(new DeathInfo(fish.getBirthTime(), numberOfFrames,
						fish.getId(), fish.getParentID(), fish.getSight(), fish.getMaxSpeed(),
						fish.getFoodAte()));
				fishList.remove(i);
				i--;
			}
		}

		for(int i = 0; i < eggList.size(); i++)
		{
			Egg egg = eggList.get(i);
			egg.update();

			if(egg.isHatched())
			{
				fishList.add(new Fish(egg.getX(), egg.getY(), foidWidth, foidHeight, fishTexture, egg.getParent(), this));
				eggList.remove(i);
				i--;
			}

		}

		cam.update();
	}


	private void setTextures()
	{
		//Texture for the fishes
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

		//Here we create a background with Simplex Noise (Perlin Noise upgrade from 2001)
		Gdx2DPixmap pxBg = new Gdx2DPixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);

		OpenSimplexNoise noise = new OpenSimplexNoise();
		System.out.println("Background seed : " + noise.getSeed());
		float offsetI = 0;
		float offsetJ = 0;
		for(int i = 0; i < pxBg.getWidth(); i++)
		{
			offsetI+= 0.2;
			for(int j = 0; j < pxBg.getHeight(); j++)
			{
				offsetJ+=0.2;

				int blue = (int)(noise.eval(offsetI,offsetJ)*110 + 87)/2;
				pxBg.setPixel(i,j, Color.rgba8888(30f/255f,80f/255f,((float)blue+155f)/255f,1f));
			}
		}
		background = new Texture(new Pixmap(pxBg));
	}

	/**
	 * Spawn the initial fish
	 */
	private void spawnFish()
	{
		fishList = new LinkedList<>();

		Random randomizer = new Random();
		for (int i = 0; i < START_FISH_COUNT; i++)
		{
			fishList.add(new Fish(randomizer.nextInt(1280), randomizer.nextInt(720), foidWidth, foidHeight, fishTexture, this));
		}
	}

	/**
	 * Spawn the initial food
	 */
	private void spawnFood()
	{
		foodList = new LinkedList<>();
		for(int i = 0; i < START_FOOD_COUNT; i++)
		{
			  foodList.add(new Food(this));
		}
	}

	/**
	 * Gets the list of all the fish
	 * @return all the alive fish
	 */
    public LinkedList<Fish> getFishList() {
        return fishList;
    }

	/**
	 * Gets the list of all the food
	 * @return all the non eaten food.
	 */
	public LinkedList<Food> getFoodList() {return  foodList; }

    public SpriteBatch getBatch() {
        return batch;
    }

	public int getFoidWidth() {
		return foidWidth;
	}

	public int getFoidHeight() {
		return foidHeight;
	}

	public FlowField getField() {
		return field;
	}

	public LinkedList<Egg> getEggList() {
		return eggList;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public long getNumberOfFrames() {
		return numberOfFrames;
	}
}
