package com.gdx.invader.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.invader.main.Game;

/**
 * Created by Apelsinas Jr on 2014.10.26.
 */
public class Crystal extends B2DSprite {

    public Crystal(Body body) {

        super(body);


        Texture tex = Game.res.getTexture("crystal");
        TextureRegion[] sprites = TextureRegion.split(tex, 16, 16)[0];
        setAnimation(sprites, 1 / 12f);

    }

}
