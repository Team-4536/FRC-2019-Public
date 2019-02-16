package org.minutebots.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.minutebots.robot.Robot;

public class HatchPiston extends Subsystem {
    private NetworkTableEntry dist = Shuffleboard.getTab("Debugging").add("Distance With Ultrasonic", Robot.hardwareManager.ultraSonicDist()).getEntry();

    HatchPiston() {
        SmartDashboard.putData(this);
    }

    public void periodic(){
        dist.setDouble(Robot.hardwareManager.ultraSonicDist());
    }

    public static InstantCommand extend() {
        return new InstantCommand("Piston Extend",getInstance(), () ->
                Robot.hardwareManager.extendIntakePiston());
    }

    public static CommandGroup retract() {
        return new RetractPiston();
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

class RetractPiston extends CommandGroup {
    RetractPiston(){
        addSequential(new InstantCommand(HatchPiston.getInstance(), () ->
        Robot.hardwareManager.retractIntakePiston()));
        addSequential(new WaitCommand(1));
        addSequential(new InstantCommand(HatchPiston.getInstance(), () ->
        Robot.hardwareManager.closeSolenoids()));
    }
}