package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.minutebots.robot.Robot;

public class HatchPiston extends Subsystem {

    HatchPiston() {
        SmartDashboard.putData(this);
    }

    public static InstantCommand extend() {
        return new InstantCommand("Piston Extend",getInstance(), () ->
                Robot.robot.extendIntakePiston());
    }

    public static InstantCommand retract() {
        return new InstantCommand("Piston Retract",getInstance(), () ->
                Robot.robot.retractIntakePiston());
    }

    public static CommandGroup eject(){
        return new EjectHatch();
    }

    public void initDefaultCommand() {
    }

    private static HatchPiston hatchPiston = new HatchPiston();

    public static HatchPiston getInstance() {
        return hatchPiston;
    }
}

class EjectHatch extends CommandGroup {
    EjectHatch(){
        setName("Eject Hatch");
        requires(HatchPiston.getInstance());
        addSequential(HatchPiston.extend());
        addSequential(new WaitCommand(0.5));
        addSequential(HatchPiston.retract());
    }
}