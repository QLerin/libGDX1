package com.gdx.invader.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.invader.main.Game;


/**
 * Created by Apelsinas Jr on 2014.10.26.
 */
public class Player extends B2DSprite {

    private float lives = 5;
    private int numCrystals;
    private int totalCrystals;
    private boolean facingSide = true;
    private boolean alive = true;

    public Player(Body body) {

        super(body);

        Texture tex = Game.res.getTexture("viking");
        TextureRegion[] sprites = TextureRegion.split(tex, 30, 30)[0];
        Texture tex2 = Game.res.getTexture("cannonbullet");
        TextureRegion[] sprites2 = TextureRegion.split(tex2, 9, 9)[0];

        if (alive) {
            setAnimation(sprites, 1 / 6f);
        }
        else {
            setAnimation(sprites2, 1 / 1f);
        }

    }

    public void collectCrystal() { numCrystals++; }
    public float getLives() {return lives; }
    public void playerDead() { alive = false; }
    public int getNumCrystals() { return numCrystals; }
    public void setTotalCrystals(int i) { totalCrystals = i; }
    public int getTotalCrystals() { return totalCrystals; }
    public boolean getFacingSide() { return facingSide; }
    public void setFacingSide( boolean side){
        facingSide = side;
    }

}
