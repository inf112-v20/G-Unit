package inf112.gunit.board;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.player.Robot;
import inf112.gunit.screens.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class LaserTests {

    public Robot mainRobot;
    public Robot otherRobot;
    public Board board;

    @Before
    public void initialise() {
        Game game = new Game(2, new TmxMapLoader().load("assets/board_new.tmx"));
        this.mainRobot = game.getRobots()[0];
        this.otherRobot = game.getRobots()[1];
        this.board = new Board(game);
        mainRobot.setPosition(new Vector2(0,0));
        mainRobot.setDirection(Direction.NORTH);
    }

    @Test
    public void testLasersDoesDamage() {
        int oldDamageTokens = mainRobot.getDamageMarkers();
        mainRobot.setPosition(new Vector2(0, 2));
        board.lasersFire();
        assertEquals(oldDamageTokens + 2, mainRobot.getDamageMarkers());
    }

    @Test
    public void laserDoesntPassWall() {
        int oldDamageTokens = mainRobot.getDamageMarkers();
        mainRobot.setPosition(new Vector2(3, 2));
        board.lasersFire();
        assertEquals(oldDamageTokens, mainRobot.getDamageMarkers());
    }

    @Test
    public void laserHitsOnCorrectWallTile() {
        int oldDamageTokens = mainRobot.getDamageMarkers();
        mainRobot.setPosition(new Vector2(2, 2));
        board.lasersFire();
        assertEquals(oldDamageTokens + 2, mainRobot.getDamageMarkers());
    }

    @Test
    public void robotBlocksLaser() {
        int mainRobotOldDamageTokens = mainRobot.getDamageMarkers();
        int otherRobotOldDamageTokens = otherRobot.getDamageMarkers();
        mainRobot.setPosition(new Vector2(7, 2));
        otherRobot.setPosition(new Vector2(8, 2));
        board.lasersFire();
        assertEquals(mainRobotOldDamageTokens, mainRobot.getDamageMarkers());
        assertEquals(otherRobotOldDamageTokens + 2, otherRobot.getDamageMarkers());
    }

}
