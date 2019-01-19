package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

public class CargoOuttake extends Subsystem {

    public static CargoOuttake cargoOuttake = new CargoOuttake();
    private PWMVictorSPX Wheel;

    private CargoOuttake(){
        Wheel = new PWMVictorSPX(OI.WHEEL);
    }

    public void setWheel(double speed){
        Wheel.set(speed);
    }

    public void initDefaultCommand(){

    }
}
