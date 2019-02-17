package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;
import org.minutebots.robot.Robot;

public class Ramp extends Subsystem {

    public void periodic(){
        setWheel(-OI.secondaryStick.getMagnitude());
    }

    public void setWheel(double speed){
        Robot.hardwareManager.rampMotor().set(speed);
    }

    public void initDefaultCommand(){
    }

    private static Ramp ramp = new Ramp();

    public static Ramp getInstance(){
        return ramp;
    }
}
