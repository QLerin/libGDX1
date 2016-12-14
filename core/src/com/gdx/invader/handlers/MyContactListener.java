package com.gdx.invader.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by Apelsinas Jr on 2014.10.26.
 */
public class MyContactListener implements ContactListener {

    private int numFootContacts;
    private float livesLost = 0;
    private Array<Body> bodiesToRemove;
    private Array<Body> cannonballsToRemove;
    private ArrayList<Body> bulletsToRemove;

    public MyContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
        bulletsToRemove = new ArrayList<Body>();
        cannonballsToRemove = new Array<Body>();
    }

    public void beginContact(Contact c) {

        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("crystal")) {
            bodiesToRemove.add(fa.getBody());
        }
        if(fb.getUserData() != null && fb.getUserData().equals("crystal")) {
            bodiesToRemove.add(fb.getBody());
        }

        if(fa.getUserData() != null && fa.getUserData().equals("bullet")) {
            bulletsToRemove.add(fa.getBody());
            //System.out.println("hi dere");
        }
        if(fb.getUserData() != null && fb.getUserData().equals("bullet")) {
            bulletsToRemove.add(fb.getBody());
            //System.out.println("hi dere");
        }

        if(fa.getUserData() != null && fa.getUserData().equals("cannonball")) {
            cannonballsToRemove.add(fa.getBody());
            if (fb.getUserData() != null && fb.getUserData().equals("player")){
                livesLost++;
                System.out.println("Player got hit by cannonball");
            }


        }
        if(fb.getUserData() != null && fb.getUserData().equals("cannonball")) {
            cannonballsToRemove.add(fb.getBody());
            if (fa.getUserData() != null && fa.getUserData().equals("player")){
                livesLost++;
                System.out.println("Player got hit by cannonball");
            }

        }

    }

    public void endContact(Contact c) {

        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

    }

    public boolean isPlayerOnGround() { return numFootContacts > 0; }
    public float getLivesLost() { return livesLost; }
    public Array<Body> getBodiesToRemove() { return bodiesToRemove; }
    public ArrayList<Body> getBulletsToRemove() { return bulletsToRemove; }
    public Array<Body> getCannonballsToRemove() { return cannonballsToRemove; }


    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}