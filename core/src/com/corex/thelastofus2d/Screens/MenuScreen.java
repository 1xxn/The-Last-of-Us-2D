package com.corex.thelastofus2d.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class MenuScreen implements Screen {
    private TheLastOfUs2D game;
    private OrthographicCamera camera;
    private Music mainMusic;

    public MenuScreen(TheLastOfUs2D game) {
        // assigns the game we passed to the placeholder
        this.game = game;
        // creates the camera
        camera = new OrthographicCamera();
        // sets the screen to be 800 width by 480 height
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // load main music theme
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/maintheme.ogg"));
        mainMusic.setLooping(true);
        mainMusic.play();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && mainMusic.isPlaying()) {
            mainMusic.stop();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // draw "Play Joel" button
        Texture joelButton = new Texture("joelButton.png");
        game.batch.draw(joelButton, 200, 300);

        // draw "Play Ellie" button
        Texture ellieButton = new Texture("ellieButton.png");
        game.batch.draw(ellieButton, 200, 150);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // check if "Play Joel" button is pressed
            if (touchPos.x > 200 && touchPos.x < 200 + joelButton.getWidth() && touchPos.y > 300 && touchPos.y < 300 + joelButton.getHeight()) {
                game.setScreen(new PlayScreen(game, "Joel"));
                System.out.println("Button 1");
                dispose();
            }
            // check if "Play Ellie" button is pressed
            else if (touchPos.x > 200 && touchPos.x < 200 + ellieButton.getWidth() && touchPos.y > 150 && touchPos.y < 150 + ellieButton.getHeight()) {
                game.setScreen(new PlayScreen(game, "Ellie"));
                System.out.println("Button 2");
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

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
