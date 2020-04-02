package inf112.gunit.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.gunit.main.Main;
import inf112.gunit.screens.Game;

public class Hud implements Disposable {

    private final Game game;
    private final Texture bg = new Texture("assets/program_sheet_bg.png");
    private final Texture DAMAGE_TOKEN_FILLED = new Texture("assets/damage_token.png");
    private final Texture DAMAGE_TOKEN = new Texture("assets/damage_token_grey.png");
    private final Texture LIFE_TOKEN_FILLED = new Texture("assets/life_token.png");
    private final Texture LIFE_TOKEN = new Texture("assets/life_token_grey.png");

    private Table damageTokenTable;
    private Table lifeTokenTable;
    private Table cardTable;

    public Stage stage;

    private Viewport viewport;

    public Hud(SpriteBatch batch, Game game) {
        this.game = game;

        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        damageTokenTable = new Table();
        damageTokenTable.top();
        damageTokenTable.setFillParent(true);
        damageTokenTable.setPosition(500, -300);
        stage.addActor(damageTokenTable);

        updateDamageTokens();
    }

    public void updateDamageTokens() {
        damageTokenTable.reset();
        int damageTokens = game.getPlayerRobot().getDamageMarkers();
        System.out.println(damageTokens);
        for (int i = 0; i < 10; i++) {
            if (damageTokens > 0) {
                damageTokenTable.add(new Image(DAMAGE_TOKEN_FILLED));
                damageTokens++;
            }
            else damageTokenTable.add(new Image(DAMAGE_TOKEN));
            if (i == 4) damageTokenTable.row();
        }

    }

    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());

        stage.getBatch().begin();
        stage.getBatch().draw(bg, 1000, 0, 500, 1000);
        stage.getBatch().end();

        updateDamageTokens();

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
