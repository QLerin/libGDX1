package com.gdx.invader.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.gdx.invader.main.Game.isItMoving;
import static com.gdx.invader.states.Play.gameOn;


/**
 * Created by Apelsinas Jr on 2014.10.26.
 */
public class Animation {

    private TextureRegion[] frames;
    private float time;
    private float delay;
    private int currentFrame;
    private int timesPlayed;

    public Animation() {}

    public Animation(TextureRegion[] frames) {
        this(frames, 1 / 12f);
    }

    public Animation(TextureRegion[] frames, float delay) {
        setFrames(frames, delay);
    }

    public void setFrames(TextureRegion[] frames, float delay) {
        this.frames = frames;
        this.delay = delay;
        time = 0;
        currentFrame = 0;
        timesPlayed = 0;
    }


    public void update(float dt) {
        if(delay <= 0) return;
        time += dt;
        while(time >= delay) {
            step();
        }
    }

    public void updatePlayer(float dt) {
        if(delay <= 0) return;
        time += dt;
        while(time >= delay) {
            stepForPlayer();
        }
    }

    private void step() {
        time -= delay;
        currentFrame++;
        if(currentFrame == frames.length) {
            currentFrame = 0;
            timesPlayed++;
        }
    }

    private void stepForPlayer() {
        time -= delay;
        currentFrame++;
        if(currentFrame == frames.length - 1 || !isItMoving) {
            if(gameOn) {
                currentFrame = 0;
                timesPlayed++;
            }
            else {
                currentFrame = 4;
                timesPlayed++;

            }
        }
    }

    public TextureRegion getFrame() { return frames[currentFrame]; }
    public int getTimesPlayed() { return timesPlayed; }

}
