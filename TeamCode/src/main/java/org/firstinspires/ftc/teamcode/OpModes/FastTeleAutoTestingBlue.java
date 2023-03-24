package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.Utils.Team;

@Autonomous
public class FastTeleAutoTestingBlue extends FastTeleAutoTesting{
    @Override
    public Team getTeam() {
        return Team.BLUE;
    }
}