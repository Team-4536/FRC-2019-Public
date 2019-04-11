package org.minutebots.robot.hardwareconfigurations

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.Spark
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.shuffleboard.*
import org.minutebots.lib.VirtualMotor
import org.minutebots.robot.subsystems.Drivetrain

class Fracture : HardwareManger {

    private val navX = AHRS(SPI.Port.kMXP)
    private val ultraS = AnalogInput(0)

    private val piston = Shuffleboard.getTab("Virtual Motors")
            .add("Intake Piston", false).entry
    private val hatchm = Shuffleboard.getTab("Virtual Motors")
            .add("Active Hatch", false).entry
    private val topLimit = Shuffleboard.getTab("Virtual Motors")
            .add("Top Limit Switch", false)
            .withWidget(BuiltInWidgets.kToggleButton)
            .entry
    private val bottomLimit = Shuffleboard.getTab("Virtual Motors")
            .add("Bottom Limit Switch", false)
            .withWidget(BuiltInWidgets.kToggleButton)
            .entry

    private val driveTrainMotors = arrayOf<SpeedController>(Spark(2), Spark(1), Spark(0), Spark(3))

    private val arm = VirtualMotor(Asimov.DEPOT_ARM)
    private val roller = VirtualMotor(Asimov.DEPOT_WHEEL)
    private val ramp = VirtualMotor(Asimov.RAMP)


    override val angle: Double
        get() = navX.angle

    override fun drivetrainMotors(): Array<SpeedController> {
        return driveTrainMotors
    }

    override fun resetGyro() {
        navX.reset()
    }

    override fun depotArm(): SpeedController {
        return arm
    }

    override fun depotRoller(): SpeedController {
        return roller
    }

    override fun rampMotor(): SpeedController {
        return ramp
    }

    override fun extendIntakePiston() {
        piston.setBoolean(true)
    }

    override fun retractIntakePiston() {
        piston.setBoolean(false)
    }

    override fun closeSolenoids() {
        piston.setBoolean(false)
    }

    /**
     * @return The distance from the ultrasonic sensor in inches.
     */
    override fun ultraSonicDist(): Double {
        return ultraS.voltage * 103.481 + 1.64105 //106.007 for one value
    }

    override fun armUp(): Boolean {
        return topLimit.getBoolean(false)
    }

    override fun armDown(): Boolean {
        return bottomLimit.getBoolean(false)
    }

    override fun extendActiveHatch() {
        hatchm.setBoolean(true)
    }

    override fun retractActiveHatch() {
        hatchm.setBoolean(false)
    }

    override fun extendRampLock() {

    }

    override fun retractRampLock() {

    }


    override fun init() {
        val debug = Shuffleboard.getTab("Debugging")
        debug.add("Ultrasonic", ultraS)

        val drivetrainCommands = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain Commands", BuiltInLayouts.kList).withProperties(Map.of<String, Any>("Label position", "HIDDEN"))
        drivetrainCommands.add("Current Commands", Drivetrain.instance)
        drivetrainCommands.add("Remove Current Command", InstantCommand { Drivetrain.instance.getCurrentCommand().cancel() })
        drivetrainCommands.add(InstantCommand("Set Setpoint 0") { Drivetrain.instance.setSetpoint(0.0) })
        drivetrainCommands.add(InstantCommand("Set Setpoint 90") { Drivetrain.instance.setSetpoint(90.0) })
        drivetrainCommands.add(InstantCommand("Set Setpoint 180") { Drivetrain.instance.setSetpoint(180.0) })
        drivetrainCommands.add(InstantCommand("Set Setpoint 270") { Drivetrain.instance.setSetpoint(270.0) })
    }
}
