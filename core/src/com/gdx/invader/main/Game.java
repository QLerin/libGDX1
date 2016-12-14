package com.gdx.invader.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.invader.handlers.Content;
import com.gdx.invader.handlers.GameStateManager;
import com.gdx.invader.handlers.MyInputProcessor;

public class Game extends ApplicationAdapter {
    public static final String TITLE = "The Invader";
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;
    public static final float friction = 0f;
    public static boolean isItMoving = false;

    public static final float STEP = 1 / 60f;
    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;
    public static Content res;

	@Override
	public void create () {
        Gdx.input.setInputProcessor(new MyInputProcessor());

        res = new Content();
        res.loadTexture("res/images/cannonbullet.png", "cannonbullet");
        res.loadTexture("res/images/viking.png", "viking");
        res.loadTexture("res/images/crystal.png", "crystal");
        res.loadTexture("res/images/bgs.png","bgs" );
        res.loadTexture("res/images/bullet.png","bullet" );
        res.loadTexture("res/images/cannon.png", "cannon");
        res.loadTexture("res/images/hud.png", "hud");
        res.loadTexture("res/images/bg.png", "bg");
        res.loadTexture("res/images/gg.png", "gg");
        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
        }
	}
    public void dispose() {
    }

    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getCamera() { return cam; }
    public OrthographicCamera getHUDCamera() { return hudCam; }

    public void resize(int w, int h) {}
    public void pause() {}
    public void resume() {}
}
