package com.corex.thelastofus2d.Screens;

import Sprites.Ellie;
import Sprites.Joel;
import Utils.B2WorldCreator;
import Utils.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.corex.thelastofus2d.Scenes.Hud;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class PlayScreen implements Screen {
    // reference to our game, used to set screens
    private TheLastOfUs2D game;
    private TextureAtlas atlas;
    private Music mainMusic;

    // basic playscreen variables
    private OrthographicCamera gameCamera;
    private Viewport gamePort;
    private Hud hud;

    // tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Ellie player;
    private Joel player2;
    private Box2DDebugRenderer b2dr;
    private String character;

    public PlayScreen(TheLastOfUs2D game, String character) {
        if (character.equals("Joel")) {
            atlas = new TextureAtlas("Joel.atlas");
        } else if (character.equals("Ellie")) {
            atlas = new TextureAtlas("Ellie.atlas");
        }

        this.game = game;
        // create camera used to follow joel through camera world
        gameCamera = new OrthographicCamera();

        // create a FitViewPort to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(TheLastOfUs2D.V_WIDTH / TheLastOfUs2D.PPM, TheLastOfUs2D.V_HEIGHT / TheLastOfUs2D.PPM, gameCamera);

        // create out game HUD for scores/timers etc.
        hud = new Hud(game.batch, character);

        // load our map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level01.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / TheLastOfUs2D.PPM);

        this.character = character;

        // initially set camera to be centered correctly at start of the map
        gameCamera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // create our Box2D world, setting no gravity in x, -10 gravity in y
        world = new World(new Vector2(0, -10), true);
        // allows for debug lines of our Box2D world
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);


        // create joel in game world
        if (character.equals("Joel")) {
            player2 = new Joel(world, this);
        } else if (character.equals("Ellie")) {
            player = new Ellie(world, this);
        }

        world.setContactListener(new WorldContactListener());
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        // control our player using immediate impulses
        if (character.equals("Joel")) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player2.b2body.applyLinearImpulse(new Vector2(0, 4f), player2.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player2.b2body.getLinearVelocity().x <= 2)
                player2.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player2.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player2.b2body.getLinearVelocity().x >= -2)
                player2.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player2.b2body.getWorldCenter(), true);
        } else if (character.equals("Ellie")) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            System.out.println("character selection");
        }
    }

    public void update(float dt) {
        // handle user input first
        handleInput(dt);

        world.step(1/60f, 6, 2);

        if (character.equals("Joel")) {
            player2.update(dt);
            gameCamera.position.x = player2.b2body.getPosition().x;
        } else if (character.equals("Ellie")) {
            player.update(dt);
            gameCamera.position.x = player.b2body.getPosition().x;
        }
        hud.update(dt);


        // update camera with correct coordinates after changes
        gameCamera.update();
        // tell renderer to draw only what camera can see in game world
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {

        // separate our update logic from render
        update(delta);

        // clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render our game map
        renderer.render();

        // renderer our Box2DDebugLines
        b2dr.render(world, gameCamera.combined);

        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        if (character.equals("Joel")) {
            player2.draw(game.batch);
        } else if (character.equals("Ellie")) {
            player.draw(game.batch);
        }
        game.batch.end();

        // set our batch to now draw what the hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
