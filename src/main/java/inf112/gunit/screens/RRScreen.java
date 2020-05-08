package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// RoboRally screen with assets loaded in
public abstract class RRScreen implements Screen {

    protected TextureAtlas atlas = new TextureAtlas("assets/clean-crispy/skin/clean-crispy-ui.atlas");
    protected Skin  skin = new Skin(Gdx.files.internal("assets/clean-crispy/skin/clean-crispy-ui.json"), atlas);

    protected int width = 1000;
    protected int height = 1000;
}
