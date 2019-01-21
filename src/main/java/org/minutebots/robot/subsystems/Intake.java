package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {
    private DoubleSolenoid velcro, cone;

    Intake(DoubleSolenoid velcro, DoubleSolenoid cone) {
        this.velcro = velcro;
        this.cone = cone;
    }

    private void setIntakeStatus(boolean out) {
        if (out) {
            velcro.set(DoubleSolenoid.Value.kForward);
        } else {
            velcro.set(DoubleSolenoid.Value.kReverse);
        }
    }

    private void setConeStatus(boolean out) {
        if (out) {
            cone.set(DoubleSolenoid.Value.kForward);
        } else {
            cone.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public static InstantCommand extend() {
        return new InstantCommand(getInstance(), () -> {
            getInstance().setIntakeStatus(true);
            getInstance().setConeStatus(true);
        });
    }

    public static InstantCommand retract() {
        return new InstantCommand(getInstance(), () -> {
            getInstance().setIntakeStatus(false);
            getInstance().setConeStatus(false);
        });
    }

    public void initDefaultCommand() {
    }

    public static Intake getInstance() {
        return Superstructure.getInstance().intake;
    }
}
