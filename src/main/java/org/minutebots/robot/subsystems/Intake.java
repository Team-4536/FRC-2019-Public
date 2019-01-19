package org.minutebots.robot.subsystems;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.RobotMap;

import static org.minutebots.robot.RobotMap.Cone_2;

public class Intake extends Subsystem {

    private DoubleSolenoid Velcro1;
    private DoubleSolenoid Cone;
    public static Intake intake = new Intake();

    private Intake(){
        Velcro1 = new DoubleSolenoid(RobotMap.INTAKE_1, RobotMap.INTAKE_2);
        Cone = new DoubleSolenoid(RobotMap.Cone_1, RobotMap.Cone_2);
    }

    public void setIntakeStatus(boolean out){
        if (out){
            Velcro1.set(DoubleSolenoid.Value.kForward);
        }
        else if (! out){
            Velcro1.set(DoubleSolenoid.Value.kReverse);
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
