package inf112.gunit.board.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Direction;

public class TileObject {

    private String name;
    private TextureRegion textureRegion;
    private Direction direction;

    private Vector2 position;

    public TileObject(String name, TextureRegion textureRegion, Direction direction, int posX, int posY) {
        this.name = name;
        this.textureRegion = textureRegion;
        this.direction = direction;

        this.position = new Vector2((float) posX, (float) posY);
    }

    public TextureRegion getTexture() {
        return textureRegion;
    }

    @Override
    public String toString() {
        return name + " " + direction;
    }
}
