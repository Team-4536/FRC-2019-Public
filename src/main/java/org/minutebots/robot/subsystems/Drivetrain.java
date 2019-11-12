package org.minutebots.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import org.minutebots.lib.Utilities;
import org.minutebots.robot.OI;
import org.minutebots.robot.Robot;
import org.minutebots.robot.hardwareconfigurations.Asimov;
import org.minutebots.robot.utilities.Constants;
import org.minutebots.robot.utilities.VisionCommunication;

public class Drivetrain extends PIDSubsystem {
    private final MecanumDrive driveBase;
    private double pidOutput = 0, angleAdjustment = 0;

    private Drivetrain() {
        super("Drivetrain", 0.035, 0.0025, 0.1);
        driveBase = new MecanumDrive(Robot.hardwareManager.drivetrainMotors()[0],
                Robot.hardwareManager.drivetrainMotors()[1],
                Robot.hardwareManager.drivetrainMotors()[2],
                Robot.hardwareManager.drivetrainMotors()[3]);
        setInputRange(-180, 180);
        getPIDController().setContinuous(true);
        setOutputRange(-Constants.OPEN_LOOP_MAX_TURN, Constants.OPEN_LOOP_MAX_TURN);

        ShuffleboardTab drive = Shuffleboard.getTab("Drive");
        drive.add("Drivetrain", driveBase);

        drive.add("Toggle Gyro", new InstantCommand(() -> {
            if (getPIDController().isEnabled()) disable();
            else {
                enable();
                resetGyro();
            }
        }));
    }

    /**
     * Runs the default teleop loop every 20ms. Does not run when a command requires the drivetrain.
     */
    @Override
    public void periodic() {
        if (getCurrentCommand() == null) {
            //TODO: add snap angle
            //else if(OI.primaryStick.getMagnitude() > 0.85 && !OI.trigger.get()) setSetpoint(
                    //Math.abs(getYaw() - OI.primaryStick.getDirectionDegrees()) > 110 ? OI.primaryStick.getDirectionDegrees()+180 : OI.primaryStick.getDirectionDegrees());

            double forwardThrottle=-OI.xController.getY(Hand.kLeft),
            strafeThrottle=OI.xController.getX(Hand.kLeft);

            double turnThrottle=speedCurve(OI.xController.getX(Hand.kRight));
            if(OI.xController.getPOV() != -1){
                setSetpoint(OI.xController.getPOV());
                turnThrottle = getPidOutput();
            } 
            else if (OI.xController.getBumper(Hand.kRight)){
                turnThrottle *= Constants.VISION_SCALAR;
                strafeThrottle *= Constants.VISION_SCALAR;
                turnThrottle += VisionCommunication.getInstance().getAngle() * Constants.VISION_ROTATE_P;
                strafeThrottle += Constants.VISION_CSTRAFE_P * (getAngle()-(getNearestSquare()));
            }    
            /*if(OI.trigger.get()) if(OI.primaryStick.getMagnitude() > 0.3) turnThrottle = 
            Utilities.limit(Utilities.angleDifference(getAngle(), 
            Math.abs(getYaw() - OI.primaryStick.getDirectionDegrees()) > 90 ?
             OI.primaryStick.getDirectionDegrees()+180 :
             OI.primaryStick.getDirectionDegrees())*0.02, 0.5);*/

            /*if(OI.strafe.get()) {
                strafeThrottle = Constants.VISION_STRAFE_P * VisionCommunication.getInstance().getAngle() + OI.secondaryStick.getX()*0.3;
                //forwardThrottle = -OI.secondaryStick.getY();
            }*/

            /*if(OI.visionRotate.get()){
                turnThrottle = VisionCommunication.getInstance().getAngle() * Constants.VISION_ROTATE_P + OI.secondaryStick.getX()*0.3;
                //forwardThrottle = -OI.secondaryStick.getY();
            }*/

            /*if(OI.defenseTurn.get()){
                //turnThrottle = OI.secondaryStick.getX()*Constants.DEFENSE_TURN_SPEED; 
            }*/
            
/*
            if (!getPIDController().isEnabled()) { //Run this if the PID controller is disabled. This is drive code without the gyroscope.
                turnThrottle = OI.xController.getX(Hand.kRight)*Constants.CLOSED_LOOP_MAX_TURN;
                 ////Makes sure that the gyroscope code doesn't run.
            }
            */
            
            //if (OI.visionRotate.get()) setSetpoint(getYaw() + VisionCommunication.getInstance().getAngle());
            //if(Robot.isAuto){
                //turnThrottle = OI.trigger.get() ? OI.primaryStick.getTwist() * Constants.MANUAL_TURN_SPEED : 0;

            mecanumDrive(speedCurve(strafeThrottle),speedCurve(forwardThrottle),turnThrottle,!OI.xController.getBumper(Hand.kRight));
            //mecanumDrive(OI.strafe.get() ? Constants.VISION_STRAFE_P * VisionCommunication.getInstance().getAngle() : OI.primaryStick.getX(),
                  //  OI.strafe.get() ? -OI.secondaryStick.getY() : -OI.primaryStick.getY(),
                   // OI.fineTurn.get() ? OI.secondaryStick.getX()*0.5 : turnThrottle, !(OI.strafe.get() || Robot.isAuto));

            //rocketMode.setBoolean(OI.secondaryStick.getThrottle() < 0);
            
        }
    }

