package org.minutebots.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.minutebots.robot.OI;

public class Drivetrain extends Subsystem {
    private Drivetrain(){
        super("Drivetrain");
        addChild(leftBackMotor);
        addChild(rightBackMotor);
        addChild(rightFrontMotor);
        addChild(leftFrontMotor);
        this.driveBase.setSafetyEnabled(false);
        this.driveBase.setDeadband(0.13);
        SmartDashboard.putData(driveBase);
        SmartDashboard.putData(this);
    }

    private static final Drivetrain drivetrain = new Drivetrain();

    public static Drivetrain getInstance(){
        return drivetrain;
    }

    private final Spark leftFrontMotor = new Spark(OI.LEFT_FRONT_MOTOR),
            rightFrontMotor = new Spark(OI.RIGHT_FRONT_MOTOR),
            leftBackMotor = new Spark(OI.LEFT_BACK_MOTOR),
            rightBackMotor = new Spark(OI.RIGHT_BACK_MOTOR);

    private final AHRS navX = new AHRS(SerialPort.Port.kMXP);

    private final MecanumDrive driveBase = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation){
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation);
    }

    public double getAngle(){
        return navX.getAngle();
    }

    public void resetGyro(){
        navX.reset();
    }

    public double getYaw(){
        return navX.getYaw();
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void periodic(){
        if(this.getCurrentCommand() != null){
            double turnThrottle = 0;



            this.mecanumDrive(OI.primaryStick.getX(), -OI.primaryStick.getY(), turnThrottle);
        }
    }
}
