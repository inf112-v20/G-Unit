package inf112.gunit.board.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.gunit.board.Direction;
import inf112.gunit.screens.Game;
import org.w3c.dom.Text;

public class Hole extends TileObject {

    public Hole(Direction direction, int posX, int posY) {
        super("Hole", new TextureRegion(Game.spriteSheet[0][0]), direction, posX, posY);
    }
}
