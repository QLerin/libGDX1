package com.gdx.invader.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.gdx.invader.DoublyLinkedList.DLL;
import com.gdx.invader.entities.*;
import com.gdx.invader.handlers.*;
import com.gdx.invader.main.Game;

import java.util.ArrayList;

import static com.gdx.invader.handlers.B2DVars.PPM;
import static com.gdx.invader.main.Game.isItMoving;

/**
 * Created by Apelsinas Jr on 2014.10.26.
 */
public class Play extends GameState {

    public static boolean gameOn = true;
    public static boolean won = false;
    public static float lostLives;
    private boolean debug = false;

    private boolean bulletOn;
    public static final long FIRE_RATE = 200000000L;
    public static final long BULLET_FIRE_RATE = 200000000L;
    public long lastShot;

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;
    private MyContactListener cl;

    private TiledMap tileMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;
    private ArrayList <Bullet> bullets;
    private Array <Crystal> crystals;
    //private Array <Cannon> cannons;
    private DLL<Cannon> cannons;
    private Array <Cannonball> cannonballs;
    private Background[] backgrounds;

    private HUD hud;

    public Play(GameStateManager gsm) {

        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();
        bullets = new ArrayList<Bullet>();
        cannons = new DLL<Cannon>();
        cannonballs = new Array<Cannonball>();

        createPlayer();
        createTiles();
        createCrystals(player);
        createCannon();

        hud = new HUD(player);

        Texture bgs = Game.res.getTexture("bgs");
        TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
        TextureRegion clouds = new TextureRegion(bgs, 0, 240, 320, 240);
        TextureRegion mountains = new TextureRegion(bgs, 0, 480, 320, 240);
        backgrounds = new Background[3];
        backgrounds[0] = new Background(sky, cam, 0f);
        backgrounds[1] = new Background(clouds, cam, 0.1f);
        backgrounds[2] = new Background(mountains, cam, 0.2f);

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM * 2, Game.V_HEIGHT / PPM * 2);
    }


    public void handleInput() {
        if (gameOn) {
            // player jump
            if (MyInput.isPressed(MyInput.BUTTON1)) {
                player.getBody().setLinearVelocity(-2, player.getBody().getLinearVelocity().y);
                player.setFacingSide(false);
                isItMoving = true;
                if (MyInput.isPressed(MyInput.BUTTON3))
                    isItMoving = false;
            } else {
                if (!MyInput.isPressed(MyInput.BUTTON2)) {
                    player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
                    isItMoving = false;
                }
            }

            if (MyInput.isPressed(MyInput.BUTTON2)) {
                player.getBody().setLinearVelocity(2, player.getBody().getLinearVelocity().y);
                player.setFacingSide(true);
                isItMoving = true;
                if (MyInput.isPressed(MyInput.BUTTON3))
                    isItMoving = false;
            } else {
                if (!MyInput.isPressed(MyInput.BUTTON1)) {
                    player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
                    isItMoving = false;
                }
            }

            if (MyInput.isPressed(MyInput.BUTTON3)) {
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyForceToCenter(0, 80, true);
                    isItMoving = false;
                }
            }

            if (MyInput.isPressed(MyInput.BUTTON4)&& cl.isPlayerOnGround()) {

                if (System.nanoTime() - lastShot >= BULLET_FIRE_RATE*2) {
                    createBullet();
                    lastShot = System.nanoTime();
                }
                bulletOn = true;
            }
        }
        else {
            isItMoving = false;
        }
    }

    public void update(float dt) {
        handleInput();

        world.step(dt, 6, 2);


        Array<Body> bodies = cl.getBodiesToRemove();
        for(int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystal();
            System.gc();

        }

        bodies.clear();
        lostLives = cl.getLivesLost();
        if (player.getLives() <= lostLives)
        {
            gameOn = false;
            player.playerDead();
        }

        if(player.getBody().getPosition().y < 0/PPM){
            gameOn = false;
        }

        player.updatePlayer(dt);

        for(int i = 0; i < crystals.size; i++) {
            crystals.get(i).update(dt);
        }

        for(int i = 0; i < cannons.size(); i++) {
            cannons.get(i).update(dt);
        }
        if(System.nanoTime() - lastShot >= FIRE_RATE * 6) {
            for(int i = 0; i < cannons.size(); i++) {
                createCannonball(cannons.get(i).getBody().getPosition().x, cannons.get(i).getBody().getPosition().y);
            }
            lastShot = System.nanoTime();
        }

        for(int i = 0; i < cannonballs.size; i++) {
            cannonballs.get(i).update(dt);

        }

        if (bulletOn)
            for(int i = 0; i < bullets.size(); i++) {
                bullets.get(i).update(dt);
            }

        if(player.getBody().getPosition().x*PPM > 8550 && player.getBody().getPosition().x*PPM <8640){
            won = true;
        }
    }

    public void render() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.position.set(
                player.getPosition().x * PPM + Game.V_WIDTH / 4,
                Game.V_HEIGHT / 2,
                0
        );
        cam.update();

        sb.setProjectionMatrix(hudCam.combined);

        for(int i = 0; i < backgrounds.length; i++) {
            backgrounds[i].render(sb);
        }

        tmr.setView(cam);
        tmr.render();

        sb.setProjectionMatrix(cam.combined);
        player.render(sb);


        if (bulletOn)
            for(int i = 0; i < bullets.size(); i++) {
                bullets.get(i).render(sb);
            }

        for(int i = 0; i < crystals.size; i++) {
            crystals.get(i).render(sb);

        }

        for(int i = 0; i < cannons.size(); i++) {
            cannons.get(i).render(sb);
        }

        for(int i = 0; i < cannonballs.size; i++) {
            cannonballs.get(i).render(sb);
        }

        ArrayList<Body> bodiesBullets = cl.getBulletsToRemove();
        if (bodiesBullets.size() > 0) {
            for (int q = 0; q < bodiesBullets.size(); q++) {
                Body b = bodiesBullets.get(q);
                bullets.remove(b.getUserData());
                world.destroyBody(b);
            }
        }

        bodiesBullets.clear();

        Array<Body> bodiesCannonballs = cl.getCannonballsToRemove();
        if (bodiesCannonballs.size > 0) {
            for (int q = 0; q < bodiesCannonballs.size; q++) {
                Body b = bodiesCannonballs.get(q);
                cannonballs.removeValue((Cannonball) b.getUserData(), true);
                world.destroyBody(b);
            }
        }

        bodiesCannonballs.clear();

        sb.setProjectionMatrix(hudCam.combined);
        hud.Render(sb);

        if(debug) {
            b2dr.render(world, b2dCam.combined);
        }
    }


    public void dispose() {}

    private void createPlayer() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(100 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        shape.setAsBox(4 / PPM, 8 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_CRYSTAL |B2DVars.BIT_CANNON | B2DVars.BIT_CANNONBALL;
        body.createFixture(fdef).setUserData("player");

        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -8 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_CANNON;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        player = new Player(body);

        body.setUserData(player);
    }

    private void createTiles() {
        tileMap = new TmxMapLoader().load("res/maps/test.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);
        tileSize = (int) tileMap.getProperties().get("tilewidth", Integer.class);

        TiledMapTileLayer layer;

        layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");
        createLayer(layer, B2DVars.BIT_GROUND);
    }

    private void createLayer(TiledMapTileLayer layer, short bits) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(
                        (col + 0.5f) * tileSize / PPM,
                        (row + 0.5f) * tileSize / PPM
                );

                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[5];
                v[0] = new Vector2(
                        -tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(
                        tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[2] = new Vector2(
                        tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[3] = new Vector2(
                        -tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[4] = new Vector2(
                        -tileSize / 2 / PPM, -tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.friction = 0;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BULLET | B2DVars.BIT_CANNONBALL;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);
            }
        }

    }

    private void createCrystals(Player player) {

        crystals = new Array<Crystal>();

        MapLayer layer = tileMap.getLayers().get("crystals");

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for(MapObject mo : layer.getObjects()) {

            bdef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mo.getProperties().get("x", float.class) / PPM;
            float y = (float) mo.getProperties().get("y", float.class) / PPM;

            bdef.position.set(x, y);

            CircleShape cshape = new CircleShape();
            cshape.setRadius(8 / PPM);

            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("crystal");

            Crystal c = new Crystal(body);
            crystals.add(c);

            player.setTotalCrystals(crystals.size);

            body.setUserData(c);

        }

    }
    private void createBullet() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        if (player.getFacingSide()) {
            bdef.position.set((player.getBody().getPosition().x + 1 / PPM), (player.getBody().getPosition().y) + 5 / PPM);
        }
        else {
            bdef.position.set((player.getBody().getPosition().x - 1 / PPM), (player.getBody().getPosition().y) + 5 / PPM);
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        shape.setAsBox(2 / PPM, 2 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_BULLET;
        fdef.filter.maskBits = B2DVars.BIT_GROUND ;
        body.createFixture(fdef).setUserData("bullet");

        Bullet b = new Bullet(body);

        if (player.getFacingSide()) {
            b.getBody().setLinearVelocity(5, 0);
        }
        else{
            b.getBody().setLinearVelocity(-5, 0);
        }
        b.getBody().setGravityScale(0);

        bullets.add(b);
        body.setUserData(b);
    }

    private void createCannon() {

        //cannons = new Array<Cannon>();

        MapLayer layer = tileMap.getLayers().get("cannons");

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        for(MapObject mo : layer.getObjects()) {
            bdef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mo.getProperties().get("x", float.class) / PPM;
            float y = (float) mo.getProperties().get("y", float.class) / PPM;

            bdef.position.set(x, y);

            shape.setAsBox(11 / PPM, 6 / PPM);

            fdef.shape = shape;
            fdef.filter.categoryBits = B2DVars.BIT_CANNON;
            fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("cannon");

            Cannon b = new Cannon(body);
            cannons.add(b);

            body.setUserData(b);
        }
    }

    private void createCannonball(float x, float y) {


        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(x , y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        shape.setAsBox(4 / PPM, 4 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_CANNONBALL;
        fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER ; //| B2DVars.BIT_BULLET
        body.createFixture(fdef).setUserData("cannonball");

        Cannonball b = new Cannonball(body);
        b.getBody().setLinearVelocity(-4, 0);
        b.getBody().setGravityScale(0);

        cannonballs.add(b);
        body.setUserData(b);
    }
}