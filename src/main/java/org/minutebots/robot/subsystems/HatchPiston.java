package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchPiston extends Subsystem {
    private DoubleSolenoid piston;

    HatchPiston(DoubleSolenoid piston) {
        SmartDashboard.putData(this);
        this.piston = piston;
    }

    public static InstantCommand extend() {
        return new InstantCommand(getInstance(), () ->
                getInstance().piston.set(DoubleSolenoid.Value.kForward));
    }

    public static InstantCommand retract() {
        return new InstantCommand(getInstance(), () ->
                getInstance().piston.set(DoubleSolenoid.Value.kReverse));
    }

    public void initDefaultCommand() {
    }

    public static HatchPiston getInstance() {
        return Superstructure.getInstance().hatchPiston;
    }
}
