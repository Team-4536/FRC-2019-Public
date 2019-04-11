package org.minutebots.robot.subsystems

import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.command.WaitCommand
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import org.minutebots.robot.Robot

class HatchPiston internal constructor() : Subsystem() {
    private val dist = Shuffleboard.getTab("Drive").add("Distance", Robot.hardwareManager.ultraSonicDist()).entry

    override fun periodic() {
        dist.setDouble(Robot.hardwareManager.ultraSonicDist())
    }

    public override fun initDefaultCommand() {}

    companion object {

        fun extend(): InstantCommand {
            return InstantCommand("Piston Extend", instance) {
                Robot.hardwareManager.extendIntakePiston()
                Robot.hardwareManager.retractActiveHatch()
            }
        }

        fun grabHatch(): InstantCommand {
            return InstantCommand("Grab Hatch", instance) { Robot.hardwareManager.extendActiveHatch() }
        }

        fun retractHatchM(): InstantCommand {
            return InstantCommand("Retract Hatch Mechanism", instance) { Robot.hardwareManager.retractActiveHatch() }
        }

        fun retract(): CommandGroup {
            return object : CommandGroup() {
                init { //Anonymous constructor. The method header is blank as the class has no name. This anonymous class extends CommandGroup and
                    addSequential(InstantCommand(HatchPiston.instance) { Robot.hardwareManager.retractIntakePiston() })
                    addSequential(WaitCommand(1.0))
                    addSequential(InstantCommand(HatchPiston.instance) { Robot.hardwareManager.closeSolenoids() })
                }
            }
        }

        fun eject(): CommandGroup {
            return object : CommandGroup() {
                init {
                    name = "Eject Hatch"
                    requires(HatchPiston.instance)
                    addSequential(HatchPiston.extend())
                    addSequential(WaitCommand(0.5))
                    addSequential(HatchPiston.retract())
                }
            }
        }

        val instance = HatchPiston()
    }
}