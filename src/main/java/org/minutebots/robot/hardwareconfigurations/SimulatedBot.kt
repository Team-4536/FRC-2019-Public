package org.minutebots.robot.hardwareconfigurations

import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import org.minutebots.lib.VirtualMotor

class SimulatedBot : HardwareManger {
    private val piston = Shuffleboard.getTab("Virtual Motors")
            .add("Intake Piston", false).entry
    private val hatchm = Shuffleboard.getTab("Virtual Motors")
            .add("Active Hatch", false).entry
    private val ramplock = Shuffleboard.getTab("Virtual Motors")
            .add("Ramp Lock", false).entry
    private val gyro = Shuffleboard.getTab("Virtual Motors")
            .add("Gyro Angle", 0)
            .withWidget(BuiltInWidgets.kTextView)
            .entry
    private val topLimit = Shuffleboard.getTab("Virtual Motors")
            .add("Top Limit Switch", false)
            .withWidget(BuiltInWidgets.kToggleButton)
            .entry
    private val bottomLimit = Shuffleboard.getTab("Virtual Motors")
            .add("Bottom Limit Switch", false)
            .withWidget(BuiltInWidgets.kToggleButton)
            .entry

    private val motors = arrayOf<SpeedController>(VirtualMotor(Asimov.LEFT_FRONT_MOTOR), VirtualMotor(Asimov.LEFT_BACK_MOTOR), VirtualMotor(Asimov.RIGHT_FRONT_MOTOR), VirtualMotor(Asimov.RIGHT_BACK_MOTOR))

    private val arm = VirtualMotor(Asimov.DEPOT_ARM)
    private val roller = VirtualMotor(Asimov.DEPOT_WHEEL)
    private val ramp = VirtualMotor(Asimov.RAMP)

    override val angle: Double
        get() = -gyro.getDouble(0.0)

    override fun drivetrainMotors(): Array<SpeedController> {
        return motors
    }

    override fun resetGyro() {
        gyro.setDouble(0.0)
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

    override fun ultraSonicDist(): Double {
        return 0.0
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
        ramplock.setBoolean(true)
    }

    override fun retractRampLock() {
        ramplock.setBoolean(false)
    }

    override fun init() {

    }
}
