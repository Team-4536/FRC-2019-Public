package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

public class Intake extends Subsystem {
    private DoubleSolenoid velcro = new DoubleSolenoid(OI.INTAKE_1, OI.INTAKE_2),
            cone = new DoubleSolenoid(OI.CONE_1, OI.CONE_2);

    private Intake() {
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

    private static Intake intake = new Intake();

    public static Intake getInstance() {
        return intake;
    }
}
