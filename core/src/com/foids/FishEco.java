package com.foids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.Queue;
import com.foids.commands.CommandManager;
import com.foids.commands.InputManager;
import com.foids.commands.DeathInfo;
import com.foids.life.Egg;
import com.foids.life.Fish;
import com.foids.life.Food;

import java.io.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Main game class
 * Created by Cedric on 2016-09-21.
 */
public class FishEco extends ApplicationAdapter {

	private final int START_FISH_COUNT = 25;
	private final int START_FOOD_COUNT = 20;

	private SpriteBatch batch;
	private OrthographicCamera cam;

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
	private byte updateCounter;

	//TODO LIST
	//FIX ORIGIN -> Use Sprite instead of SpriteBatch
	//OPTIMIZE!!
	//Groups

	//PLANNED FEATURES
	//Special Mutations : Specially Mutated fish will have a slightly different appeareance
	//Special Mutations include : Egg-Eating, Shark-Friendly
	//Other mutations affect speed, sight and digestion rate.


	@Override
	public void create () {
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

		//This is used to change the flow field, currently obsolete
		updateCounter = 0;

		spawnFish();
		spawnFood();

		eggList = new LinkedList<>();
        deathList = new Queue<>(100);

		commandManager = new CommandManager(this);

		inputManager = new InputManager(commandManager, this);
		Gdx.input.setInputProcessor(inputManager);

        PrintWriter clearWriter = null;
        try {
            clearWriter = new PrintWriter("N:/Data/fishTree.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        clearWriter.print("");
        clearWriter.close();


        Thread fileWriter = new Thread(() ->
        {
            BufferedWriter writer = null;


            while(true)
            {
                try {
                    Thread.sleep(10000);
                    writer = new BufferedWriter(new FileWriter("N:/Data/fishTree.txt", true));
                    while(deathList.size != 0)
                    {
                        writer.write(deathList.removeFirst().toString());
                        writer.newLine();
                    }
                    writer.close();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileWriter.start();
	}

	@Override
	public void render () {

		update();

		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);

		batch.begin();
		//Drawing background
		batch.draw(background, 0, 0);

		for(Food food : foodList)
			food.draw();

		for(Egg egg : eggList)
			egg.draw();

		for(Fish fish : fishList)
			fish.draw();

		commandManager.draw();
		batch.end();
	}

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
				fishList.add(new Fish(egg.getX(), egg.getY(), foidWidth, foidHeight, this, fishTexture, egg.getParent()));
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
			fishList.add(new Fish(randomizer.nextInt(1280), randomizer.nextInt(720), foidWidth, foidHeight, this,fishTexture));
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
