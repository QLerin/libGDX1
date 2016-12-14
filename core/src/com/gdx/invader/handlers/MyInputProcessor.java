package com.gdx.invader.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Apelsinas Jr on 2014.10.26.
 */
public class MyInputProcessor extends InputAdapter {

    public boolean keyDown(int k) {
        if(k == Input.Keys.LEFT) {
            MyInput.setKey(MyInput.BUTTON1, true);
        }
        if(k == Input.Keys.RIGHT) {
            MyInput.setKey(MyInput.BUTTON2, true);
        }
        if(k == Input.Keys.UP) {
            MyInput.setKey(MyInput.BUTTON3, true);
        }
        if(k == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.BUTTON4, true);
        }
        return true;
    }

    public boolean keyUp(int k) {
        if(k == Input.Keys.LEFT) {
            MyInput.setKey(MyInput.BUTTON1, false);
        }
        if(k == Input.Keys.RIGHT) {
            MyInput.setKey(MyInput.BUTTON2, false);
        }
        if(k == Input.Keys.UP) {
            MyInput.setKey(MyInput.BUTTON3, false);
        }
        if(k == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.BUTTON4, false);
        }
        return true;
    }

}