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
        setInputRange(-180, 180);
        getPIDController().setContinuous(true);
        setOutputRange(-0.8, 0.8);

        Shuffleboard.getTab("Virtual Motors")
                .add("Virtual Drivetrain", driveBase);

        SmartDashboard.putData("Toggle Gyro", new InstantCommand(()->{
            if(getPIDController().isEnabled()) disable();
            else enable();
        }));
        SmartDashboard.putData("Set Setpoint 0", new InstantCommand(() -> setSetpoint(0)));
        SmartDashboard.putData("Set Setpoint 90", new InstantCommand(() -> setSetpoint(90)));
        SmartDashboard.putData("Set Setpoint 180", new InstantCommand(() -> setSetpoint(180)));
        SmartDashboard.putData("Set Setpoint 270", new InstantCommand(() -> setSetpoint(270)));
        SmartDashboard.putData("Remove Current Command", new InstantCommand(() -> getCurrentCommand().cancel()));
        SmartDashboard.putBoolean("Gyro Enabled", getPIDController().isEnabled());
    }

    /**
     * Runs the default teleop loop every 20ms. Does not run when a command requires the drivetrain.
     */
    @Override
    public void periodic() {
        if (getCurrentCommand() == null) {
            if (!getPIDController().isEnabled()) {
                System.out.println("Running Gyro-Free Loop");
                mecanumDrive(OI.primaryStick.getX(), -OI.primaryStick.getY(), (OI.trigger.get()) ? OI.primaryStick.getTwist() : 0);
                return; //Makes sure that the gyroscope code doesn't run.
            }
            System.out.println("Running Gyro Loop");
            if (OI.vision.get()) setSetpoint(getYaw() + VisionCommunication.getInstance().getAngle());
            else if (OI.trigger.get()) setSetpoint(getSetpoint() + OI.primaryStick.getTwist() * 4);
            else if (OI.primaryStick.getPOV() != -1) setSetpoint(OI.primaryStick.getPOV());
            mecanumDriveFC(OI.primaryStick.getX(), -OI.primaryStick.getY(), turnThrottle);
        }
    }

    /**
     * Returns the singleton Drivetrain instance.
     * In order to interact with the Drivetrain methods, you must call them from this instance.
     */
    public static Drivetrain getInstance() {
        return Superstructure.getInstance().driveTrain;
    }

    /**
     * Robot-centric Mecanum drive method.
     *
     * This method does not rotate the robot's perspective to the field, so all inputs are relative to robot forwards.
     *
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     *
     * @param ySpeed    The robot's speed strafing sideways. [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed driving forwards. [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate. [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, 0.0);
    }

    /**
     * Field-Centric Mecanum drive method.
     *
     * This method rotates the robot's perspective to the field, so all inputs are relative to 0 degrees of the gyro.
     * Usually, this is the angle you start the robot at.
     *
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     *
     * @param ySpeed    The robot's speed strafing sideways. [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed driving forwards. [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate. [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    public void mecanumDriveFC(double ySpeed, double xSpeed, double zRotation){
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, -getAngle());
    }

    /**
     * Sets the setpoint of the Drivetrain's internal PID controller.
     * The drivetrain will rotate to the setpoint angle if the gyro is enabled.
     *
     * @param setpoint the new setpoint
     */
    @Override
    public void setSetpoint(double setpoint) {
        super.setSetpoint(Utilities.angleConverter(setpoint));
    }

    @Override
    protected void initDefaultCommand() {
    }

    /**
     * Resets gyro angle and angle adjustment to 0.
     */
    public void resetGyro() {
        navX.reset();
        angleAdjustment = 0;
    }

    /**
     * Redefines the field's rotation for field-centric driving.
     *
     * This also rotates the robot to the new angle defined as 0 degrees, so the driver can see the new forwards direction.
     *
     * @param angle The amount of degrees to rotate the field left by. Positive rotates left, negative rotates right.
     */
    public void adjustAngle(double angle) {
        angleAdjustment += angle;
        setSetpoint(0);
    }

    /**
     * Gets the gyro angle. The return value is unbounded, so it can be greater than the domain [-180..180].
     * This is the total sum of rotation.
     */
    private double getAngle() {
        return navX.getAngle() + angleAdjustment;
    }

    /**
     * Gets the gyro angle. The return value is bounded inside the domain [-180..180] degrees.
     */
    private double getYaw() {
        return Utilities.angleConverter(getAngle());
    }

    /**
     * Pushes the yaw angle as input into the internal PID controller.
     */
    @Override
    protected double returnPIDInput() {
        return getYaw();
    }

    /**
     * Returns the output of the internal PID controller.
     */
    public double getTurnThrottle() {
        return turnThrottle;
    }

    /**
     * Writes the output of the internal PID controller to a variable.
     */
    @Override
    protected void usePIDOutput(double output) {
        turnThrottle = output;
    }
}
