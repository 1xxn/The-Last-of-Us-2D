package com.corex.thelastofus2d.Scenes;

import Sprites.Ellie;
import Sprites.Joel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label joelLabel;
    private Label ellieLabel;
    private Label fpsLabel;
    private String character;

    public Hud(SpriteBatch spriteBatch, String character) {
        this.character = character;

        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(TheLastOfUs2D.V_WIDTH, TheLastOfUs2D.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%6d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("01", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("Level", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        joelLabel = new Label("Joel", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        ellieLabel = new Label("Ellie", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        fpsLabel = new Label("FPS:" + Gdx.graphics.getFramesPerSecond(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));



        if (character.equals("Joel")) {
            table.add(joelLabel).expandX().padTop(10);
        } else if (character.equals("Ellie")) {
            table.add(ellieLabel).expandX().padTop(10);
        }
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(fpsLabel).expandX();

        stage.addActor(table);
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
