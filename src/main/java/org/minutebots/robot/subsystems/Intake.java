package org.minutebots.robot.subsystems;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

import static org.minutebots.robot.OI.*;

public class Intake extends Subsystem {

    private DoubleSolenoid Velcro;
    private DoubleSolenoid Cone;
    public static Intake intake = new Intake();

    private Intake(){
        Velcro = new DoubleSolenoid(OI.INTAKE_1, OI.INTAKE_2);
        Cone = new DoubleSolenoid(OI.CONE_1, OI.CONE_2);
    }

    public void setIntakeStatus(boolean out){
        if (out){
            Velcro.set(DoubleSolenoid.Value.kForward);
        }
        else if (!out){
            Velcro.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void setConeStatus(boolean out){
        if (out){
            Cone.set(DoubleSolenoid.Value.kForward);
        }
        else if (!out){
            Cone.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void initDefaultCommand(){

    }
}
