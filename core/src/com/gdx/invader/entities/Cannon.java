package com.gdx.invader.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.invader.main.Game;

/**
 * Created by Apelsinas Jr on 2014.10.30.
 */
public class Cannon extends B2DSprite implements Comparable<Cannon> {

    public Cannon(Body body) {

        super(body);


        Texture tex = Game.res.getTexture("cannon");
        TextureRegion[] sprites = TextureRegion.split(tex, 40, 40)[0];
        setAnimation(sprites, 1 / 12f);

    }

    @Override
    public int compareTo(Cannon o) {
        return 0;
    }
}