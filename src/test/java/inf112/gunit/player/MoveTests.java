package inf112.gunit.player;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * these test do not pass yet becuase, how the fuck do i load resources from asset folder?????
 *
 * Players start-direction is always north, see player constructor
 */
public class MoveTests {

    TiledMap map = new TmxMapLoader().load("assets/tile_map_3Kx3K_full.tmx"); //nullpointer exception on load
    Player player = new Player(map, map.getProperties());
    int x = (int) player.getPositionX();
    int y = (int) player.getPositionY();

    /**
     * Test if the player can move with distance = 1
     */
    @Test
    public void moveDistOne() {
        int y = (int) player.getPositionY();
        player.move(1);
        assertEquals(y+1, (int) player.getPositionY());
    }

    /**
     * Test if the player can move with distance = 2
     */
    @Test
    public void moveDistTwo() {
        int y = (int) player.getPositionY();
        player.move(2);
        assertEquals(y+2, (int) player.getPositionY());
    }

    /**
     * Test that the player cant move out of the map
     */
    @Test
    public void moveIllegalY() {
        int y = (int) player.getPositionY();
        player.rotate(true, 2); //rotate player 180 degrees, to face south (position is 0,0)
        player.move(1);
        assertEquals(y, (int) player.getPositionY()); //check that distance is the same as when started
    }
}
