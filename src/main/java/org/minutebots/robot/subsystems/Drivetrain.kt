package org.minutebots.robot.subsystems

import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.command.PIDSubsystem
import edu.wpi.first.wpilibj.drive.MecanumDrive
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import org.minutebots.lib.Utilities
import org.minutebots.robot.OI
import org.minutebots.robot.Robot
import org.minutebots.robot.utilities.Constants
import org.minutebots.robot.utilities.VisionCommunication

class Drivetrain private constructor() : PIDSubsystem("Drivetrain", 0.035, 0.0025, 0.1) {
    private val driveBase: MecanumDrive
    /**
     * Returns the output of the internal PID controller.
     */
    var pidOutput = 0.0
        private set
    private var angleAdjustment = 0.0

    /**
     * Gets the gyro angle. The return value is unbounded, so it can be greater than the domain [-180..180].
     * This is the total sum of rotation.
     */
    val angle: Double
        get() = Robot.hardwareManager.angle + angleAdjustment

    /**
     * Gets the gyro angle. The return value is bounded inside the domain [-180..180] degrees.
     */
    val yaw: Double
        get() = Utilities.angleConverter(angle)

    val nearestSquare: Double
        get() = if (yaw < -135)
            -180.0
        else if (yaw < -45)
            -90.0
        else if (yaw < 45)
            0.0
        else if (yaw < 135)
            90.0
        else
            180.0

    val nearestRocket: Double
        get() = if (yaw < -120.625)
            -151.25
        else if (yaw < -59.375)
            -90.0
        else if (yaw < 0)
            -28.75
        else if (yaw < 59.375)
            28.75
        else if (yaw < 120.625)
            90.0
        else
            151.25

    var rocketMode = Shuffleboard.getTab("Drive")
            .add("Rocketmode", false)
            .withWidget("Toggle Button")
            .entry

    init {
        driveBase = MecanumDrive(Robot.hardwareManager.drivetrainMotors()[0],
                Robot.hardwareManager.drivetrainMotors()[1],
                Robot.hardwareManager.drivetrainMotors()[2],
                Robot.hardwareManager.drivetrainMotors()[3])
        setInputRange(-180.0, 180.0)
        pidController.setContinuous(true)
        setOutputRange(-Constants.OPEN_LOOP_MAX_TURN, Constants.OPEN_LOOP_MAX_TURN)

        val drive = Shuffleboard.getTab("Drive")
        drive.add("Drivetrain", driveBase)

        drive.add("Toggle Gyro", InstantCommand {
            if (pidController.isEnabled)
                disable()
            else {
                enable()
                resetGyro()
            }
        })
    }

    /**
     * Runs the default teleop loop every 20ms. Does not run when a command requires the drivetrain.
     */
    override fun periodic() {
        if (currentCommand == null) {
            if (OI.primaryStick.pov != -1)
                setpoint = OI.primaryStick.pov.toDouble()
            else if (OI.secondaryStick.pov != -1) setpoint = OI.secondaryStick.pov.toDouble()
            //else if(OI.primaryStick.getMagnitude() > 0.85 && !OI.trigger.get()) setSetpoint(
            //Math.abs(getYaw() - OI.primaryStick.getDirectionDegrees()) > 110 ? OI.primaryStick.getDirectionDegrees()+180 : OI.primaryStick.getDirectionDegrees());

            var turnThrottle = pidOutput
            var forwardThrottle = -OI.primaryStick.y
            var strafeThrottle = OI.primaryStick.x

            if (OI.trigger.get())
                if (OI.primaryStick.magnitude > 0.3)
                    turnThrottle = Utilities.limit(Utilities.angleDifference(angle,
                            if (Math.abs(yaw - OI.primaryStick.directionDegrees) > 90)
                                OI.primaryStick.directionDegrees + 180
                            else
                                OI.primaryStick.directionDegrees) * 0.02, 0.5)

            if (OI.strafe.get()) {
                strafeThrottle = Constants.VISION_STRAFE_P * VisionCommunication.instance.angle + OI.secondaryStick.x * 0.3
                forwardThrottle = -OI.secondaryStick.y
            }

            if (OI.visionRotate.get()) {
                turnThrottle = VisionCommunication.instance.angle * Constants.VISION_ROTATE_P + OI.secondaryStick.x * 0.3
                forwardThrottle = -OI.secondaryStick.y
            }

            if (OI.fineTurn.get()) {
                turnThrottle = OI.secondaryStick.x * Constants.FINE_TURN_SPEED
            }

            if (OI.defenseTurn.get()) {
                turnThrottle = OI.secondaryStick.x * Constants.DEFENSE_TURN_SPEED
            }

            if (!pidController.isEnabled) { //Run this if the PID controller is disabled. This is drive code without the gyroscope.
                if (OI.trigger.get()) turnThrottle = OI.primaryStick.twist * Constants.CLOSED_LOOP_MAX_TURN
                ////Makes sure that the gyroscope code doesn't run.
            }

            //if (OI.visionRotate.get()) setSetpoint(getYaw() + VisionCommunication.getInstance().getAngle());
            //if(Robot.isAuto){
            //turnThrottle = OI.trigger.get() ? OI.primaryStick.getTwist() * Constants.MANUAL_TURN_SPEED : 0;

            mecanumDrive(strafeThrottle, forwardThrottle, turnThrottle, !(OI.visionRotate.get() or OI.strafe.get() or !pidController.isEnabled))
            //mecanumDrive(OI.strafe.get() ? Constants.VISION_STRAFE_P * VisionCommunication.getInstance().getAngle() : OI.primaryStick.getX(),
            //  OI.strafe.get() ? -OI.secondaryStick.getY() : -OI.primaryStick.getY(),
            // OI.fineTurn.get() ? OI.secondaryStick.getX()*0.5 : turnThrottle, !(OI.strafe.get() || Robot.isAuto));

            rocketMode.setBoolean(OI.secondaryStick.throttle < 0)

        }
    }

