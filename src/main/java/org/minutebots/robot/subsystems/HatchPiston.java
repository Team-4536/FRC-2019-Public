package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchPiston extends Subsystem {
    private DoubleSolenoid piston;

    HatchPiston(DoubleSolenoid piston) {
        SmartDashboard.putData(this);
        this.piston = piston;
    }

    public static InstantCommand extend() {
        return new InstantCommand(getInstance(), () ->
                getInstance().piston.set(DoubleSolenoid.Value.kReverse));
    }

    public static InstantCommand retract() {
        return new InstantCommand(getInstance(), () ->
                getInstance().piston.set(DoubleSolenoid.Value.kForward));
    }

    public static CommandGroup eject(){
        return new EjectHatch();
    }

    public void initDefaultCommand() {
    }

    public static HatchPiston getInstance() {
        return Superstructure.getInstance().hatchPiston;
    }
}

class EjectHatch extends CommandGroup {
    EjectHatch(){
        addSequential(HatchPiston.extend());
        addSequential(new WaitCommand(0.5));
        addSequential(HatchPiston.retract());
    }
}