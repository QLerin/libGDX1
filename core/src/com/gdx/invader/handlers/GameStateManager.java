package com.gdx.invader.handlers;

import com.gdx.invader.main.Game;
import com.gdx.invader.states.GameState;
import com.gdx.invader.states.Play;

import java.util.Stack;

/**
 * Created by Apelsinas Jr on 2014.10.26.
 */
public class GameStateManager {

    private Game game;

    private Stack<GameState> gameStates;

    public static final int PLAY = 912837;

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public Game game() { return game; }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    private GameState getState(int state) {
        if(state == PLAY) return new Play(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

}
