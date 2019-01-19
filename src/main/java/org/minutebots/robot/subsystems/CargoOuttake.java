package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

public class CargoOuttake extends Subsystem {

    private PWMVictorSPX wheel = new PWMVictorSPX(OI.WHEEL);

    private CargoOuttake(){
    }

    public void setWheel(double speed){
        wheel.set(speed);
    }

    public void initDefaultCommand(){

    }

    private static CargoOuttake cargoOuttake = new CargoOuttake();
    public static CargoOuttake getInstance(){
        return cargoOuttake;
    }
}
