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
import inf112.gunit.main.Main;
import inf112.gunit.player.card.CardType;
import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;
import inf112.gunit.screens.Game;

public class Hud implements Disposable {

    private final Game game;
    private final Texture bg = new Texture("assets/hud/program_sheet_bg.png");
    private final Texture DAMAGE_TOKEN_FILLED = new Texture("assets/hud/damage_token.png");
    private final Texture DAMAGE_TOKEN = new Texture("assets/hud/damage_token_grey.png");
    private final Texture LIFE_TOKEN_FILLED = new Texture("assets/hud/life_token.png");
    private final Texture LIFE_TOKEN = new Texture("assets/hud/life_token_grey.png");
    private final Texture POWER_DOWN = new Texture("assets/hud/power_down.png");
    private final Texture POWER_DOWN_GREY = new Texture("assets/hud/power_down_grey.png");
    private final Texture SUBMIT_GREEN = new Texture("assets/hud/submit_green.png");
    private final Texture SUBMIT_GREY = new Texture("assets/hud/submit_grey.png");

    private final TextureRegion[][] CARD_TEXTURES = TextureRegion.split(new Texture("assets/hud/card_sprites.png"), 75, 100);
    private final TextureRegion[][] FLAG_TEXTURES = TextureRegion.split(new Texture("assets/hud/flag_sprites.png"), 300, 300);

    private final int TEXTURE_PADDING = 30;
    private final float FLAG_SCALE = 0.3f;

    private Table damageTokenTable;
    private Table lifeTokenTable;
    private Table cardTable;
    private Table flagTable;

    private ImageButton powerDownButton;
    private ImageButton submitButton;
    
    private ImageButton.ImageButtonStyle powerDownUncheckedStyle;
    private ImageButton.ImageButtonStyle powerDownCheckedStyle;
    
    private ImageButton.ImageButtonStyle submitReadyStyle;
    private ImageButton.ImageButtonStyle submitUnreadyStyle;

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

        powerDownUncheckedStyle = new ImageButton.ImageButtonStyle();
        powerDownUncheckedStyle.imageUp = new TextureRegionDrawable(new TextureRegion(POWER_DOWN_GREY));
        powerDownUncheckedStyle.imageDown = new TextureRegionDrawable(new TextureRegion(POWER_DOWN));

        powerDownCheckedStyle = new ImageButton.ImageButtonStyle();
        powerDownCheckedStyle.imageUp = new TextureRegionDrawable(new TextureRegion(POWER_DOWN));
        powerDownCheckedStyle.imageDown = new TextureRegionDrawable(new TextureRegion(POWER_DOWN_GREY));

        submitUnreadyStyle = new ImageButton.ImageButtonStyle();
        submitUnreadyStyle.imageUp = new TextureRegionDrawable(new TextureRegion(SUBMIT_GREY));

        submitReadyStyle = new ImageButton.ImageButtonStyle();
        submitReadyStyle.imageUp = new TextureRegionDrawable(new TextureRegion(SUBMIT_GREEN));

        powerDownButton = new ImageButton(powerDownUncheckedStyle);
        powerDownButton.setPosition(Main.WIDTH - 500 + TEXTURE_PADDING, Main.HEIGHT - POWER_DOWN.getHeight() - TEXTURE_PADDING);
        powerDownButton.addListener(addButtonListener(powerDownButton));
        powerDownButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.getPlayerRobot().updatePowerDownDesire();
                powerDownButton.setStyle((game.getPlayerRobot().getPowerDownDesire()) ? powerDownCheckedStyle : powerDownUncheckedStyle);
            }
        });

        submitButton = new ImageButton(submitUnreadyStyle);
        submitButton.setPosition(Main.WIDTH - 200, 200);
        submitButton.addListener(addButtonListener(submitButton));
        submitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (game.getPlayerRobot().getProgramBuffer().size() == 5) {
                    submitButton.setStyle(submitUnreadyStyle);
                    game.getPlayerRobot().isDonePicking = true;
                }
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

    /**
     * Common ethod for adding mouse listeners for the buttons
     * @param button the button to add the listener to
     * @return a new ClickListener
     */
    private ClickListener addButtonListener(final Button button) {
        return new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                button.setChecked(true);
                if(button == submitButton && submitButton.getStyle() == submitUnreadyStyle)
                    Gdx.graphics.setCursor(Main.arrow);
                else
                    Gdx.graphics.setCursor(Main.pointer);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                button.setChecked(false);
                Gdx.graphics.setCursor(Main.arrow);
            }
        };
    }

    /**
     * Update the flag-table, which draws all flags picked up by the players robot
     */
    private void updateFlags() {
        flagTable.reset();

        for (int i = 0; i < game.getPlayerRobot().getFlagsCollected(); i++) {
            Drawable d = new TextureRegionDrawable(FLAG_TEXTURES[0][i]);
            d.setMinWidth(d.getMinWidth() * FLAG_SCALE);
            d.setMinHeight(d.getMinHeight() * FLAG_SCALE);
            flagTable.add(new Image(d));
        }
    }

    /**
     * Update the damage token-table, which draws all damage tokens according to the players robot
     */
    private void updateDamageTokens() {
        damageTokenTable.reset();
        int damageTokens = game.getPlayerRobot().getDamageMarkers();

        for (int i = 0; i < 10; i++) {
            if (i < damageTokens) damageTokenTable.add(new Image(DAMAGE_TOKEN_FILLED));
            else damageTokenTable.add(new Image(DAMAGE_TOKEN));
            if (damageTokenTable.getCells().size % 5 == 0) damageTokenTable.row();
        }
    }

    /**
     * Update the life token-table, which draws all life tokens according to the players robot
     */
    private void updateLifeTokens() {
        lifeTokenTable.reset();
        int lifeTokens = game.getPlayerRobot().getLifeTokens();

        for (int i = 0; i < 3; i++) {
            if (i < lifeTokens) lifeTokenTable.add(new Image(LIFE_TOKEN_FILLED));
            else lifeTokenTable.add(new Image(LIFE_TOKEN));
        }

    }

    /**
     * Update the card-table, which is used for picking program cards to program the robot
     */
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
                    Gdx.graphics.setCursor(Main.pointer);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    button.setChecked(false);
                    Gdx.graphics.setCursor(Main.arrow);
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (game.getPlayerRobot().getProgramBuffer().size() < 5) {
                        game.getPlayerRobot().addBufferCard(card);
                        cardTable.removeActor(button);
                        if (game.getPlayerRobot().getProgramBuffer().size() >= 5)
                            submitButton.setStyle(submitReadyStyle);
                    }
                }
            });

            cardTable.add(button).pad(10);
            if (cardTable.getCells().size % 3 == 0) cardTable.row();
        }

    }

    /**
     * Clear card-buttons from the hud
     */
    public void clearCards() {
        cardTable.reset();
    }

    /**
     * Reset the powerdown-button style
     */
    public void resetPowerDownButton() {
        powerDownButton.setStyle(powerDownUncheckedStyle);
    }

    /**
     * Draw the hud
     */
    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());

        stage.getBatch().begin();
        stage.getBatch().draw(bg, 1000, 0, 500, 1000);
        stage.getBatch().draw(game.getPlayerRobot().getTextureRegion(), Main.WIDTH - 200 - TEXTURE_PADDING, Main.HEIGHT - 200 - TEXTURE_PADDING, 200, 200);
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
