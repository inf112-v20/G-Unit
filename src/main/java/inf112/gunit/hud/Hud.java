package inf112.gunit.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.gunit.GameState;
import inf112.gunit.main.Main;
import inf112.gunit.player.card.CardType;
import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;
import inf112.gunit.screens.Game;

public class Hud implements Disposable {

    private final Game game;
    private final Texture bg = new Texture("assets/program_sheet_bg.png");
    private final Texture DAMAGE_TOKEN_FILLED = new Texture("assets/damage_token.png");
    private final Texture DAMAGE_TOKEN = new Texture("assets/damage_token_grey.png");
    private final Texture LIFE_TOKEN_FILLED = new Texture("assets/life_token.png");
    private final Texture LIFE_TOKEN = new Texture("assets/life_token_grey.png");
    private final Texture POWER_DOWN = new Texture("assets/power_down.png");
    private final Texture POWER_DOWN_GREY = new Texture("assets/power_down_grey.png");
    private final Texture SUBMIT_GREEN = new Texture("assets/submit_green.png");
    private final Texture SUBMIT_GREY = new Texture("assets/submit_grey.png");

    private final TextureRegion[][] CARD_TEXTURES = TextureRegion.split(new Texture("assets/card_sprites.png"), 75, 100);
    private final TextureRegion[][] FLAG_TEXTURES = TextureRegion.split(new Texture("assets/flag_sprites.png"), 300, 300);

    private final int TEXTURE_PADDING = 30;
    private final float FLAG_SCALE = 0.3f;

    private Table damageTokenTable;
    private Table lifeTokenTable;
    private Table cardTable;
    private Table flagTable;

    private ImageButton powerDownButton;
    private ImageButton submitButton;

    public Stage stage;

    private Viewport viewport;

