package inf112.gunit.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.gunit.screens.Menu;

public class Main extends Game {

    //put globals here
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 1000;

    public static Cursor arrow;
    public static Cursor pointer;

    public static SpriteBatch batch;
    public static BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        arrow = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("assets/cursors/arrow.png")), 0, 0);
        pointer = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("assets/cursors/pointer.png")), 11, 0);

        Gdx.graphics.setCursor(arrow);

        Gdx.gl.glClearColor(0,0,0,1);
        this.setScreen(new Menu(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "RoboRally";
        cfg.width = Main.WIDTH;
        cfg.height = Main.HEIGHT;
        cfg.resizable = false;
        cfg.foregroundFPS = 60;

        new LwjglApplication(new Main(), cfg);
    }
}
