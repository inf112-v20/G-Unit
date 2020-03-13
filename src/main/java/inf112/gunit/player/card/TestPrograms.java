package inf112.gunit.player.card;

import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;

public class TestPrograms {

    public static ProgramCard[] program1 = {
        new MovementCard(100, 2),
        new RotationCard(50, 1, true),
        new MovementCard(200, 2),
        new RotationCard(300, 1, false),
        new MovementCard(200, 1)
    };

    public static ProgramCard[] program2 = {
        new RotationCard(200, 1, true),
        new MovementCard(50, 1),
        new MovementCard(150, 1),
        new RotationCard(100, 1, false),
        new MovementCard(10, 1)
    };

    public static ProgramCard[] program3 = {
        new MovementCard(200, 1),
        new MovementCard(100, 1),
        new RotationCard(300, 2, true),
        new RotationCard(100, 1, false),
        new MovementCard(200, 2)
    };

    public static ProgramCard[] program4 = {
        new RotationCard(50, 1, false),
        new MovementCard(400, 1),
        new RotationCard(500, 2, true),
        new MovementCard(50, 1),
        new RotationCard(350, 1, false)
    };

    public static ProgramCard[] defaultProgram = {
        new MovementCard(100, 1),
        new MovementCard(100, 1),
        new MovementCard(100, 1),
        new MovementCard(100, 1),
        new RotationCard(100, 1, true)
    };

    public static ProgramCard[] getProgram(int id) {
        switch (id) {
            case 0:
                return program1;
            case 1:
                return program2;
            case 2:
                return program3;
            case 3:
                return program4;
            default:
                return defaultProgram;
        }

    }

}