    public Hud(SpriteBatch batch, final Game game) {
        this.game = game;

        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        flagTable = new Table();
        flagTable.top();
        flagTable.setFillParent(true);
        flagTable.setPosition(510, 230);

        damageTokenTable = new Table();
        damageTokenTable.top();
        damageTokenTable.setFillParent(true);
        damageTokenTable.setPosition(500, -400);

        lifeTokenTable = new Table();
        lifeTokenTable.top();
        lifeTokenTable.setFillParent(true);
        lifeTokenTable.setPosition(400, -250);

        cardTable = new Table();
        cardTable.top();
        cardTable.setFillParent(true);
        cardTable.setPosition(500, 0);

        ImageButton.ImageButtonStyle powerDownStyle = new ImageButton.ImageButtonStyle();
        powerDownStyle.imageUp = new TextureRegionDrawable(new TextureRegion(POWER_DOWN_GREY));
        powerDownStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(POWER_DOWN));
        // TODO: make own texture for this
        powerDownStyle.imageDown = new TextureRegionDrawable(new TextureRegion(POWER_DOWN));
        powerDownStyle.imageOver = new TextureRegionDrawable(new TextureRegion(POWER_DOWN));
        powerDownButton = new ImageButton(powerDownStyle);
        powerDownButton.setPosition(Main.WIDTH - 500 + TEXTURE_PADDING, Main.HEIGHT - POWER_DOWN.getHeight() - TEXTURE_PADDING);
        powerDownButton.addListener(addButtonListener(powerDownButton));
        powerDownButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("powerdown");
            }
        });

        ImageButton.ImageButtonStyle submitStyle = new ImageButton.ImageButtonStyle();
        submitStyle.imageUp = new TextureRegionDrawable(new TextureRegion(SUBMIT_GREY));
        submitStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(SUBMIT_GREEN));
        // TODO: make own texture for this
        submitStyle.imageDown = new TextureRegionDrawable(new TextureRegion(SUBMIT_GREEN));
        submitStyle.imageOver = new TextureRegionDrawable(new TextureRegion(SUBMIT_GREEN));
        submitButton = new ImageButton(submitStyle);
        submitButton.setPosition(Main.WIDTH - 200, 200);
        submitButton.addListener(addButtonListener(submitButton));
        submitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (game.getPlayerRobot().getProgramBuffer().size() == 5)
                    game.getPlayerRobot().isDonePicking = true;
            }
        });

        stage.addActor(flagTable);
        stage.addActor(damageTokenTable);
        stage.addActor(lifeTokenTable);
        stage.addActor(cardTable);
        stage.addActor(powerDownButton);
        stage.addActor(submitButton);

        updateFlags();
        updateDamageTokens();
        updateLifeTokens();

        Gdx.input.setInputProcessor(stage);
    }

    private ClickListener addButtonListener(final Button button) {
        return new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                button.setChecked(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                button.setChecked(false);
            }
        };
    }

    private void updateFlags() {
        flagTable.reset();

        for (int i = 0; i < game.getPlayerRobot().getFlagsCollected(); i++) {
            Drawable d = new TextureRegionDrawable(FLAG_TEXTURES[0][i]);
            d.setMinWidth(d.getMinWidth() * FLAG_SCALE);
            d.setMinHeight(d.getMinHeight() * FLAG_SCALE);
            flagTable.add(new Image(d));
        }
    }

    private void updateDamageTokens() {
        damageTokenTable.reset();
        int damageTokens = game.getPlayerRobot().getDamageMarkers();

        for (int i = 0; i < 10; i++) {
            if (i < damageTokens) damageTokenTable.add(new Image(DAMAGE_TOKEN_FILLED));
            else damageTokenTable.add(new Image(DAMAGE_TOKEN));
            if (damageTokenTable.getCells().size % 5 == 0) damageTokenTable.row();
        }
    }

    private void updateLifeTokens() {
        lifeTokenTable.reset();
        int lifeTokens = game.getPlayerRobot().getLifeTokens();

        for (int i = 0; i < 3; i++) {
            if (i < lifeTokens) lifeTokenTable.add(new Image(LIFE_TOKEN_FILLED));
            else lifeTokenTable.add(new Image(LIFE_TOKEN));
        }

    }

    public void updateCards() {
        cardTable.reset();

        for (final ProgramCard card : game.getPlayerRobot().getCardDeck()) {
            CardType type = card.getType();
            int x;
            int y;

            if (type == CardType.MOVEMENT) {
                y = 0;
                x = ((MovementCard) card).getDistance() - 1;
            } else {
                y = 1;
                if (((RotationCard) card).getRotations() == 2) {
                    x = 2;
                } else {
                    x = (((RotationCard) card).isClockwise()) ? 0 : 1;
                }
            }
            ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
            style.imageUp = new TextureRegionDrawable(CARD_TEXTURES[y][x]);
            style.imageChecked = new TextureRegionDrawable(CARD_TEXTURES[y][x]);
            style.imageChecked.setMinHeight(style.imageChecked.getMinHeight() * 0.9f);
            style.imageChecked.setMinWidth(style.imageChecked.getMinWidth() * 0.9f);

            style.font = Main.font;
            final ProgramCardButton button = new ProgramCardButton(card, String.valueOf(card.getPriority()), style);
            button.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    button.setChecked(true);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    button.setChecked(false);
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (game.getPlayerRobot().getProgramBuffer().size() < 5) {
                        game.getPlayerRobot().addBufferCard(card);
                        cardTable.removeActor(button);
                    }
                }
            });

            cardTable.add(button).pad(10);
            if (cardTable.getCells().size % 3 == 0) cardTable.row();
        }

    }

    public void clearCards() {
        cardTable.reset();
    }

    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());

        stage.getBatch().begin();
        stage.getBatch().draw(bg, 1000, 0, 500, 1000);
        stage.getBatch().draw(game.getPlayerRobot().getTexture(), Main.WIDTH - 200 - TEXTURE_PADDING, Main.HEIGHT - 200 - TEXTURE_PADDING, 200, 200);
        stage.getBatch().end();

        updateDamageTokens();
        updateLifeTokens();
        updateFlags();

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
