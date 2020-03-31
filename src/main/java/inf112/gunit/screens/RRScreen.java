package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// RoboRally screen with assets loaded in
public class RRScreen implements Screen {

    protected TextureAtlas atlas = new TextureAtlas("assets/clean-crispy/skin/clean-crispy-ui.atlas");
    protected Skin  skin = new Skin(Gdx.files.internal("assets/clean-crispy/skin/clean-crispy-ui.json"), atlas);


    protected int width = 1000;
    protected int height = 1000;

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

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
