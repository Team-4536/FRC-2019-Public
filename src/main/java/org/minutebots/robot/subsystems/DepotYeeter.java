package org.minutebots.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

public class DepotYeeter extends Subsystem {
    private WPI_VictorSPX sideWheel = new WPI_VictorSPX(OI.SIDE_WHEEL);

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
