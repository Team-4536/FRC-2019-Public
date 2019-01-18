package org.minutebots.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.minutebots.robot.OI;

public class Drivetrain extends PIDSubsystem {
    private double turnThrottle = 0;
    private boolean backupDrive = false;
    private double angleAdjustment = 0.0;

    private Drivetrain() {
        super("Drivetrain", 0.01, 0, 0.01);
        addChild(leftBackMotor);
        addChild(rightBackMotor);
        addChild(rightFrontMotor);
        addChild(leftFrontMotor);
        SmartDashboard.putData(driveBase);
        SmartDashboard.putData(this);
        SmartDashboard.putData(getPIDController());
        getPIDController().setInputRange(0, 360);
        getPIDController().setContinuous(true);
        setOutputRange(-0.8, 0.8);
    }

    @Override
    public void periodic() {
        if (backupDrive) {
            mecanumDrive(OI.primaryStick.getX(), -OI.primaryStick.getY(), (OI.trigger.get()) ? OI.primaryStick.getTwist() : 0);
            return; //Makes sure that the gyroscope code doesn't run.
        }
        if (this.getCurrentCommand() == null) {
            if (OI.primaryStick.getPOV() != -1) setSetpoint(OI.primaryStick.getPOV());
            mecanumDrive(OI.primaryStick.getX(), -OI.primaryStick.getY(), turnThrottle, getAngle());
        }
    }

    private static final Drivetrain drivetrain = new Drivetrain();

    public static Drivetrain getInstance() {
        return drivetrain;
    }

    private final Spark leftFrontMotor = new Spark(OI.LEFT_FRONT_MOTOR),
            rightFrontMotor = new Spark(OI.RIGHT_FRONT_MOTOR),
            leftBackMotor = new Spark(OI.LEFT_BACK_MOTOR),
            rightBackMotor = new Spark(OI.RIGHT_BACK_MOTOR);
    private final AHRS navX = new AHRS(SPI.Port.kMXP);
    private final MecanumDrive driveBase = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation);
    }

    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, gyroAngle);
    }

    public double getAngle() {
        return navX.getAngle() + angleAdjustment;
    }

    public void resetGyro() {
        navX.reset();
angleAdjustment = 0;
    }

    public void adjustAngle(double angle){
        angleAdjustment += angle;
    }

    public double getYaw() {
        return navX.getYaw() + angleAdjustment;
    }

    public void emergencyDrive(){
        backupDrive = true;
        disable();
        getPIDController().close(); //Sets all variables to null to free up memory
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    protected double returnPIDInput() {
        return Drivetrain.getInstance().getYaw();
    }

    @Override
    protected void usePIDOutput(double output) {
        this.turnThrottle = output;
    }
}
