package org.firstinspires.ftc.teamcode.Robot.Subsystems.ScoringMechanism;

import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.CommandFramework.Subsystem;
import org.firstinspires.ftc.teamcode.Math.AsymmetricProfile.MotionConstraint;
import org.firstinspires.ftc.teamcode.Utils.ProfiledPID;

public class VerticalExtension extends Subsystem {

	MainScoringMechanism.MechanismStates state = MainScoringMechanism.MechanismStates.BEGIN;

	DcMotorEx vertical1;
	DcMotorEx vertical2;

	PIDCoefficients coefficients = new PIDCoefficients(0.01,0,0);
	MotionConstraint upConstraint = new MotionConstraint(5000,5000,2000);
	MotionConstraint downConstraint = new MotionConstraint(5000,5000,2000);

	ProfiledPID controller = new ProfiledPID(upConstraint,downConstraint,coefficients);
	public final static double HIGH_POSITION = 833;


	protected double slideTargetPosition = 0;


	public void commonInit(HardwareMap hwMap) {
		vertical1 = hwMap.get(DcMotorEx.class, "vertical1");
		vertical2 = hwMap.get(DcMotorEx.class, "vertical2");
		// TODO, set direction
		vertical2.setDirection(DcMotorSimple.Direction.REVERSE);
		vertical1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		vertical2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	}
	@Override
	public void initAuto(HardwareMap hwMap) {
		commonInit(hwMap);
		vertical1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		vertical2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		vertical1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		vertical2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	}
	@Override
	public void initTeleop(HardwareMap hwMap) {
		commonInit(hwMap);
		vertical1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		vertical2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	}

	@Override
	public void periodic() {

		updatePID();
	}

	@Override
	public void shutdown() {

	}

	public void setState(MainScoringMechanism.MechanismStates state) {
		this.state = state;
		updateTargetPosition();
	}

	protected void updatePID() {
		double measuredPosition = getSlidePosition();
		double power = controller.calculate(slideTargetPosition,measuredPosition);
		vertical1.setPower(power);
		vertical2.setPower(power);
	}

	/**
	 * get position of the linear slides
	 * @return average encoder position of the slides
	 */
	public double getSlidePosition() {
		return (vertical1.getCurrentPosition() + vertical2.getCurrentPosition()) / 2.0;
	}

	public double getSlideTargetPosition() {
		return slideTargetPosition;
	}

	public void updateTargetPosition() {
		switch (this.state) {
			case BEGIN:
				slideTargetPosition = 0.0;
				break;
			case LOW:
				// TODO: Find the correct position for this
				slideTargetPosition = 0.0;
				break;
			case MID:
				// TODO: Find the correct position for this
				slideTargetPosition = 0.0;
				break;
			case HIGH:
				// TODO: Find the correct position for this
				slideTargetPosition = HIGH_POSITION;
				break;
			case GO_TO_LOW:
				// TODO: Find the correct position for this
				slideTargetPosition = 0.0;
				break;
			case TransferCone:
				// TODO: Find the correct position for this
				slideTargetPosition = 0.0;
				break;
		}
	}
}
