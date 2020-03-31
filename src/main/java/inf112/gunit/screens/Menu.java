package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.gunit.main.Main;

/**
 * The Menu class is called from the Main class as
 * the first screen to be rendered by the program.
 */

public class Menu extends RRScreen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    private Main main;

    /**
     * The Menu construcor
     * @param main takes a main (which is a game)
     */
    public Menu(final Main main) {
        this.main = main;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(width, height, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        final TextButton testplayButton = new TextButton("TESTPlay", skin);
        testplayButton.getLabel().setFontScale(3);
        TextButton hostButton = new TextButton("Host", skin);
        hostButton.getLabel().setFontScale(3);
        TextButton joinButton = new TextButton("Join", skin);
        joinButton.getLabel().setFontScale(3);
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(3);

        // Listeners
        // TODO The skin is supposed to change when hovering not clicking
        testplayButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                testplayButton.setStyle(skin.get("toggle", TextButton.TextButtonStyle.class));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.enter(event, x, y, pointer, toActor);
                testplayButton.setStyle(skin.get("default", TextButton.TextButtonStyle.class));
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new Game(main, 4));
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // TODO Implement join screen
        joinButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // TODO Implement host screen
        hostButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        int padding = 50;
        //Add buttons to table
        mainTable.row();
        mainTable.add(testplayButton).width(400).height(100).pad(padding);
        mainTable.row();
        mainTable.add(joinButton).width(400).height(100).pad(padding);
        mainTable.row();
        mainTable.add(hostButton).width(400).height(100).pad(padding);
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
    }
}
