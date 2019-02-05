package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Ramp extends Subsystem {

    private SpeedController wheel;

    Ramp(SpeedController wheel){
        this.wheel = wheel;
    }

    public void setWheel(double speed){
        wheel.set(speed);
    }

    public void initDefaultCommand(){

    }

    public static Ramp getInstance(){
        return Superstructure.getInstance().ramp;
    }
}
