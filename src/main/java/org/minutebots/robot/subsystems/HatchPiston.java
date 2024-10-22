package org.minutebots.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.minutebots.robot.Robot;

public class HatchPiston extends Subsystem {
    private NetworkTableEntry dist = Shuffleboard.getTab("Drive").add("Distance", Robot.hardwareManager.ultraSonicDist()).getEntry();

    HatchPiston() {
    }

    public void periodic() {
        dist.setDouble(Robot.hardwareManager.ultraSonicDist());
    }

    public static InstantCommand extend() {
        return new InstantCommand("Piston Extend", getInstance(), () ->{
                Robot.hardwareManager.extendIntakePiston();
                Robot.hardwareManager.retractActiveHatch();
        });
    }

    public static InstantCommand grabHatch() {
        return new InstantCommand("Grab Hatch", getInstance(), () ->{
                Robot.hardwareManager.extendActiveHatch();
        });
    }

    public static InstantCommand retractHatchM() {
        return new InstantCommand("Retract Hatch Mechanism", getInstance(), () ->{
                Robot.hardwareManager.retractActiveHatch();
        });
    }

    public static CommandGroup retract() {
        return new CommandGroup() {
            { //Anonymous constructor. The method header is blank as the class has no name. This anonymous class extends CommandGroup and
                addSequential(new InstantCommand(HatchPiston.getInstance(), () ->
                        Robot.hardwareManager.retractIntakePiston()));
                addSequential(new WaitCommand(1));
                addSequential(new InstantCommand(HatchPiston.getInstance(), () ->
                        Robot.hardwareManager.closeSolenoids()));
            }
        };
    }

    public static CommandGroup eject() {
        return new CommandGroup() {
            {
                setName("Eject Hatch");
                requires(HatchPiston.getInstance());
                addSequential(HatchPiston.extend());
                addSequential(new WaitCommand(0.5));
                addSequential(HatchPiston.retract());
            }
        };
    }

    public void initDefaultCommand() {
    }

    private static HatchPiston hatchPiston = new HatchPiston();

    public static HatchPiston getInstance() {
        return hatchPiston;
    }
}