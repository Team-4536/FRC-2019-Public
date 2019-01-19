package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

public class Intake extends Subsystem {
    private DoubleSolenoid velcro = new DoubleSolenoid(OI.INTAKE_1, OI.INTAKE_2),
            cone = new DoubleSolenoid(OI.CONE_1, OI.CONE_2);

    private Intake() {
    }

    public void setIntakeStatus(boolean out) {
        if (out) {
            velcro.set(DoubleSolenoid.Value.kForward);
        } else {
            velcro.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void setConeStatus(boolean out) {
        if (out) {
            cone.set(DoubleSolenoid.Value.kForward);
        } else {
            cone.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void initDefaultCommand() {
    }

    private static Intake intake = new Intake();

    public static Intake getInstance() {
        return intake;
    }
}
