package inf112.gunit.hud;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import inf112.gunit.player.card.ProgramCard;

public class ProgramCardButton extends ImageTextButton {

    private ProgramCard card;

    public ProgramCardButton(ProgramCard card, String text, ImageTextButtonStyle style) {
        super(text, style);
        this.card = card;
    }

    public ProgramCard getCard() {
        return card;
    }
}
