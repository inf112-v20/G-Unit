package inf112.gunit.hud;

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
import inf112.gunit.main.Main;
import inf112.gunit.screens.Game;

public class Hud implements Disposable {

    private final Game game;

    public Stage stage;

    private Viewport viewport;

    public Hud(SpriteBatch batch, Game game) {
        this.game = game;

        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Label halla = new Label("HALLA", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label snakkes = new Label("SNAKKES", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label hade = new Label("HADE", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label hei = new Label("HEI", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label hadebra = new Label("HADEBRA", new Label.LabelStyle(new BitmapFont(), Color.RED));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(halla).expandX().padTop(10);
        table.add(snakkes).expandX().padTop(10);
        table.add(hade).expandX().padTop(10);
        table.row();
        table.add(hei).expandX().padTop(10);
        table.add(hadebra).expandX().padTop(10);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
