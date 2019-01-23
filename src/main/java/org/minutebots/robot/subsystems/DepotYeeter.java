package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DepotYeeter extends Subsystem {
    private SpeedController sideWheel;

    DepotYeeter(SpeedController sideWheel){
        this.sideWheel = sideWheel;
    }

    public void setSideWheel(double speed){
        sideWheel.set(speed);
    }

    public void initDefaultCommand(){
    }

    public static DepotYeeter getInstance(){
        return Superstructure.getInstance().depotYeeter;
    }
}
