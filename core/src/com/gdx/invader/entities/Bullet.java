package com.gdx.invader.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.invader.main.Game;

/**
 * Created by Apelsinas Jr on 2014.10.28.
 */
public class Bullet extends B2DSprite {

    public Bullet(Body body) {

        super(body);

        Texture tex = Game.res.getTexture("bullet");
        TextureRegion[] sprites = TextureRegion.split(tex, 2, 2)[0];

        setAnimation(sprites, 1 / 1f);

    }
 }
