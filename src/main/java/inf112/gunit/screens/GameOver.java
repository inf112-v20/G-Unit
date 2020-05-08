package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.gunit.main.Main;
import inf112.gunit.player.Robot;

public class GameOver extends RRScreen {

    private final SpriteBatch batch;
    protected Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final GlyphLayout layoutWinner;
    private final GlyphLayout layoutLoser;
    private final Robot robot;
    private final Main main;

    /**
     * The Menu construcor
     * @param main takes a main (which is a game)
     */
    public GameOver(final Main main, Robot robot) {
        this.main = main;
        this.robot = robot;
        String winnerString = "Game Over! The " + robot.toString() + " robot won!";
        String loserString = "Game Over! You Lost!";

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(width, height, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // This is for the Game Over String
        stage = new Stage(viewport, batch);
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        layoutWinner = new GlyphLayout();
        layoutWinner.setText(font, winnerString);
        layoutLoser = new GlyphLayout();
        layoutLoser.setText(font, loserString);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.getLabel().setFontScale(3);
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(3);

        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new Menu(main));
                dispose();
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        int padding = 50;
        //Add buttons to table
        mainTable.row();
        mainTable.add(mainMenuButton).width(400).height(100).pad(padding);
        mainTable.row();
        mainTable.add(exitButton).width(400).height(100).pad(padding);
        mainTable.row();

        mainTable.center();

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();
        stage.act(delta);
        stage.draw();

        // Draw Game Over String
        batch.begin();
        // Display different messages whether the player loses, or if anyone wins.
        if (robot.isDead())
            font.draw(batch, layoutLoser, (camera.viewportWidth - layoutLoser.width) / 2, 875);
        else
            font.draw(batch, layoutWinner, (camera.viewportWidth - layoutWinner.width) / 2, 875);
        batch.end();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        skin.dispose();
        atlas.dispose();
        font.dispose();
        stage.dispose();
        batch.dispose();
    }
}
