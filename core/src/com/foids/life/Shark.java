package com.foids.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.foids.FishEco;

import java.util.Random;

/**
 * Sharks are the predators in FishEco, they do not eat Food like Fish do, instead they eat Fish.
 * Created by Cedric Martens on 2016-10-08.
 */
public class Shark extends Creature
{

    private Fish foodTarget;

    private Random randomizer;

    public Shark(FishEco game) {

        this.randomizer = new Random();

        setLocation(new Vector2(randomizer.nextInt(Gdx.graphics.getWidth()), randomizer.nextInt(Gdx.graphics.getHeight())));

        setMass(2.5f);
        setGame(game);

        foodTarget = null;
        setFoodAte(0);

        createTexture();

        setWidth(getTextureRegion().getRegionWidth());
        setHeight(getTextureRegion().getRegionHeight());

        setForce(new Vector2(0,0));
        setVelocity(new Vector2(0,0));
        setDesired(new Vector2(0,0));

        setMaxSpeed(1f);
    }

    private void createTexture() {
        setTexture(new Texture("core/assets/shark.png"));
        setTextureRegion(new TextureRegion(getTexture()));
    }

    private void applyForce()
    {
        getVelocity().scl(0);
        getForce().scl(0);

        getForce().add(vectorFromField());
        getForce().scl(getMaxSpeed()/getMass()); //THIS IS NO GOOD NEED CHANGE
        getVelocity().add(getForce());
        getVelocity().add(getDesired());
        setDir(getDirectionDegrees(getVelocity()));

        getLocation().add(getVelocity());

        if(getOriginY() > Gdx.graphics.getHeight() - 1)
            setOriginY(1);

        if(getOriginY() < 1)
            setOriginY(Gdx.graphics.getHeight() - 1);

        if(getOriginX() > Gdx.graphics.getWidth() - 1)
            setOriginX(1);

        if(getOriginX() < 1)
            setOriginX(Gdx.graphics.getWidth() - 1);
    }

    @Override
    public void update()
    {
        //11,5;11,6;12,5;12,6 where the shark eats
        applyForce();
        /*getDesired().x = 0.25f;
        getDesired().y = 0f;

        //If there's no food target
        if(getFoodTarget() == null)
        {
            for(Fish fish : getGame().getFishList())
            {
                //And theres a food close, assign it as a new target
                if(Math.sqrt(Math.pow(getOriginX() - fish.getLocation().x, 2) + Math.pow(getOriginY() - fish.getLocation().y, 2)) <= 30)
                {
                    setFoodTarget(fish);
                    break;
                }
            }
            //if the current food target is too far
        }else if(Math.sqrt(Math.pow(getOriginX() - getFoodTarget().getOriginX(), 2) + Math.pow(getOriginY() - foodTarget.getOriginY(), 2)) > getSight())
        {
            foodTarget = null;
            //if the current food target is close enough to it
        }else if(foodTarget.contains(getOriginVector()))
        {

            ateFood(foodTarget.getQuantity());

            if(getBelly() > 1f)
                setBelly(1f);

            getGame().getFoodList().remove(foodTarget);

            if(getGame().getFoodList().size() == 19)
                getGame().getFoodList().add(new Food(getGame()));

            createFishTexture();
            foodTarget = null;
        }else{
            //Make sure the food still exist (If another fish hasn't ate it yet)
            boolean stopChasing = true;
            for(Food food : getGame().getFoodList())
            {
                if(food == foodTarget)
                {
                    stopChasing = false;
                    break;
                }
            }

            if(stopChasing)
            {
                foodTarget = null;
            }
        }
    }

    public Fish getFoodTarget() {
        return foodTarget;
    }

    public void setFoodTarget(Fish foodTarget) {
        this.foodTarget = foodTarget;*/
    }
}