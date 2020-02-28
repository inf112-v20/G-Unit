package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import inf112.gunit.main.Main;

/**
 * The Menu class is called from the Main class as
 * the first screen to be rendered by the program.
 */
public class Menu implements Screen {

    // global variables for buttons
    private static final int BUTTON_PLAY_WIDTH = 200;
    private static final int BUTTON_PLAY_HEIGHT = 80;
    private static final int BUTTON_PLAY_X = (Main.WIDTH / 2) - (BUTTON_PLAY_WIDTH/2);
    private static final int BUTTON_PLAY_Y = (Main.HEIGHT / 2) - (BUTTON_PLAY_HEIGHT/2);

    private Main main;

    private Texture buttonPlay;
    private Texture buttonPlayActive;

    /**
     * The Menu construcor
     * @param main takes a main (which is a game)
     */
    public Menu(Main main) {
        this.main = main;
        this.buttonPlay = new Texture("assets/menu-items/button_play.png");
        this.buttonPlayActive = new Texture("assets/menu-items/button_play_active.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        main.batch.begin();

        //render active button if mouse over the button
        if (Gdx.input.getY() > BUTTON_PLAY_Y && Gdx.input.getY() < BUTTON_PLAY_Y + BUTTON_PLAY_HEIGHT && Gdx.input.getX() > BUTTON_PLAY_X && Gdx.input.getX() < BUTTON_PLAY_X + BUTTON_PLAY_WIDTH) {
            main.batch.draw(buttonPlayActive, BUTTON_PLAY_X, BUTTON_PLAY_Y, BUTTON_PLAY_WIDTH, BUTTON_PLAY_HEIGHT);

            //start game when button is pressed
            if (Gdx.input.isTouched()) {
                main.setScreen(new Game(main, new TmxMapLoader().load("assets/tile_map_3Kx3K_full.tmx")));
                this.dispose();
            }

        } else {
            //else render the normal button
            main.batch.draw(buttonPlay, BUTTON_PLAY_X, BUTTON_PLAY_Y, BUTTON_PLAY_WIDTH, BUTTON_PLAY_HEIGHT);
        }

        main.batch.end();

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
