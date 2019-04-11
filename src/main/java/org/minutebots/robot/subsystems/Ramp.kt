package org.minutebots.robot.subsystems

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.command.WaitCommand

import org.minutebots.robot.OI
import org.minutebots.robot.Robot

class Ramp : Subsystem() {

    override fun periodic() {
        setWheel(if (OI.fineTurn.get()) -OI.secondaryStick.y else 0)
    }

    fun setWheel(speed: Double) {
        Robot.hardwareManager.rampMotor().set(speed)
    }

    public override fun initDefaultCommand() {}

    companion object {

        fun spinWheel(speed: Double): InstantCommand {
            return InstantCommand {
                instance.setWheel(speed)
                if (speed > 0.5) Ramp.retractLock().start()
            }
        }

        fun extendLock(): InstantCommand {
            return InstantCommand { Robot.hardwareManager.extendRampLock() }
        }

        fun retractLock(): InstantCommand {
            return InstantCommand { Robot.hardwareManager.retractRampLock() }
        }

        fun lockCargo(): CommandGroup {
            return object : CommandGroup() {
                init {
                    addSequential(spinWheel(0.3))
                    addSequential(extendLock())
                    addSequential(WaitCommand(0.1))
                    addSequential(spinWheel(0.0))
                }
            }
        }

        val instance = Ramp()
    }
}