    private static double speedCurve(double input){
        return Math.copySign(Math.pow(input, 2), input);
    }
    /**
     * Singleton drivetrain instance.
     */
    private static Drivetrain driveTrain = new Drivetrain();

    /**
     * Returns the singleton Drivetrain instance.
     * In order to interact with the Drivetrain methods, you must call them from this instance.
     */
    public static Drivetrain getInstance() {
        return driveTrain;
    }

    /**
     * Mecanum drive method.
     * <p>
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     * @param ySpeed    The robot's speed strafing sideways. [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed driving forwards. [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate. [-1.0..1.0]. Clockwise is
     *                  positive.
     * @param fieldCentric If the robot will have controls based on the field.
     */
    public void mecanumDrive(double ySpeed, double xSpeed, double zRotation, boolean fieldCentric) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, fieldCentric ? -getAngle() : 0.0);
    }

    /**
     * Robot-centric Mecanum drive method.
     * <p>
     * This method does not rotate the robot's perspective to the field, so all inputs are relative to robot forwards.
     * <p>
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
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
     * <p>
     * This method rotates the robot's perspective to the field, so all inputs are relative to 0 degrees of the gyro.
     * Usually, this is the angle you start the robot at.
     * <p>
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     * @param ySpeed    The robot's speed strafing sideways. [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed driving forwards. [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate. [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    public void mecanumDriveFC(double ySpeed, double xSpeed, double zRotation) {
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
        Robot.hardwareManager.resetGyro();
        angleAdjustment = 0;
        setSetpoint(0);
    }

    /**
     * Redefines the field's rotation for field-centric driving.
     * <p>
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
    public double getAngle() {
        return Robot.hardwareManager.getAngle() + angleAdjustment;
    }

    /**
     * Gets the gyro angle. The return value is bounded inside the domain [-180..180] degrees.
     */
    public double getYaw() {
        return Utilities.angleConverter(getAngle());
    }

    public double getNearestSquare(){
        if(getYaw() < -135) return -180;
        else if(getYaw() < -45) return -90;
        else if(getYaw() < 45) return 0;
        else if(getYaw() < 135) return 90;
        else return 180;
    }

    public double getNearestRocket() {
    	if(getYaw() < -120.625) return -151.25;
	else if(getYaw() < -59.375) return -90;
	else if(getYaw() < 0) return -28.75;
	else if(getYaw() < 59.375) return 28.75;
	else if(getYaw() < 120.625) return 90;
	else return 151.25;
    }

    public NetworkTableEntry rocketMode = Shuffleboard.getTab("Drive")
            .add("Rocketmode", false)
            .withWidget("Toggle Button")
            .getEntry();


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
    public double getPidOutput() {
        return pidOutput;
    }

    /**
     * Writes the output of the internal PID controller to a variable.
     */
    @Override
    protected void usePIDOutput(double output) {
        pidOutput = output;
    }
}
