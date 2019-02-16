package org.minutebots.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.minutebots.lib.Utilities;
import org.minutebots.robot.OI;
import org.minutebots.robot.utilities.VisionCommunication;

public class Drivetrain extends PIDSubsystem {
    private final AHRS navX = new AHRS(SPI.Port.kMXP);
    private SpeedController leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;
    private final MecanumDrive driveBase;

    private double turnThrottle = 0, angleAdjustment = 0;
    private boolean backupDrive = false;

    private double xThrottle;
    private double yThrottle;
    private double pYThrottle;
    private double pXThrottle;
    private double pThrottle = 0;

    Drivetrain(SpeedController lf,
               SpeedController rf,
               SpeedController lb,
               SpeedController rb) {
        super("Drivetrain", 0.01, 0, 0.01);
        leftFrontMotor = lf;
        rightBackMotor = rb;
        leftBackMotor = lb;
        rightFrontMotor = rf;
        driveBase = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

        SmartDashboard.putData(this);
        SmartDashboard.putData(getPIDController());
        setInputRange(-180,180);
        getPIDController().setContinuous(true);
        setOutputRange(-0.8, 0.8);

        Shuffleboard.getTab("Virtual Motors")
                .add("Virtual Drivetrain", driveBase);

        SmartDashboard.putData("DISABLE GYRO", new InstantCommand(this::emergencyDrive));
        SmartDashboard.putData("Set Setpoint 0", new InstantCommand(() -> setSetpoint(0)));
        SmartDashboard.putData("Set Setpoint 90", new InstantCommand(() -> setSetpoint(90)));
        SmartDashboard.putData("Set Setpoint 180", new InstantCommand(() -> setSetpoint(180)));
        SmartDashboard.putData("Set Setpoint 270", new InstantCommand(() -> setSetpoint(270)));
    }


    @Override
    public void periodic() {
        xThrottle = OI.primaryStick.getX();
        yThrottle = -OI.primaryStick.getY();
        if (backupDrive) {
            mecanumDrive(Utilities.accelLimit(xThrottle, pXThrottle),Utilities.accelLimit(yThrottle, pYThrottle), (OI.trigger.get()) ? OI.primaryStick.getTwist() : 0);
            return; //Makes sure that the gyroscope code doesn't run.
        }
        if (this.getCurrentCommand() == null) {
            if (OI.vision.get()) setSetpoint(getYaw() + VisionCommunication.getInstance().getAngle());
            else if (OI.trigger.get()) setSetpoint(getSetpoint() + OI.primaryStick.getTwist() * 4);
            else if (OI.primaryStick.getPOV() != -1) setSetpoint(OI.primaryStick.getPOV());
            mecanumDrive(Utilities.accelLimit(xThrottle, pXThrottle),Utilities.accelLimit(yThrottle, pYThrottle), turnThrottle, -getAngle());
        }
        pXThrottle = xThrottle;
        pYThrottle = yThrottle;
    }

    public static Drivetrain getInstance() {
        return Superstructure.getInstance().driveTrain;
    }

    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation);
    }

    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, gyroAngle);
    }

    @Override
    public void setSetpoint(double setpoint) {
        super.setSetpoint(Utilities.angleConverter(setpoint));
    }

    public double getAngle() {
        return navX.getAngle() + angleAdjustment;
    }

    public void resetGyro() {
        navX.reset();
        angleAdjustment = 0;
    }

    public void adjustAngle(double angle) {
        angleAdjustment += angle;
        setSetpoint(0); //This is so you can see the angle you're adjusting to.
    }

    public double getYaw() {
        return Utilities.angleConverter(getAngle());
    }

    public void emergencyDrive() {
        backupDrive = true;
        disable();
    }

    @Override
    protected void initDefaultCommand() {
    }

    @Override
    protected double returnPIDInput() {
        return getYaw();
    }

    @Override
    protected void usePIDOutput(double output) {
        this.turnThrottle = output;
    }
}
