package org.minutebots.robot.hardwareconfigurations

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.*
import edu.wpi.first.wpilibj.DoubleSolenoid.Value
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import org.minutebots.robot.subsystems.Drivetrain
import org.minutebots.robot.subsystems.HatchPiston

class Asimov : HardwareManger {

    private val leftFront = WPI_VictorSPX(LEFT_FRONT_MOTOR)
    private val rightFront = WPI_VictorSPX(RIGHT_FRONT_MOTOR)
    private val leftBack = WPI_VictorSPX(LEFT_BACK_MOTOR)
    private val rightBack = WPI_VictorSPX(RIGHT_BACK_MOTOR)

    private val armMotor = WPI_TalonSRX(DEPOT_ARM)
    private val wheelMotor = WPI_TalonSRX(DEPOT_WHEEL)
    private val rampMotor = WPI_TalonSRX(RAMP)

    private val upLimit = DigitalInput(UP_LIMIT_SWITCH)
    private val downLimit = DigitalInput(DOWN_LIMIT_SWITCH)

    private val piston = DoubleSolenoid(PISTON_1, PISTON_2)
    private val activeHatch = DoubleSolenoid(ACTIVEH_1, ACTIVEH_2)
    private val rampLock = DoubleSolenoid(RAMPLOCK_1, RAMPLOCK_2)

    private val navX = AHRS(SPI.Port.kMXP)

    private val ultraS = Ultrasonic(2, 3)


    override val angle: Double
        get() = navX.angle

    //private PowerDistributionPanel pdp = new PowerDistributionPanel();

    override fun init() {
        //Shuffleboard.getTab("Debugging").add(pdp);
        ultraS.setAutomaticMode(true)
        val drivetrainInfo = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain", BuiltInLayouts.kList)
        drivetrainInfo.add("Left Front DM 2", leftFront)
        drivetrainInfo.add("Right Front DM 0", rightFront)
        drivetrainInfo.add("Left Back DM 1", leftBack)
        drivetrainInfo.add("Right Back DM 3", rightBack)
        drivetrainInfo.add("PID Controller", Drivetrain.instance.getPIDController())


        val drivetrainCommands = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain Commands", BuiltInLayouts.kList).withProperties(Map.of<String, Any>("Label position", "HIDDEN"))
        drivetrainCommands.add("Current Commands", Drivetrain.instance)
        drivetrainCommands.add("Remove Current Command", InstantCommand { Drivetrain.instance.getCurrentCommand().cancel() })
        drivetrainCommands.add(InstantCommand("Set Setpoint 0") { Drivetrain.instance.setSetpoint(0.0) })
        drivetrainCommands.add(InstantCommand("Set Setpoint 90") { Drivetrain.instance.setSetpoint(90.0) })
        drivetrainCommands.add(InstantCommand("Set Setpoint 180") { Drivetrain.instance.setSetpoint(180.0) })
        drivetrainCommands.add(InstantCommand("Set Setpoint 270") { Drivetrain.instance.setSetpoint(270.0) })

        val depotArm = Shuffleboard.getTab("Debugging")
                .getLayout("Depot Arm", BuiltInLayouts.kList)
        depotArm.add("Depot Arm", armMotor)
        depotArm.add("Depot Wheel", wheelMotor)
        depotArm.add("Arm Up", upLimit)
        depotArm.add("Arm Down", downLimit)

        val intake = Shuffleboard.getTab("Debugging")
                .getLayout("Intake", BuiltInLayouts.kList).withProperties(Map.of<String, Any>("Label position", "HIDDEN"))
        intake.add("Piston", piston)
        intake.add("Ultrasonic", ultraS)
        intake.add(HatchPiston.extend())
        intake.add(HatchPiston.retract())
        intake.add(HatchPiston.eject())
        intake.add(HatchPiston.grabHatch())

        Shuffleboard.getTab("Debugging").add("Ramp Motor", rampMotor)
    }

    override fun drivetrainMotors(): Array<SpeedController> {
        return arrayOf(leftFront, leftBack, rightFront, rightBack)
    }

    override fun resetGyro() {
        navX.reset()
    }

    override fun rampMotor(): SpeedController {
        return rampMotor
    }

    override fun depotArm(): SpeedController {
        return armMotor
    }

    override fun depotRoller(): SpeedController {
        return wheelMotor
    }

    override fun extendIntakePiston() {
        piston.set(DoubleSolenoid.Value.kForward) //Yes, this value are intentional. The solenoid is backwards.
    }

    override fun retractIntakePiston() {
        piston.set(DoubleSolenoid.Value.kReverse) //Yes, this value are intentional. The solenoid is backwards.
    }

    override fun closeSolenoids() {
        piston.set(DoubleSolenoid.Value.kOff)
        activeHatch.set(Value.kOff)
        rampLock.set(Value.kOff)
    }

    override fun extendActiveHatch() {
        activeHatch.set(DoubleSolenoid.Value.kForward)
    }

    override fun retractActiveHatch() {
        activeHatch.set(DoubleSolenoid.Value.kReverse)

    }

    override fun extendRampLock() {
        rampLock.set(Value.kForward)
    }

    override fun retractRampLock() {
        rampLock.set(Value.kReverse)
    }


    /**
     * @return The distance from the ultrasonic sensor in inches.
     */
    override fun ultraSonicDist(): Double {
        return ultraS.rangeInches
    }

    override fun armUp(): Boolean {
        return !upLimit.get()
    }

    override fun armDown(): Boolean {
        return !downLimit.get()
    }

    companion object {
        //Victor
        internal val LEFT_FRONT_MOTOR = 0
        internal val LEFT_BACK_MOTOR = 3
        internal val RIGHT_FRONT_MOTOR = 1
        internal val RIGHT_BACK_MOTOR = 2
        //Pneumatic Ports
        internal val PISTON_1 = 6
        internal val PISTON_2 = 7
        internal val ACTIVEH_1 = 4
        internal val ACTIVEH_2 = 5
        internal val RAMPLOCK_1 = 3
        internal val RAMPLOCK_2 = 2

        //Talons Ports
        internal val DEPOT_ARM = 4
        internal val DEPOT_WHEEL = 5
        internal val RAMP = 6

        internal val UP_LIMIT_SWITCH = 0
        internal val DOWN_LIMIT_SWITCH = 1
    }
}
