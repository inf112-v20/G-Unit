package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.gunit.main.Main;

public class Menu implements Screen {

    //globals
    private static final int BUTTON_PLAY_WIDTH = 200;
    private static final int BUTTON_PLAY_HEIGHT = 80;
    private static final int BUTTON_PLAY_X = (Main.WIDTH / 2) - (BUTTON_PLAY_WIDTH/2);
    private static final int BUTTON_PLAY_Y = (Main.HEIGHT / 2) - (BUTTON_PLAY_HEIGHT/2);

    private Main game;

    private Texture buttonPlay;
    private Texture buttonPlayActive;

    public Menu(Main game) {
        this.game = game;
        this.buttonPlay = new Texture("src/assets/menu-items/button_play.png");
        this.buttonPlayActive = new Texture("src/assets/menu-items/button_play_active.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        game.batch.begin();

        //render active button if mouse over the button
        if (Gdx.input.getY() > BUTTON_PLAY_Y && Gdx.input.getY() < BUTTON_PLAY_Y + BUTTON_PLAY_HEIGHT && Gdx.input.getX() > BUTTON_PLAY_X && Gdx.input.getX() < BUTTON_PLAY_X + BUTTON_PLAY_WIDTH) {
            game.batch.draw(buttonPlayActive, BUTTON_PLAY_X, BUTTON_PLAY_Y, BUTTON_PLAY_WIDTH, BUTTON_PLAY_HEIGHT);

            //start game when button is pressed
            if (Gdx.input.isTouched()) {
                game.setScreen(new Game(game, new TmxMapLoader().load("src/assets/tile_map_3Kx3K_full.tmx")));
                this.dispose();
            }

        } else {
            //else render the normal button
            game.batch.draw(buttonPlay, BUTTON_PLAY_X, BUTTON_PLAY_Y, BUTTON_PLAY_WIDTH, BUTTON_PLAY_HEIGHT);
        }

        game.batch.end();

    }

    @Override
    public void resize(int i, int i1) {

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
