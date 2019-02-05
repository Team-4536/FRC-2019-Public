package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DepotArm extends Subsystem {
    private SpeedController sideWheel;

    DepotArm(SpeedController sideWheel){
        this.sideWheel = sideWheel;
    }

    public void setSideWheel(double speed){
        sideWheel.set(speed);
    }

    public void initDefaultCommand(){
    }

    public static DepotArm getInstance(){
        return Superstructure.getInstance().depotArm;
    }
}
