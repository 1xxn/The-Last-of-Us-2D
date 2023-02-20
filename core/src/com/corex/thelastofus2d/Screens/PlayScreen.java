package com.corex.thelastofus2d.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.*;
import com.corex.thelastofus2d.Scenes.Hud;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class PlayScreen implements Screen {
    private TheLastOfUs2D game;
    private OrthographicCamera gameCamera;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public PlayScreen(TheLastOfUs2D game) {
        this.game = game;
        // create camera used to follow joel through camera world
        gameCamera = new OrthographicCamera();

        // create a FitViewPort to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(TheLastOfUs2D.V_WIDTH, TheLastOfUs2D.V_HEIGHT, gameCamera);

        // create out game HUD for scores/timers etc.
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("testLevel1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gameCamera.position.set(TheLastOfUs2D.V_WIDTH / 2, TheLastOfUs2D.V_HEIGHT / 2, 0);
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            gameCamera.position.x += 100 * dt;
        }
    }

    public void update(float dt) {
        handleInput(dt);

        gameCamera.update();
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        // clear the game screen with black
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

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

    }
}