    /**
     * Mecanum drive method.
     *
     *
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     * @param ySpeed    The robot's speed strafing sideways. [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed driving forwards. [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate. [-1.0..1.0]. Clockwise is
     * positive.
     * @param fieldCentric If the robot will have controls based on the field.
     */
    fun mecanumDrive(ySpeed: Double, xSpeed: Double, zRotation: Double, fieldCentric: Boolean) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, if (fieldCentric) -angle else 0.0)
    }

    /**
     * Robot-centric Mecanum drive method.
     *
     *
     * This method does not rotate the robot's perspective to the field, so all inputs are relative to robot forwards.
     *
     *
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     * @param ySpeed    The robot's speed strafing sideways. [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed driving forwards. [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate. [-1.0..1.0]. Clockwise is
     * positive.
     */
    fun mecanumDrive(ySpeed: Double, xSpeed: Double, zRotation: Double) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, 0.0)
    }

    /**
     * Field-Centric Mecanum drive method.
     *
     *
     * This method rotates the robot's perspective to the field, so all inputs are relative to 0 degrees of the gyro.
     * Usually, this is the angle you start the robot at.
     *
     *
     * Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     * @param ySpeed    The robot's speed strafing sideways. [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed driving forwards. [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate. [-1.0..1.0]. Clockwise is
     * positive.
     */
    fun mecanumDriveFC(ySpeed: Double, xSpeed: Double, zRotation: Double) {
        driveBase.driveCartesian(ySpeed, xSpeed, zRotation, -angle)
    }

    /**
     * Sets the setpoint of the Drivetrain's internal PID controller.
     * The drivetrain will rotate to the setpoint angle if the gyro is enabled.
     *
     * @param setpoint the new setpoint
     */
    override fun setSetpoint(setpoint: Double) {
        super.setSetpoint(Utilities.angleConverter(setpoint))
    }


    override fun initDefaultCommand() {}

    /**
     * Resets gyro angle and angle adjustment to 0.
     */
    fun resetGyro() {
        Robot.hardwareManager.resetGyro()
        angleAdjustment = 0.0
        setpoint = 0.0
    }

    /**
     * Redefines the field's rotation for field-centric driving.
     *
     *
     * This also rotates the robot to the new angle defined as 0 degrees, so the driver can see the new forwards direction.
     *
     * @param angle The amount of degrees to rotate the field left by. Positive rotates left, negative rotates right.
     */
    fun adjustAngle(angle: Double) {
        angleAdjustment += angle
        setpoint = 0.0
    }


    /**
     * Pushes the yaw angle as input into the internal PID controller.
     */
    override fun returnPIDInput(): Double {
        return yaw
    }

    /**
     * Writes the output of the internal PID controller to a variable.
     */
    override fun usePIDOutput(output: Double) {
        pidOutput = output
    }

    companion object {

        /**
         * Singleton drivetrain instance.
         */
        /**
         * Returns the singleton Drivetrain instance.
         * In order to interact with the Drivetrain methods, you must call them from this instance.
         */
        val instance = Drivetrain()
    }
}
