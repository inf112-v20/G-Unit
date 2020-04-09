package inf112.gunit.player;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Board;
import inf112.gunit.board.Direction;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.screens.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class RobotShootLaserTest {

    private Board board;

    private Robot robotShooter;
    private Robot robotGettingShot;
    private Robot robotHidingBehind;

    private Vector2 shooterPos = new Vector2(0,0);
    private Vector2 beingShotPos = new Vector2(1,0);
    private Vector2 hidingBehindPos = new Vector2(2,0);

    private int before;

    @Before
    public void initialise(){
        Game game = new Game(3, new TmxMapLoader().load("assets/board_new.tmx"));
        this.board = new Board(game);

        this.robotShooter = game.getRobots()[0];
        this.robotGettingShot = game.getRobots()[1];
        this.robotHidingBehind = game.getRobots()[2];


        robotShooter.setPosition(new Vector2(0,0));
        robotGettingShot.setPosition(new Vector2(0,0));
        robotHidingBehind.setPosition(new Vector2(0,0));

        robotShooter.setDirection(Direction.EAST);
        robotGettingShot.setDirection(Direction.NORTH);
        robotHidingBehind.setDirection(Direction.WEST);

        before = robotGettingShot.getDamageMarkers();
    }

    @Test
    public void testToSeeIfRobotsAreTakingDamage(){
        robotShooter.setPosition(shooterPos.cpy());
        robotGettingShot.setPosition(beingShotPos.cpy());
        robotHidingBehind.setPosition(hidingBehindPos.cpy());

        System.out.println("before: " + before);
        board.robotsFire();
        int after = robotGettingShot.getDamageMarkers();
        System.out.println("after: " + robotGettingShot.getDamageMarkers());

        assertEquals(before - 2, after);
    }

    @Test
    public void testToCheckThatLasersDoNotGoThroughRobots(){
        robotShooter.setPosition(shooterPos.cpy());
        robotGettingShot.setPosition(beingShotPos.cpy());
        robotHidingBehind.setPosition(hidingBehindPos.cpy());

        int before = robotHidingBehind.getDamageMarkers();
        board.robotsFire();
        int after = robotHidingBehind.getDamageMarkers();

        assertEquals(before, after);
    }
}