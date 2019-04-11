package org.minutebots.robot.subsystems

import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.command.Subsystem
import org.minutebots.robot.Robot
import org.minutebots.robot.utilities.Constants

class DepotArm : Subsystem() {

    private var isDown = false

    private fun moveArm(speed: Double) {
        var motorSpeed = speed
        if (Robot.hardwareManager.armUp()) motorSpeed = if (speed < 0) speed else 0
        if (Robot.hardwareManager.armDown()) motorSpeed = if (speed > 0) speed else 0
        Robot.hardwareManager.depotArm().set(motorSpeed)
    }

    override fun periodic() {
        if (isDown)
            moveArm(-Constants.DEPOT_DOWN_MAX_SPEED)
        else
            moveArm(Constants.DEPOT_UP_MAX_SPEED)
    }

    private fun spinWheel(speed: Double) {
        Robot.hardwareManager.depotRoller().set(speed)
    }

    public override fun initDefaultCommand() {}

    companion object {

        fun armDown(forwards: Boolean): InstantCommand {
            return InstantCommand {
                instance.isDown = true
                instance.spinWheel((if (forwards) 1 else -1) * Constants.DEPOT_SPIN_MAX_SPEED)
            }
        }

        fun armUp(): InstantCommand {
            return InstantCommand {
                instance.isDown = false
                instance.spinWheel(0.0)
            }
        }


        fun toggleArm(): InstantCommand {
            return InstantCommand { instance.isDown = !instance.isDown }
        }

        val instance = DepotArm()
    }
}
