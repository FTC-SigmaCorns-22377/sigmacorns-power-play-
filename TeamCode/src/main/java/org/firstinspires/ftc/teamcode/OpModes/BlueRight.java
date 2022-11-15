package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CommandFramework.BaseAuto;
import org.firstinspires.ftc.teamcode.CommandFramework.Command;
import org.firstinspires.ftc.teamcode.CommandFramework.CommandScheduler;
import org.firstinspires.ftc.teamcode.RR_quickstart.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.RR_quickstart.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Robot.Commands.DrivetrainCommands.AlignWithVision2Auto;
import org.firstinspires.ftc.teamcode.Robot.Commands.DrivetrainCommands.UltimateGoalMoment.FollowPath;
import org.firstinspires.ftc.teamcode.Robot.Commands.MiscCommands.Delay;
import org.firstinspires.ftc.teamcode.Robot.Commands.MiscCommands.MultipleCommand;
import org.firstinspires.ftc.teamcode.Robot.Commands.ScoringSubsystem.ActivateIntakeAuto;
import org.firstinspires.ftc.teamcode.Robot.Commands.ScoringSubsystem.DepositAuto;
import org.firstinspires.ftc.teamcode.Robot.Commands.ScoringSubsystem.GoToScore;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.ScoringMechanism;

@Autonomous
public class BlueRight extends BaseAuto {

	public final Pose2d initialPose = new Pose2d( -37.5, 63.5, Math.toRadians(-90));
	@Override
	public Command setupAuto(CommandScheduler scheduler) {

		Pose2d placeCone = new Pose2d( -36.60741466514628, 11, Math.toRadians(308.06138282915236));
		Pose2d placeCone2 = new Pose2d( placeCone.getX() + 2, placeCone.getY() + 0.5, placeCone.getHeading());

		Pose2d pickupFull = new Pose2d(-62,14.5,Math.toRadians(0));
		Pose2d pickupPartial = new Pose2d(-48,pickupFull.getY() + 3,Math.toRadians(0));


		Trajectory goToConePlacingFirst = robot.drivetrain.getBuilder().trajectoryBuilder(initialPose)
				.lineToLinearHeading(placeCone)
				.build();
		TrajectoryVelocityConstraint slowConstraint = SampleMecanumDrive.getVelocityConstraint(DriveConstants.MAX_VEL / 2, DriveConstants.MAX_ANG_VEL/2,DriveConstants.TRACK_WIDTH);
		TrajectoryAccelerationConstraint slowConstraintAccel = SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL / 3);

		Trajectory pickupConeFIRST = robot.drivetrain.getBuilder().trajectoryBuilder(placeCone,true)
				.splineTo(pickupPartial.vec(),Math.toRadians(180))
				.splineToConstantHeading(pickupFull.vec(),Math.toRadians(180))
				.build();
		Trajectory pickupCone = robot.drivetrain.getBuilder().trajectoryBuilder(placeCone2,true)
				.splineTo(pickupPartial.vec(),Math.toRadians(180),slowConstraint,slowConstraintAccel)
				.splineToConstantHeading(pickupFull.vec(),Math.toRadians(180),slowConstraint,slowConstraintAccel)
				.build();

		Trajectory placeConeTrajectory = robot.drivetrain.getBuilder().trajectoryBuilder(pickupFull)
				.splineTo(pickupPartial.vec(),Math.toRadians(0),slowConstraint,slowConstraintAccel)
				.splineTo(placeCone2.vec(), placeCone2.getHeading(),slowConstraint,slowConstraintAccel)
				.build();


		double depositDelayS = 0.5;



		return followRR(goToConePlacingFirst)
				.addNext(new GoToScore(robot.scoringMechanism, ScoringMechanism.States.HIGH))
				.addNext(new DepositAuto(robot.scoringMechanism))
				.addNext(new Delay(depositDelayS))
				.addNext(followRR(pickupConeFIRST))
				.addNext(new ActivateIntakeAuto(robot.scoringMechanism))
				.addNext(followRR(placeConeTrajectory))
				.addNext(new GoToScore(robot.scoringMechanism, ScoringMechanism.States.HIGH))
				//.addNext(new MultipleCommand(new GoToScore(robot.scoringMechanism, ScoringMechanism.States.HIGH), followRR(placeConeTrajectory)))
				.addNext(new DepositAuto(robot.scoringMechanism))
				.addNext(new Delay(depositDelayS))
				.addNext(followRR(pickupCone));
//				.addNext(new GoToScore(robot.scoringMechanism, ScoringMechanism.States.HIGH))
//				//.addNext(new AlignWithVision2Auto(robot.drivetrain, robot.distanceSensor))
//				.addNext(new DepositAuto(robot.scoringMechanism))
//				.addNext(new Delay(depositDelayS))
//				// cone 2
//				.addNext(followPath(pickupPartial,pickupFull))
//				.addNext(new ActivateIntakeAuto(robot.scoringMechanism))
//				.addNext(new MultipleCommand(new GoToScore(robot.scoringMechanism, ScoringMechanism.States.HIGH), followRR(placeConeTrajectory)))
////				.addNext(goToLQR(goNearScoring))
////				.addNext(new MultipleCommand(new GoToScore(robot.scoringMechanism, ScoringMechanism.States.HIGH), goToLQR(placeCone2)))
//				.addNext(new DepositAuto(robot.scoringMechanism))
//				.addNext(new Delay(depositDelayS));



	}

	@Override
	public void setRobotPosition() {
		robot.drivetrain.setPose(initialPose);
	}
}
