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
    private Robot robotBeingShot;
    private Robot robotHidingBehind;

    // EW East West Positions and Directions
    private Vector2 shooterPosEW = new Vector2(0,0);
    private Vector2 beingShotPosEW = new Vector2(1,0);
    private Vector2 hidingBehindPosEW = new Vector2(2,0);

    private Direction shooterDirEW = Direction.EAST;
    private Direction beingShotDirEW = Direction.NORTH;
    private Direction hidingBehindDirEW = Direction.WEST;

    // NS North South Positions and Directions
    private Vector2 shooterPosNS = new Vector2(0,0);
    private Vector2 beingShotPosNS = new Vector2(0,1);
    private Vector2 hidingBehindPosNS = new Vector2(0,2);

    private Direction shooterDirNS = Direction.SOUTH;
    private Direction beingShotDirNS = Direction.EAST;
    private Direction hidingBehindDirNS = Direction.NORTH;

    @Before
    public void initialise(){

        Game game = new Game(3, new TmxMapLoader().load("assets/board_new.tmx"));
        this.board = new Board(game);

        this.robotShooter = game.getRobots()[0];
        this.robotBeingShot = game.getRobots()[1];
        this.robotHidingBehind = game.getRobots()[2];

        robotShooter.setPosition(new Vector2(0,0));
        robotBeingShot.setPosition(new Vector2(0,0));
        robotHidingBehind.setPosition(new Vector2(0,0));
    }

    @Test
    public void testToSeeIfRobotsAreTakingDamageEastWest(){
        robotShooter.setPosition(shooterPosEW.cpy());
        robotBeingShot.setPosition(beingShotPosEW.cpy());
        robotHidingBehind.setPosition(hidingBehindPosEW.cpy());

        robotShooter.setDirection(shooterDirEW);
        robotBeingShot.setDirection(beingShotDirEW);
        robotHidingBehind.setDirection(hidingBehindDirEW);

        System.out.println(robotShooter.toString() + " robot pos: " + robotShooter.getPosition());
        System.out.println(robotBeingShot.toString() + " robot pos: " + robotBeingShot.getPosition());
        System.out.println(robotHidingBehind.toString() + " robot pos: " + robotHidingBehind.getPosition());

        System.out.println(robotShooter.toString() + " robot dir: " + robotShooter.getDirection());
        System.out.println(robotBeingShot.toString() + " robot dir: " + robotBeingShot.getDirection());
        System.out.println(robotHidingBehind.toString() + " robot dir: " + robotHidingBehind.getDirection());

        int before = robotBeingShot.getDamageMarkers();
        board.robotsFire();
        int after = robotBeingShot.getDamageMarkers();

        assertEquals(before - 2, after);
    }

    @Test
    public void testToSeeIfRobotsAreTakingDamageNorthSouth(){
        robotShooter.setPosition(shooterPosNS.cpy());
        robotBeingShot.setPosition(beingShotPosNS.cpy());
        robotHidingBehind.setPosition(hidingBehindPosNS.cpy());

        robotShooter.setDirection(shooterDirNS);
        robotBeingShot.setDirection(beingShotDirNS);
        robotHidingBehind.setDirection(hidingBehindDirNS);

        System.out.println(robotShooter.toString() + " robot pos: " + robotShooter.getPosition());
        System.out.println(robotBeingShot.toString() + " robot pos: " + robotBeingShot.getPosition());
        System.out.println(robotHidingBehind.toString() + " robot pos: " + robotHidingBehind.getPosition());

        System.out.println(robotShooter.toString() + " robot dir: " + robotShooter.getDirection());
        System.out.println(robotBeingShot.toString() + " robot dir: " + robotBeingShot.getDirection());
        System.out.println(robotHidingBehind.toString() + " robot dir: " + robotHidingBehind.getDirection());

        int before = robotBeingShot.getDamageMarkers();
        board.robotsFire();
        int after = robotBeingShot.getDamageMarkers();

        assertEquals(before - 2, after);
    }

    @Test
    public void testToCheckThatLasersDoNotGoThroughRobotsFromWest(){
        robotShooter.setPosition(shooterPosEW.cpy());
        robotBeingShot.setPosition(beingShotPosEW.cpy());
        robotHidingBehind.setPosition(hidingBehindPosEW.cpy());

        robotShooter.setDirection(shooterDirEW);
        robotBeingShot.setDirection(beingShotDirEW);
        robotHidingBehind.setDirection(hidingBehindDirEW);

        int before = robotHidingBehind.getDamageMarkers();
        board.robotsFire();
        int after = robotHidingBehind.getDamageMarkers();

        assertEquals(before, after);
    }

    @Test
    public void testToCheckThatLasersDoNotGoThroughRobotsFromEast(){
        robotShooter.setPosition(shooterPosEW.cpy());
        robotBeingShot.setPosition(beingShotPosEW.cpy());
        robotHidingBehind.setPosition(hidingBehindPosEW.cpy());

        robotShooter.setDirection(shooterDirEW);
        robotBeingShot.setDirection(beingShotDirEW);
        robotHidingBehind.setDirection(hidingBehindDirEW);

        int before = robotShooter.getDamageMarkers();
        board.robotsFire();
        int after = robotShooter.getDamageMarkers();

        assertEquals(before, after);
    }

    @Test
    public void testToCheckThatLasersDoNotGoThroughRobotsFromNorth(){
        robotShooter.setPosition(shooterPosNS.cpy());
        robotBeingShot.setPosition(beingShotPosNS.cpy());
        robotHidingBehind.setPosition(hidingBehindPosNS.cpy());

        robotShooter.setDirection(shooterDirNS);
        robotBeingShot.setDirection(beingShotDirNS);
        robotHidingBehind.setDirection(hidingBehindDirNS);

        int before = robotHidingBehind.getDamageMarkers();
        board.robotsFire();
        int after = robotHidingBehind.getDamageMarkers();

        assertEquals(before, after);
    }

    @Test
    public void testToCheckThatLasersDoNotGoThroughRobotsFromSouth(){
        robotShooter.setPosition(shooterPosNS.cpy());
        robotBeingShot.setPosition(beingShotPosNS.cpy());
        robotHidingBehind.setPosition(hidingBehindPosNS.cpy());

        robotShooter.setDirection(shooterDirNS);
        robotBeingShot.setDirection(beingShotDirNS);
        robotHidingBehind.setDirection(hidingBehindDirNS);

        int before = robotShooter.getDamageMarkers();
        board.robotsFire();
        int after = robotShooter.getDamageMarkers();

        assertEquals(before, after);
    }
}