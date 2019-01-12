package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import org.minutebots.robot.OI;

public class Drivetrain extends Subsystem {
    private Drivetrain(){
        super("Drivetrain");
        addChild(leftBackMotor);
        addChild(rightBackMotor);
        addChild(rightFrontMotor);
        addChild(leftFrontMotor);
        this.driveBase.setSafetyEnabled(false);
    }

    private static final Drivetrain drivetrain = new Drivetrain();

    public static Drivetrain getInstance(){
        return drivetrain;
    }

    private final Spark leftFrontMotor = new Spark(OI.LEFT_FRONT_MOTOR),
            rightFrontMotor = new Spark(OI.RIGHT_FRONT_MOTOR),
            leftBackMotor = new Spark(OI.LEFT_BACK_MOTOR),
            rightBackMotor = new Spark(OI.RIGHT_BACK_MOTOR);

    private final MecanumDrive driveBase = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation){
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
