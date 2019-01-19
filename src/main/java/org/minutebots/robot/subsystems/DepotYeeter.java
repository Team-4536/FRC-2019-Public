package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

public class DepotYeeter extends Subsystem {
    private PWMVictorSPX sideWheel = new PWMVictorSPX(OI.SIDE_WHEEL);

    private DepotYeeter(){
    }

    public void setSideWheel(double speed){
        sideWheel.set(speed);
    }

    public void initDefaultCommand(){
    }

    private static DepotYeeter depotYeeter = new DepotYeeter();
    public static DepotYeeter getInstance(){
        return depotYeeter;
    }
}
