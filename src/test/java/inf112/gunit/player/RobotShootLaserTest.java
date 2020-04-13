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
    private Robot robotFarAway;

    // EW East West Positions and Directions
    private Vector2 shooterPosEW = new Vector2(0,0);
    private Vector2 beingShotPosEW = new Vector2(1,0);
    private Vector2 hidingBehindPosEW = new Vector2(2,0);
    private Vector2 farAwayOutOfTheWay = new Vector2(5,5);

    private Direction shooterDirEW = Direction.EAST;
    private Direction beingShotDirEW = Direction.NORTH;
    private Direction hidingBehindDirEW = Direction.WEST;

    // NS North South Positions and Directions
    private Vector2 shooterPosNS = new Vector2(0,0);
    private Vector2 beingShotPosNS = new Vector2(0,1);
    private Vector2 hidingBehindPosNS = new Vector2(0,2);

    private Direction shooterDirNS = Direction.NORTH;
    private Direction beingShotDirNS = Direction.EAST;
    private Direction hidingBehindDirNS = Direction.SOUTH;

    // Far away test case positions and directions
    // East
    private Vector2 shooterPosFarAwayEast = new Vector2(0,0);
    private Vector2 farAwayPosEast = new Vector2(9, 0);

    private Direction shooterDirFarAwayEast = Direction.EAST;
    private Direction farAwayDirEast = Direction.SOUTH;

    // West
    private Vector2 shooterPosFarAwayWest = new Vector2(9,0);
    private Vector2 farAwayPosWest = new Vector2(0, 0);

    private Direction shooterDirFarAwayWest = Direction.WEST;
    private Direction farAwayDirWest = Direction.SOUTH;

    // North
    private Vector2 shooterPosFarAwayNorth = new Vector2(5,0);
    private Vector2 farAwayPosNorth = new Vector2(5, 9);

    private Direction shooterDirFarAwayNorth = Direction.NORTH;
    private Direction farAwayDirNorth = Direction.NORTH;

    // South
    private Vector2 shooterPosFarAwaySouth = new Vector2(5,9);
    private Vector2 farAwayPosSouth = new Vector2(5, 0);

    private Direction shooterDirFarAwaySouth = Direction.SOUTH;
    private Direction farAwayDirSouth = Direction.SOUTH;



    @Before
    public void initialise(){

        Game game = new Game(4, new TmxMapLoader().load("assets/board_new.tmx"));
        this.board = new Board(game);

        this.robotShooter = game.getRobots()[0];
        this.robotBeingShot = game.getRobots()[1];
        this.robotHidingBehind = game.getRobots()[2];
        this.robotFarAway = game.getRobots()[3];

        robotShooter.setPosition(new Vector2(0,0));
        robotBeingShot.setPosition(new Vector2(0,0));
        robotHidingBehind.setPosition(new Vector2(0,0));
        robotFarAway.setPosition(new Vector2(0,0));
    }

    @Test
    public void testToSeeIfRobotsAreTakingDamageEastWest(){
        robotShooter.setPosition(shooterPosEW.cpy());
        robotBeingShot.setPosition(beingShotPosEW.cpy());
        robotHidingBehind.setPosition(hidingBehindPosEW.cpy());
        robotFarAway.setPosition(farAwayOutOfTheWay.cpy());

        robotShooter.setDirection(shooterDirEW);
        robotBeingShot.setDirection(beingShotDirEW);
        robotHidingBehind.setDirection(hidingBehindDirEW);

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
        robotFarAway.setPosition(farAwayOutOfTheWay.cpy());

        robotShooter.setDirection(shooterDirNS);
        robotBeingShot.setDirection(beingShotDirNS);
        robotHidingBehind.setDirection(hidingBehindDirNS);

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
        robotFarAway.setPosition(farAwayOutOfTheWay.cpy());

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

    @Test
    public void testToConfirmThatShooterCanReachEndOfBoardFromEast(){
        robotShooter.setPosition(shooterPosFarAwayEast.cpy());
        robotFarAway.setPosition(farAwayPosEast.cpy());

        robotShooter.setDirection(shooterDirFarAwayEast);
        robotFarAway.setDirection(farAwayDirEast);

        int before = robotFarAway.getDamageMarkers();
        board.robotsFire();
        int after = robotFarAway.getDamageMarkers();

        assertEquals(before - 1, after);
    }

    @Test
    public void testToConfirmThatShooterCanReachEndOfBoardFromWest(){
        robotShooter.setPosition(shooterPosFarAwayWest.cpy());
        robotFarAway.setPosition(farAwayPosWest.cpy());

        robotShooter.setDirection(shooterDirFarAwayWest);
        robotFarAway.setDirection(farAwayDirWest);

        int before = robotFarAway.getDamageMarkers();
        board.robotsFire();
        int after = robotFarAway.getDamageMarkers();

        assertEquals(before - 1, after);
    }

    @Test
    public void testToConfirmThatShooterCanReachEndOfBoardFromNorth(){
        robotShooter.setPosition(shooterPosFarAwayNorth.cpy());
        robotFarAway.setPosition(farAwayPosNorth.cpy());

        robotShooter.setDirection(shooterDirFarAwayNorth);
        robotFarAway.setDirection(farAwayDirNorth);

        int before = robotFarAway.getDamageMarkers();
        board.robotsFire();
        int after = robotFarAway.getDamageMarkers();

        assertEquals(before - 1, after);
    }

    @Test
    public void testToConfirmThatShooterCanReachEndOfBoardFromSouth(){
        robotShooter.setPosition(shooterPosFarAwaySouth.cpy());
        robotFarAway.setPosition(farAwayPosSouth.cpy());

        robotShooter.setDirection(shooterDirFarAwaySouth);
        robotFarAway.setDirection(farAwayDirSouth);

        int before = robotFarAway.getDamageMarkers();
        board.robotsFire();
        int after = robotFarAway.getDamageMarkers();

        assertEquals(before - 1, after);
    }
}