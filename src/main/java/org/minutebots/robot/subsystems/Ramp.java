package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;
import org.minutebots.robot.Robot;
import org.minutebots.robot.utilities.Constants;

public class Ramp extends Subsystem {

    @Override
    public void periodic(){
        if (OI.ramp.get()) setWheel(Constants.RAMP_MAX_SPEED);
        else setWheel(0);
    }

    private void setWheel(double speed){
        Robot.hardwareManager.rampMotor().set(speed);
    }

    public void initDefaultCommand(){
    }

    private static Ramp ramp = new Ramp();

    public static Ramp getInstance(){
        return ramp;
    }
}
